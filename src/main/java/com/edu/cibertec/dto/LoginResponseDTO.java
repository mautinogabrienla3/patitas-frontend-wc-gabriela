package com.edu.cibertec.dto;

public record LoginResponseDTO(
        String codigo,
        String mensaje,
        String nombreUsuario,
        String correoUsuario,
        String tipoDocumento,
        String numeroDocumento) { }
