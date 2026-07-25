package com.ocupa.ocupa.service;

import com.ocupa.ocupa.model.*;
import com.ocupa.ocupa.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.Normalizer;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SeedingService {
    private final UserRepository userRepo;
    private final ArtistaRepository artistaRepo;
    private final PortfolioRepository portfolioRepo;
    private final EspacoRepository espacoRepo;
    private final JdbcTemplate jdbcTemplate;
    private final DataSource dataSource;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public void seedDatabase() {
        try {
            jdbcTemplate.execute("SET GLOBAL max_allowed_packet=67108864");
            System.out.println("max_allowed_packet definido para 64MB globalmente!");
            if (dataSource instanceof com.zaxxer.hikari.HikariDataSource) {
                ((com.zaxxer.hikari.HikariDataSource) dataSource).getHikariPoolMXBean().softEvictConnections();
                System.out.println("Conexoes do pool Hikari reiniciadas para herdar o novo max_allowed_packet.");
            }
        } catch (Exception e) {
            System.err.println("Nao foi possivel definir max_allowed_packet: " + e.getMessage());
        }

        // Register admin@admin.com
        if (!userRepo.findByEmail("admin@admin.com").isPresent()) {
            User admin = new User();
            admin.setNome("admin");
            admin.setEmail("admin@admin.com");
            admin.setSenha(encoder.encode("admin"));
            admin.setRole("ADMIN");
            userRepo.save(admin);
        }
        
        // Register admi@admin.com (to cover the typo version)
        if (!userRepo.findByEmail("admi@admin.com").isPresent()) {
            User admin = new User();
            admin.setNome("admin");
            admin.setEmail("admi@admin.com");
            admin.setSenha(encoder.encode("admin"));
            admin.setRole("ADMIN");
            userRepo.save(admin);
        }

        // Popular artistas a partir da pasta "Popular artistas"
        File popularDir = new File("Popular artistas");
        if (popularDir.exists() && popularDir.isDirectory()) {
            System.out.println("Encontrada pasta 'Popular artistas'. Iniciando populacao de dados...");
            File[] artistDirs = popularDir.listFiles(File::isDirectory);
            if (artistDirs != null) {
                for (File artistDir : artistDirs) {
                    try {
                        File txtFile = new File(artistDir, "Portfolio.txt");
                        if (!txtFile.exists()) continue;
                        
                        Map<String, String> data = new HashMap<>();
                        for (String line : Files.readAllLines(txtFile.toPath(), StandardCharsets.UTF_8)) {
                            line = line.trim();
                            if (line.isEmpty()) continue;
                            int colIdx = line.indexOf(":");
                            if (colIdx != -1) {
                                String rawKey = line.substring(0, colIdx);
                                String normalizedKey = Normalizer.normalize(rawKey, Normalizer.Form.NFD)
                                    .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                                    .toLowerCase()
                                    .replaceAll("[^a-z0-9]", "")
                                    .trim();
                                String val = line.substring(colIdx + 1).trim();
                                data.put(normalizedKey, val);
                            }
                        }
                        
                        String email = data.get("email");
                        if (email == null || email.isEmpty()) continue;
                        
                        // Verificar se o usuario ja existe
                        if (userRepo.findByEmail(email).isPresent()) {
                            System.out.println("Usuario " + email + " ja existe no banco. Pulando...");
                            continue;
                        }
                        
                        String nome = data.get("nomeartisticocoletivo");
                        String senha = data.get("senha");
                        String categoria = data.get("suacategoriadeatuacaoprincipal");
                        String cidade = data.get("cidade");
                        String headline = data.get("headlinesloganprofissional");
                        String about = data.get("sobrevocebiodetalhadadeportfolio");
                        String contato = data.get("contato");
                        
                        // Buscar a foto de perfil do artista
                        String fotoUrl = null;
                        File[] files = artistDir.listFiles();
                        if (files != null) {
                            for (File f : files) {
                                if (f.isFile() && f.getName().toLowerCase().startsWith("foto de perfil do artista")) {
                                    fotoUrl = getBase64DataUrl(f);
                                    break;
                                }
                            }
                        }
                        
                        // 1. Criar Artista
                        Artista artista = new Artista();
                        artista.setNome(nome);
                        artista.setCategoria(categoria);
                        artista.setCidade(cidade);
                        artista.setFotoUrl(fotoUrl);
                        artista = artistaRepo.save(artista);
                        
                        // 2. Criar Usuario
                        User user = new User();
                        user.setNome(nome);
                        user.setEmail(email);
                        user.setSenha(encoder.encode(senha != null ? senha : "1234"));
                        user.setRole("ARTISTA");
                        user.setArtistaId(artista.getId());
                        userRepo.save(user);
                        
                        // 3. Criar Portfolio
                        Portfolio portfolio = new Portfolio();
                        portfolio.setArtista(artista);
                        portfolio.setHeadline(headline);
                        portfolio.setAbout(about);
                        portfolio.setContacts(contato != null ? contato : email);
                        
                        // 4. Carregar Midias do Portfolio
                        File mediaDir = new File(artistDir, "Mídias do Portfólio");
                        if (mediaDir.exists() && mediaDir.isDirectory()) {
                            File[] mediaFiles = mediaDir.listFiles(f -> f.isFile() && (f.getName().toLowerCase().endsWith(".png") || f.getName().toLowerCase().endsWith(".jpg") || f.getName().toLowerCase().endsWith(".jpeg")));
                            if (mediaFiles != null) {
                                for (File mf : mediaFiles) {
                                    String mBase64 = getBase64DataUrl(mf);
                                    if (mBase64 != null) {
                                        PortfolioMedia pm = new PortfolioMedia();
                                        pm.setMediaType("IMAGE");
                                        pm.setUrl(mBase64);
                                        String caption = mf.getName();
                                        int dotIdx = caption.lastIndexOf(".");
                                        if (dotIdx != -1) {
                                            caption = caption.substring(0, dotIdx);
                                        }
                                        pm.setCaption(caption);
                                        pm.setPortfolio(portfolio);
                                        portfolio.getMediaItems().add(pm);
                                    }
                                }
                            }
                        }
                        
                        portfolioRepo.save(portfolio);
                        System.out.println("Artista " + nome + " cadastrado e populado com sucesso!");
                    } catch (Exception ex) {
                        System.err.println("Erro ao popular artista na pasta " + artistDir.getName() + ": " + ex.getMessage());
                    }
                }
            }
        }

        // Popular espaços a partir da pasta "Popular lugares"
        File popularPlacesDir = new File("Popular lugares");
        if (popularPlacesDir.exists() && popularPlacesDir.isDirectory()) {
            System.out.println("Encontrada pasta 'Popular lugares'. Iniciando populacao de dados...");
            File[] placeDirs = popularPlacesDir.listFiles(File::isDirectory);
            if (placeDirs != null) {
                for (File placeDir : placeDirs) {
                    try {
                        File txtFile = new File(placeDir, "Cadastro.txt");
                        if (!txtFile.exists()) continue;
                        
                        Map<String, String> data = new HashMap<>();
                        for (String line : Files.readAllLines(txtFile.toPath(), StandardCharsets.UTF_8)) {
                            line = line.trim();
                            if (line.isEmpty()) continue;
                            int colIdx = line.indexOf(":");
                            if (colIdx != -1) {
                                String rawKey = line.substring(0, colIdx);
                                String normalizedKey = Normalizer.normalize(rawKey, Normalizer.Form.NFD)
                                    .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                                    .toLowerCase()
                                    .replaceAll("[^a-z0-9]", "")
                                    .trim();
                                String val = line.substring(colIdx + 1).trim();
                                data.put(normalizedKey, val);
                            }
                        }
                        
                        String nome = data.get("nomedoespaco");
                        if (nome == null || nome.isEmpty()) continue;

                        // Verificar se o espaco ja existe
                        if (espacoRepo.findByNome(nome).isPresent()) {
                            System.out.println("Espaco " + nome + " ja existe no banco. Pulando...");
                            continue;
                        }
                        
                        // 1. Criar Espaco
                        Espaco espaco = new Espaco();
                        espaco.setNome(nome);
                        espaco.setEndereco(data.get("endereco"));
                        espaco.setDescricao(data.get("descricaohistorico"));
                        espaco.setCriadoPorEmail("admin@admin.com");
                        
                        // Parse capacidade
                        String capRaw = data.get("capacidadeaproximadadepublico");
                        if (capRaw != null) {
                            try {
                                espaco.setCapacidade(Integer.parseInt(capRaw.trim()));
                            } catch (Exception ex) {
                                espaco.setCapacidade(0);
                            }
                        } else {
                            espaco.setCapacidade(0);
                        }
                        
                        // Parse latitude/longitude
                        String latLongRaw = data.get("latitudelongitude");
                        if (latLongRaw != null && latLongRaw.contains(",")) {
                            String[] parts = latLongRaw.split(",");
                            try {
                                espaco.setLatitude(Double.parseDouble(parts[0].trim()));
                                espaco.setLongitude(Double.parseDouble(parts[1].trim()));
                            } catch (Exception ex) {
                                System.err.println("Erro ao parsear coordenadas: " + ex.getMessage());
                            }
                        }
                        
                        // Parse attributes
                        String attrRaw = data.get("atributosfisicos");
                        if (attrRaw != null) {
                            String normalizedAttr = Normalizer.normalize(attrRaw, Normalizer.Form.NFD)
                                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                                .toLowerCase();
                            espaco.setCobertura(normalizedAttr.contains("cober"));
                            espaco.setIluminacao(normalizedAttr.contains("ilumi"));
                            espaco.setEnergia(normalizedAttr.contains("energi"));
                            espaco.setBanheiro(normalizedAttr.contains("banhe"));
                        }
                        
                        // Parse activities
                        String actRaw = data.get("atividadespermitidas");
                        if (actRaw != null) {
                            String normalizedAct = Normalizer.normalize(actRaw, Normalizer.Form.NFD)
                                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                                .toLowerCase();
                            espaco.setPermiteGrafite(normalizedAct.contains("grafi") || normalizedAct.contains("mural"));
                            espaco.setPermiteBatalha(normalizedAct.contains("batalh"));
                            espaco.setPermiteDanca(normalizedAct.contains("danc"));
                        }

                        // 2. Carregar mídias da galeria
                        File galleryDir = new File(placeDir, "Galeria de Fotos do Local");
                        if (galleryDir.exists() && galleryDir.isDirectory()) {
                            File[] mediaFiles = galleryDir.listFiles();
                            if (mediaFiles != null) {
                                for (File mf : mediaFiles) {
                                    if (mf.isFile() && (mf.getName().toLowerCase().endsWith(".png") || mf.getName().toLowerCase().endsWith(".jpg") || mf.getName().toLowerCase().endsWith(".jpeg"))) {
                                        String mBase64 = getBase64DataUrl(mf);
                                        if (mBase64 != null) {
                                            EspacoMedia em = new EspacoMedia();
                                            em.setMediaType("IMAGE");
                                            em.setUrl(mBase64);
                                            em.setCaption(""); // Sem legenda
                                            em.setEspaco(espaco);
                                            espaco.getMediaItems().add(em);
                                        }
                                    }
                                }
                            }
                        }
                        
                        espacoRepo.save(espaco);
                        System.out.println("Espaco " + nome + " cadastrado e populado com sucesso!");
                    } catch (Exception ex) {
                        System.err.println("Erro ao popular espaco na pasta " + placeDir.getName() + ": " + ex.getMessage());
                    }
                }
            }
        }
    }

    private String getBase64DataUrl(File file) {
        try {
            byte[] bytes = Files.readAllBytes(file.toPath());
            String base64 = Base64.getEncoder().encodeToString(bytes);
            String ext = file.getName().substring(file.getName().lastIndexOf(".") + 1).toLowerCase();
            String mimeType = "image/" + ext;
            if (ext.equals("jpg") || ext.equals("jpeg")) {
                mimeType = "image/jpeg";
            }
            return "data:" + mimeType + ";base64," + base64;
        } catch (Exception e) {
            return null;
        }
    }
}
