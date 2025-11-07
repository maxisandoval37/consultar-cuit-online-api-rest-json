package dev.ar.maxisandoval.consultarcuitonlineapirestjson.model;

public record CuitResultDto(
        String query,
        String nombre,
        String cuit,
        String tipoPersona,
        String sexo
) {}