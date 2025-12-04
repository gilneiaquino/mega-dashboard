package br.com.megadashboard.controller.dto;

import br.com.megadashboard.model.Perfil;

public record UsuarioResponse(
        Long id,
        String nome,
        String login,
        Perfil perfil,
        String tenant
) {}
