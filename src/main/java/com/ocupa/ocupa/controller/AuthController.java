package com.ocupa.ocupa.controller;

import com.ocupa.ocupa.config.JwtUtil;
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
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (request.getEmail() == null || request.getSenha() == null || request.getNome() == null || request.getRole() == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Nome, email, senha e papel são obrigatórios"));
        }

        if (!request.getRole().equals("ARTISTA") && !request.getRole().equals("COLABORADOR")) {
            return ResponseEntity.badRequest().body(Map.of("error", "Papel inválido. Use ARTISTA ou COLABORADOR"));
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
            artista.setStatus("PENDENTE");
            artista = artistaService.save(artista);
            user.setArtistaId(artista.getId());
        }

        userService.save(user);
        
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole(), user.getId(), user.getArtistaId());

        Map<String, Object> userData = new HashMap<>();
        userData.put("id", user.getId());
        userData.put("nome", user.getNome());
        userData.put("email", user.getEmail());
        userData.put("role", user.getRole());
        userData.put("artistaId", user.getArtistaId());
        
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("user", userData);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        if (request.getEmail() == null || request.getSenha() == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email e senha são obrigatórios"));
        }

        return userService.findByEmail(request.getEmail())
                .map(user -> {
                    if (passwordEncoder.matches(request.getSenha(), user.getSenha())) {
                        String token = jwtUtil.generateToken(user.getEmail(), user.getRole(), user.getId(), user.getArtistaId());
                        
                        Map<String, Object> userData = new HashMap<>();
                        userData.put("id", user.getId());
                        userData.put("nome", user.getNome());
                        userData.put("email", user.getEmail());
                        userData.put("role", user.getRole());
                        userData.put("artistaId", user.getArtistaId());
                        
                        Map<String, Object> response = new HashMap<>();
                        response.put("token", token);
                        response.put("user", userData);
                        
                        return ResponseEntity.ok(response);
                    }
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Credenciais inválidas"));
                })
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Credenciais inválidas")));
    }
}
