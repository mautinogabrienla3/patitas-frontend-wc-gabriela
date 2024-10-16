package com.edu.cibertec.controller;

import com.edu.cibertec.dto.LoginRequestDTO;
import com.edu.cibertec.dto.LoginResponseDTO;
import com.edu.cibertec.dto.LogoutRequestDTO;
import com.edu.cibertec.dto.LogoutResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/autenticacion")
@CrossOrigin(origins = "http://localhost:5173")
public class AutenticacionControllerAsync {

    @Autowired
    private WebClient webClientAutenticacion;

    @PostMapping("/login-async")
    public Mono<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO) {

        if (loginRequestDTO.tipoDocumento() == null || loginRequestDTO.tipoDocumento().trim().isEmpty() ||
                loginRequestDTO.numeroDocumento() == null || loginRequestDTO.numeroDocumento().trim().isEmpty() ||
                loginRequestDTO.password() == null || loginRequestDTO.password().trim().isEmpty()){
            return Mono.just(new LoginResponseDTO(
                    "01",
                    "Error: Debes completar correctamente sus credenciales",
                    "", "", "", ""));
        }

        try {
            return webClientAutenticacion.post()
                    .uri("/login")
                    .body(Mono.just(loginRequestDTO), LoginRequestDTO.class)
                    .retrieve()
                    .bodyToMono(LoginResponseDTO.class)
                    .flatMap(response -> {
                        if(response.codigo().equals("00")){
                            return Mono.just(new LoginResponseDTO(
                                    "00",
                                    "",
                                    response.nombreUsuario(),
                                    response.correoUsuario(),
                                    response.tipoDocumento(),
                                    response.numeroDocumento()));
                        } else {
                            return Mono.just(new LoginResponseDTO(
                                    response.codigo(),
                                    response.mensaje(),
                                    "", "", "", ""));
                        }
                    });
        } catch(Exception e) {
            return Mono.just(new LoginResponseDTO(
                    "99",
                    "Error: Ocurrió un problema en la autenticación : ".concat(e.getMessage()),
                    "", "", "", ""));
        }

    }

    @PostMapping("/logout-async")
    public Mono<LogoutResponseDTO> logout(@RequestBody LogoutRequestDTO logoutRequestDTO) {
        try {
            return webClientAutenticacion.post()
                    .uri("/logout")
                    .body(Mono.just(logoutRequestDTO), LogoutRequestDTO.class)
                    .retrieve()
                    .bodyToMono(LogoutResponseDTO.class)
                    .flatMap(response ->
                            Mono.just(new LogoutResponseDTO(
                                    response.resultado(),
                                    response.fecha(),
                                    response.mensajeError()))
                    );
        } catch(Exception e) {
            return Mono.just(new LogoutResponseDTO(
                    false,
                    null,
                    "Error al cerrar sesión: ".concat(e.getMessage())));
        }
    }
}
