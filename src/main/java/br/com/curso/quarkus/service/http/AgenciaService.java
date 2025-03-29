package br.com.curso.quarkus.service.http;

import br.com.curso.quarkus.domain.Agencia;
import br.com.curso.quarkus.exceptions.AgenciaNaoAtivaOuNaoEncontradaException;
import br.com.curso.quarkus.repository.AgenciaRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@ApplicationScoped
public class AgenciaService {

    private final AgenciaRepository agenciaRepository;

    @RestClient
    private SituacaoCadastralHttpService situacaoCadastralHttpService;

    AgenciaService(AgenciaRepository agenciaRepository) {
        this.agenciaRepository = agenciaRepository;
    }

    @Transactional
    public void cadastrar(Agencia agencia) {
        AgenciaHttp agenciaRetorno = situacaoCadastralHttpService.buscarPorCnpj(agencia.getCnpj());
        if(agenciaRetorno != null && agenciaRetorno.getSituacaoCadastral().equals(SituacaoCadastral.ATIVO)) {
            agenciaRepository.persist(agencia);
        } else {
            throw new AgenciaNaoAtivaOuNaoEncontradaException();
        }
    }

    public Agencia buscarPorId(Long id) {
        return agenciaRepository.findById(id);
    }

    @Transactional
    public void deletar(Long id) {
        agenciaRepository.deleteById(id);
    }

    @Transactional
    public void alterar(Agencia agencia) {
        agenciaRepository.update("nome = ?1, razaoSocial = ?2, cnpj = ?3 where id = ?4",
                agencia.getNome(), agencia.getRazaoSocial(), agencia.getCnpj(), agencia.getId());
    }
}
