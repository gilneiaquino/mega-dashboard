package br.com.megadashboard.controller.dto;

public record LoginRequest(
        String login,
        String senha
) {}

