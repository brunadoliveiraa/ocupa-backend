package com.ocupa.ocupa;

import com.ocupa.ocupa.model.User;
import com.ocupa.ocupa.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class OcupaBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(OcupaBackendApplication.class, args);
    }

    @Bean
    public CommandLineRunner initDatabase(
            UserRepository userRepo, 
            com.ocupa.ocupa.repository.ArtistaRepository artistaRepo,
            com.ocupa.ocupa.repository.PortfolioRepository portfolioRepo,
            org.springframework.jdbc.core.JdbcTemplate jdbcTemplate,
            javax.sql.DataSource dataSource) {
        return args -> {
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

            try {
                jdbcTemplate.execute("ALTER TABLE artista MODIFY COLUMN foto_url LONGTEXT");
                jdbcTemplate.execute("ALTER TABLE portfolio_media MODIFY COLUMN url LONGTEXT");
                System.out.println("Migracao de colunas executada com sucesso!");
            } catch (Exception e) {
                System.err.println("Erro ao executar migracao de colunas: " + e.getMessage());
            }

            // Limpar dados de seed anteriores (para evitar duplicidade ou cadastros parciais corrompidos)
            try {
                jdbcTemplate.execute("DELETE FROM portfolio_media WHERE portfolio_id IN (SELECT id FROM portfolio WHERE artista_id IN (SELECT id FROM artista WHERE nome IN ('Elias Mast', 'Igor Izy', 'Mazur 13', 'Ébano Bronca')))");
                jdbcTemplate.execute("DELETE FROM portfolio WHERE artista_id IN (SELECT id FROM artista WHERE nome IN ('Elias Mast', 'Igor Izy', 'Mazur 13', 'Ébano Bronca'))");
                jdbcTemplate.execute("DELETE FROM users WHERE email IN ('elias.mast@gmail.com', 'igor.izy@gmail.com', 'mazur.13@gmail.com', 'ebano.bronca@gmail.com')");
                jdbcTemplate.execute("DELETE FROM artista WHERE nome IN ('Elias Mast', 'Igor Izy', 'Mazur 13', 'Ébano Bronca')");
                System.out.println("Limpeza de registros antigos executada com sucesso!");
            } catch (Exception e) {
                System.err.println("Erro ao limpar registros antigos: " + e.getMessage());
            }

            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            
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
            java.io.File popularDir = new java.io.File("Popular artistas");
            if (popularDir.exists() && popularDir.isDirectory()) {
                System.out.println("Encontrada pasta 'Popular artistas'. Iniciando populacao de dados...");
                java.io.File[] artistDirs = popularDir.listFiles(java.io.File::isDirectory);
                if (artistDirs != null) {
                    for (java.io.File artistDir : artistDirs) {
                        try {
                            java.io.File txtFile = new java.io.File(artistDir, "Portfolio.txt");
                            if (!txtFile.exists()) continue;
                            
                            java.util.Map<String, String> data = new java.util.HashMap<>();
                            for (String line : java.nio.file.Files.readAllLines(txtFile.toPath(), java.nio.charset.StandardCharsets.UTF_8)) {
                                line = line.trim();
                                if (line.isEmpty()) continue;
                                int colIdx = line.indexOf(":");
                                if (colIdx != -1) {
                                    String rawKey = line.substring(0, colIdx);
                                    String normalizedKey = java.text.Normalizer.normalize(rawKey, java.text.Normalizer.Form.NFD)
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
                            java.io.File[] files = artistDir.listFiles();
                            if (files != null) {
                                for (java.io.File f : files) {
                                    if (f.isFile() && f.getName().toLowerCase().startsWith("foto de perfil do artista")) {
                                        fotoUrl = getBase64DataUrl(f);
                                        break;
                                    }
                                }
                            }
                            
                            // 1. Criar Artista
                            com.ocupa.ocupa.model.Artista artista = new com.ocupa.ocupa.model.Artista();
                            artista.setNome(nome);
                            artista.setCategoria(categoria);
                            artista.setCidade(cidade);
                            artista.setFotoUrl(fotoUrl);
                            artista = artistaRepo.save(artista);
                            
                            // 2. Criar Usuario
                            User user = new User();
                            user.setNome(nome);
                            user.setEmail(email);
                            user.setSenha(encoder.encode(senha != null ? senha : "123"));
                            user.setRole("ARTISTA");
                            user.setArtistaId(artista.getId());
                            userRepo.save(user);
                            
                            // 3. Criar Portfolio
                            com.ocupa.ocupa.model.Portfolio portfolio = new com.ocupa.ocupa.model.Portfolio();
                            portfolio.setArtista(artista);
                            portfolio.setHeadline(headline);
                            portfolio.setAbout(about);
                            portfolio.setContacts(contato != null ? contato : email);
                            
                            // 4. Carregar Midias do Portfolio
                            java.io.File mediaDir = new java.io.File(artistDir, "Mídias do Portfólio");
                            if (mediaDir.exists() && mediaDir.isDirectory()) {
                                java.io.File[] mediaFiles = mediaDir.listFiles(f -> f.isFile() && (f.getName().toLowerCase().endsWith(".png") || f.getName().toLowerCase().endsWith(".jpg") || f.getName().toLowerCase().endsWith(".jpeg")));
                                if (mediaFiles != null) {
                                    for (java.io.File mf : mediaFiles) {
                                        String mBase64 = getBase64DataUrl(mf);
                                        if (mBase64 != null) {
                                            com.ocupa.ocupa.model.PortfolioMedia pm = new com.ocupa.ocupa.model.PortfolioMedia();
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
                            ex.printStackTrace();
                        }
                    }
                }
            }
        };
    }

    private static String getBase64DataUrl(java.io.File file) {
        try {
            byte[] bytes = java.nio.file.Files.readAllBytes(file.toPath());
            String base64 = java.util.Base64.getEncoder().encodeToString(bytes);
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
