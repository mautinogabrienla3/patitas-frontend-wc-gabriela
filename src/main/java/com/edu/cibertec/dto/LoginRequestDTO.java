package com.edu.cibertec.dto;

public record LoginRequestDTO(
        String tipoDocumento,
        String numeroDocumento,
        String password) { }