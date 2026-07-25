package com.ocupa.ocupa.controller;

import com.ocupa.ocupa.dto.LoginRequest;
import com.ocupa.ocupa.dto.RegisterRequest;
import com.ocupa.ocupa.model.Artista;
import com.ocupa.ocupa.model.User;
import com.ocupa.ocupa.service.ArtistaService;
import com.ocupa.ocupa.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final ArtistaService artistaService;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (request.getEmail() == null || request.getSenha() == null || request.getNome() == null || request.getRole() == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Nome, email, senha e papel são obrigatórios"));
        }

        if (!request.getRole().equals("ARTISTA") && !request.getRole().equals("EMPREENDEDOR")) {
            return ResponseEntity.badRequest().body(Map.of("error", "Papel inválido. Use ARTISTA ou EMPREENDEDOR"));
        }

        if (userService.existsByEmail(request.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", "Email já cadastrado"));
        }

        User user = new User();
        user.setNome(request.getNome());
        user.setEmail(request.getEmail());
        user.setRole(request.getRole());
        user.setSenha(passwordEncoder.encode(request.getSenha()));

        if (request.getRole().equals("ARTISTA")) {
            Artista artista = new Artista();
            artista.setNome(request.getNome());
            artista.setCategoria(request.getCategoria());
            artista = artistaService.save(artista);
            user.setArtistaId(artista.getId());
        }

        userService.save(user);

        Map<String, Object> registerResponse = new HashMap<>();
        registerResponse.put("id", user.getId());
        registerResponse.put("nome", user.getNome());
        registerResponse.put("email", user.getEmail());
        registerResponse.put("role", user.getRole());
        registerResponse.put("artistaId", user.getArtistaId());

        return ResponseEntity.ok(registerResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        if (request.getEmail() == null || request.getSenha() == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email e senha são obrigatórios"));
        }

        return userService.findByEmail(request.getEmail())
                .map(user -> {
                    if (passwordEncoder.matches(request.getSenha(), user.getSenha())) {
                        Map<String, Object> loginResponse = new HashMap<>();
                        loginResponse.put("id", user.getId());
                        loginResponse.put("nome", user.getNome());
                        loginResponse.put("email", user.getEmail());
                        loginResponse.put("role", user.getRole());
                        loginResponse.put("artistaId", user.getArtistaId());
                        return ResponseEntity.ok(loginResponse);
                    }
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Credenciais inválidas"));
                })
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Credenciais inválidas")));
    }
}
