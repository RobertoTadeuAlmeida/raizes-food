package br.com.raizesfood.controller;

import br.com.raizesfood.dto.CadastroUsuarioRequest;
import br.com.raizesfood.dto.LoginRequest;
import br.com.raizesfood.dto.LoginResponse;
import br.com.raizesfood.model.entity.Usuario;
import br.com.raizesfood.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/cadastro")
    public ResponseEntity<Void> cadastrar(
            @RequestBody CadastroUsuarioRequest request
    ) {
        authService.cadastrarCliente(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody LoginRequest request
    ) {
        return ResponseEntity.ok(
                authService.login(request)
        );
    }
}