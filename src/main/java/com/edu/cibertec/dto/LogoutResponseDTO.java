package com.edu.cibertec.dto;

import java.util.Date;

public record LogoutResponseDTO(
        Boolean resultado,
        Date fecha,
        String mensajeError) { }
