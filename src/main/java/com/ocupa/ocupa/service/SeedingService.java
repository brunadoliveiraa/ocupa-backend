package com.ocupa.ocupa.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ocupa.ocupa.model.*;
import com.ocupa.ocupa.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.File;
import java.util.Base64;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SeedingService {
    private final UserRepository userRepo;
    private final ArtistaRepository artistaRepo;
    private final PortfolioRepository portfolioRepo;
    private final EspacoRepository espacoRepo;
    private final EventoRepository eventoRepo;
    private final OportunidadeRepository oportunidadeRepo;
    private final JdbcTemplate jdbcTemplate;
    private final DataSource dataSource;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    private final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    public void seedDatabase() {
        configureDatabasePacketSize();
        seedAdminUsers();
        seedArtistas();
        seedEspacos();
        seedEventos();
        seedOportunidades();
    }

    private void configureDatabasePacketSize() {
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
    }

    private void seedAdminUsers() {
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
    }

    private void seedArtistas() {
        File popularDir = new File("Popular artistas");
        if (!popularDir.exists() || !popularDir.isDirectory()) return;

        System.out.println("Encontrada pasta 'Popular artistas'. Iniciando populacao de dados...");
        File[] artistDirs = popularDir.listFiles(File::isDirectory);
        if (artistDirs == null) return;

        for (File artistDir : artistDirs) {
            try {
                File jsonFile = new File(artistDir, "Portfolio.json");
                if (!jsonFile.exists()) continue;
                
                Map<?, ?> root = objectMapper.readValue(jsonFile, Map.class);
                
                Artista artista = objectMapper.convertValue(root.get("artista"), Artista.class);
                User user = objectMapper.convertValue(root.get("user"), User.class);
                Portfolio portfolio = objectMapper.convertValue(root.get("portfolio"), Portfolio.class);
                
                if (user == null || user.getEmail() == null || user.getEmail().isEmpty()) continue;
                
                // Verificar se o usuario ja existe
                if (userRepo.findByEmail(user.getEmail()).isPresent()) {
                    System.out.println("Usuario " + user.getEmail() + " ja existe no banco. Pulando...");
                    continue;
                }
                
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
                artista.setFotoUrl(fotoUrl);
                artista.setStatus("APROVADO");
                artista = artistaRepo.save(artista);
                
                // 2. Criar Usuario
                user.setNome(artista.getNome());
                user.setSenha(encoder.encode(user.getSenha() != null ? user.getSenha() : "123"));
                user.setRole("ARTISTA");
                user.setArtistaId(artista.getId());
                userRepo.save(user);
                
                // 3. Criar Portfolio
                portfolio.setArtista(artista);
                if (portfolio.getContacts() == null) {
                    portfolio.setContacts(user.getEmail());
                }
                
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
                System.out.println("Artista " + artista.getNome() + " cadastrado e populado com sucesso!");
            } catch (Exception ex) {
                System.err.println("Erro ao popular artista na pasta " + artistDir.getName() + ": " + ex.getMessage());
            }
        }
    }

    private void seedEspacos() {
        File popularPlacesDir = new File("Popular espacos");
        if (!popularPlacesDir.exists() || !popularPlacesDir.isDirectory()) return;

        System.out.println("Encontrada pasta 'Popular espacos'. Iniciando populacao de dados...");
        File[] placeDirs = popularPlacesDir.listFiles(File::isDirectory);
        if (placeDirs == null) return;

        for (File placeDir : placeDirs) {
            try {
                File jsonFile = new File(placeDir, "Cadastro.json");
                if (!jsonFile.exists()) continue;
                
                Espaco espaco = objectMapper.readValue(jsonFile, Espaco.class);
                if (espaco == null || espaco.getNome() == null || espaco.getNome().isEmpty()) continue;

                // Verificar se o espaco ja existe
                if (espacoRepo.findByNome(espaco.getNome()).isPresent()) {
                    System.out.println("Espaco " + espaco.getNome() + " ja existe no banco. Pulando...");
                    continue;
                }
                
                // Configurar metadados adicionais do Espaco
                espaco.setCriadoPorEmail("admin@admin.com");
                espaco.setStatus("APROVADO");

                // Carregar mídias da galeria
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
                                    em.setCaption("");
                                    em.setEspaco(espaco);
                                    espaco.getMediaItems().add(em);
                                }
                            }
                        }
                    }
                }
                
                espacoRepo.save(espaco);
                System.out.println("Espaco " + espaco.getNome() + " cadastrado e populado com sucesso!");
            } catch (Exception ex) {
                System.err.println("Erro ao popular espaco na pasta " + placeDir.getName() + ": " + ex.getMessage());
            }
        }
    }

    private void seedEventos() {
        File popularEventsDir = new File("Popular eventos");
        if (!popularEventsDir.exists() || !popularEventsDir.isDirectory()) return;

        System.out.println("Encontrada pasta 'Popular eventos'. Iniciando populacao de dados...");
        File[] eventDirs = popularEventsDir.listFiles(File::isDirectory);
        if (eventDirs == null) return;

        for (File eventDir : eventDirs) {
            try {
                File jsonFile = new File(eventDir, "Detalhes.json");
                if (!jsonFile.exists()) continue;

                Map<?, ?> root = objectMapper.readValue(jsonFile, Map.class);

                String nome = (String) root.get("nome");
                if (nome == null || nome.isEmpty()) continue;

                // Verificar se o evento ja existe
                if (eventoRepo.findByNome(nome).isPresent()) {
                    System.out.println("Evento " + nome + " ja existe no banco. Pulando...");
                    continue;
                }

                Evento evento = new Evento();
                evento.setNome(nome);
                evento.setDescricao((String) root.get("descricao"));
                
                String dateStr = (String) root.get("dataEvento");
                if (dateStr != null) {
                    evento.setDataEvento(java.time.LocalDate.parse(dateStr));
                }
                
                String timeStr = (String) root.get("horaEvento");
                if (timeStr != null) {
                    evento.setHoraEvento(java.time.LocalTime.parse(timeStr));
                }

                // Vincular Artista
                String artistaNome = (String) root.get("artistaNome");
                if (artistaNome != null) {
                    artistaRepo.findByNome(artistaNome).ifPresent(evento::setArtista);
                }

                // Vincular Espaço
                String espacoNome = (String) root.get("espacoNome");
                if (espacoNome != null) {
                    espacoRepo.findByNome(espacoNome).ifPresent(espaco -> {
                        evento.setEspaco(espaco);
                        evento.setLatitude(espaco.getLatitude());
                        evento.setLongitude(espaco.getLongitude());
                    });
                }

                evento.setCriadoPorEmail("admin@admin.com");
                evento.setStatus("APROVADO");

                // Buscar a foto de capa do evento
                String fotoUrl = null;
                File[] files = eventDir.listFiles();
                if (files != null) {
                    for (File f : files) {
                        if (f.isFile() && f.getName().toLowerCase().startsWith("capa")) {
                            fotoUrl = getBase64DataUrl(f);
                            break;
                        }
                    }
                }
                evento.setFotoUrl(fotoUrl);

                eventoRepo.save(evento);
                System.out.println("Evento " + nome + " cadastrado e populado com sucesso!");
            } catch (Exception ex) {
                System.err.println("Erro ao popular evento na pasta " + eventDir.getName() + ": " + ex.getMessage());
            }
        }
    }

    private String getBase64DataUrl(File file) {
        try {
            byte[] bytes = java.nio.file.Files.readAllBytes(file.toPath());
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
    private void seedOportunidades() {
        File popularOpDir = new File("Popular oportunidades");
        if (!popularOpDir.exists() || !popularOpDir.isDirectory()) return;

        System.out.println("Encontrada pasta 'Popular oportunidades'. Iniciando populacao de dados...");
        File[] opDirs = popularOpDir.listFiles(File::isDirectory);
        if (opDirs == null) return;

        for (File opDir : opDirs) {
            try {
                File jsonFile = new File(opDir, "oportunidade.json");
                if (!jsonFile.exists()) continue;

                Map<?, ?> root = objectMapper.readValue(jsonFile, Map.class);

                String titulo = (String) root.get("titulo");
                if (titulo == null || titulo.isEmpty()) continue;

                if (oportunidadeRepo.findByTitulo(titulo).isPresent()) {
                    System.out.println("Oportunidade " + titulo + " ja existe no banco. Pulando...");
                    continue;
                }

                Oportunidade op = new Oportunidade();
                op.setTitulo(titulo);
                op.setTipo((String) root.get("tipo"));
                op.setDescricao((String) root.get("descricao"));
                op.setLocal((String) root.get("local"));
                op.setInscricaoLink((String) root.get("inscricaoLink"));

                String dataInicioStr = (String) root.get("dataInicio");
                if (dataInicioStr != null) {
                    op.setDataInicio(java.time.LocalDate.parse(dataInicioStr));
                }
                String dataFimStr = (String) root.get("dataFim");
                if (dataFimStr != null) {
                    op.setDataFim(java.time.LocalDate.parse(dataFimStr));
                }

                op.setCriadoPorEmail("admin@admin.com");
                op.setStatus("APROVADO");

                // Buscar foto de capa
                File[] files = opDir.listFiles();
                if (files != null) {
                    for (File f : files) {
                        String name = f.getName().toLowerCase();
                        if (f.isFile() && !name.endsWith(".json") && (name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".png") || name.endsWith(".webp"))) {
                            op.setFotoUrl(getBase64DataUrl(f));
                            break;
                        }
                    }
                }

                oportunidadeRepo.save(op);
                System.out.println("Oportunidade " + titulo + " cadastrada e populada com sucesso!");
            } catch (Exception ex) {
                System.err.println("Erro ao popular oportunidade na pasta " + opDir.getName() + ": " + ex.getMessage());
            }
        }
    }
}
