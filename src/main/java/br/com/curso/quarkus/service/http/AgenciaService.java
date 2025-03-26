package br.com.curso.quarkus.service.http;

import br.com.curso.quarkus.domain.Agencia;
import br.com.curso.quarkus.exceptions.AgenciaNaoAtivaOuNaoEncontradaException;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class AgenciaService {

    @RestClient
    private SituacaoCadastralHttpService situacaoCadastralHttpService;

    private List<Agencia> agencias = new ArrayList<>();

    public void cadastrar(Agencia agencia) {
        AgenciaHttp agenciaRetorno = situacaoCadastralHttpService.buscarPorCnpj(agencia.getCnpj());
        if(agenciaRetorno.getSituacaoCadastral().equals(StituacaoCadastral.ATIVO)) {
            agencias.add(agencia);
        } else {
            throw new AgenciaNaoAtivaOuNaoEncontradaException();
        }
    }
}
