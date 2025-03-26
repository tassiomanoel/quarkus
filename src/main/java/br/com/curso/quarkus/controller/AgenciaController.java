package br.com.curso.quarkus.controller;

import br.com.curso.quarkus.domain.Agencia;
import br.com.curso.quarkus.service.http.AgenciaService;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.UriInfo;
import org.jboss.resteasy.reactive.RestResponse;

@Path("/agencias")
public class AgenciaController {

    private AgenciaService agenciaService;

    AgenciaController(AgenciaService agenciaService) {
        this.agenciaService = agenciaService;
    }

    public RestResponse<Void> cadastrar(Agencia agencia, @Context UriInfo uriInfo) {
        agenciaService.cadastrar(agencia);
        return RestResponse.created(uriInfo.getAbsolutePath());
    }
}
