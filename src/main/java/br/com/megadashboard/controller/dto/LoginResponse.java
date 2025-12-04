package br.com.megadashboard.controller.dto;

public record LoginResponse(
        String token,
        UsuarioResponse usuario
) {}
