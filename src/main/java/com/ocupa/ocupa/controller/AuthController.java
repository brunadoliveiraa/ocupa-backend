package com.ocupa.ocupa.controller;

import com.ocupa.ocupa.model.Artista;
import com.ocupa.ocupa.model.User;
import com.ocupa.ocupa.repository.ArtistaRepository;
import com.ocupa.ocupa.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    private final UserRepository userRepository;
    private final ArtistaRepository artistaRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthController(UserRepository userRepository, ArtistaRepository artistaRepository) {
        this.userRepository = userRepository;
        this.artistaRepository = artistaRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (request.getEmail() == null || request.getSenha() == null || request.getNome() == null || request.getRole() == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Nome, email, senha e papel são obrigatórios"));
        }

        if (!request.getRole().equals("ARTISTA") && !request.getRole().equals("EMPREENDEDOR")) {
            return ResponseEntity.badRequest().body(Map.of("error", "Papel inválido. Use ARTISTA ou EMPREENDEDOR"));
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", "Email já cadastrado"));
        }

        User user = new User();
        user.setNome(request.getNome());
        user.setEmail(request.getEmail());
        user.setRole(request.getRole());
        user.setSenha(passwordEncoder.encode(request.getSenha()));

        if (request.getRole().equals("ARTISTA")) {
            var artista = new Artista();
            artista.setNome(request.getNome());
            artista.setCategoria(request.getCategoria());
            artista = artistaRepository.save(artista);
            user.setArtistaId(artista.getId());
        }

        userRepository.save(user);

        return ResponseEntity.ok(Map.of(
                "id", user.getId(),
                "nome", user.getNome(),
                "email", user.getEmail(),
                "role", user.getRole(),
                "artistaId", user.getArtistaId()
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        if (request.getEmail() == null || request.getSenha() == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email e senha são obrigatórios"));
        }

        return userRepository.findByEmail(request.getEmail())
                .map(user -> {
                    if (passwordEncoder.matches(request.getSenha(), user.getSenha())) {
                        return ResponseEntity.ok(Map.of(
                                "id", user.getId(),
                                "nome", user.getNome(),
                                "email", user.getEmail(),
                                "role", user.getRole(),
                                "artistaId", user.getArtistaId()
                        ));
                    }
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Credenciais inválidas"));
                })
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Credenciais inválidas")));
    }

    public static class RegisterRequest {
        private String nome;
        private String email;
        private String senha;
        private String role;
        private String categoria;

        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getSenha() {
            return senha;
        }

        public void setSenha(String senha) {
            this.senha = senha;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getCategoria() {
            return categoria;
        }

        public void setCategoria(String categoria) {
            this.categoria = categoria;
        }
    }

    public static class LoginRequest {
        private String email;
        private String senha;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getSenha() {
            return senha;
        }

        public void setSenha(String senha) {
            this.senha = senha;
        }
    }
}
