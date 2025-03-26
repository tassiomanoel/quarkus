package br.com.curso.quarkus.controller;

import br.com.curso.quarkus.domain.Agencia;
import br.com.curso.quarkus.service.http.AgenciaService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.UriInfo;
import org.jboss.resteasy.reactive.RestResponse;

@Path("/agencias")
public class AgenciaController {

    private AgenciaService agenciaService;

    AgenciaController(AgenciaService agenciaService) {
        this.agenciaService = agenciaService;
    }

    @POST
    public RestResponse<Void> cadastrar(Agencia agencia, @Context UriInfo uriInfo) {
        agenciaService.cadastrar(agencia);
        return RestResponse.created(uriInfo.getAbsolutePath());
    }

    @GET
    @Path("/{id}")
    public RestResponse<Agencia> buscarPorId(Integer id) {
        return RestResponse.ok(agenciaService.buscarPorId(id));
    }

    @DELETE
    @Path("{id}")
    public RestResponse<Void> deletar(Integer id) {
        agenciaService.deletar(id);
        return RestResponse.ok();
    }

    @PUT
    @Path("{id}")
    public RestResponse<Void> alterar(Agencia agencia) {
        agenciaService.alterar(agencia);
        return RestResponse.ok();
    }
}
