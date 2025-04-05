package br.com.curso.quarkus.service;

import br.com.curso.quarkus.domain.Agencia;
import br.com.curso.quarkus.exceptions.AgenciaNaoAtivaOuNaoEncontradaException;
import br.com.curso.quarkus.repository.AgenciaRepository;
import br.com.curso.quarkus.domain.http.AgenciaHttp;
import br.com.curso.quarkus.service.http.SituacaoCadastral;
import br.com.curso.quarkus.service.http.SituacaoCadastralHttpService;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.List;

@ApplicationScoped
public class AgenciaService {

    @RestClient
    private SituacaoCadastralHttpService situacaoCadastralHttpService;

    private final AgenciaRepository agenciaRepository;
    private final MeterRegistry meterRegistry;

    AgenciaService(AgenciaRepository agenciaRepository, MeterRegistry meterRegistry) {
        this.agenciaRepository = agenciaRepository;
        this.meterRegistry = meterRegistry;
    }

    @Transactional
    public void cadastrar(Agencia agencia) {
        try {

            Timer timer = this.meterRegistry.timer("cadastrar_agencia_timer");
            timer.record(() -> {
                AgenciaHttp agenciaRetorno = situacaoCadastralHttpService.buscarPorCnpj(agencia.getCnpj());

                if(agenciaRetorno != null && agenciaRetorno.getSituacaoCadastral().equals(SituacaoCadastral.ATIVO)) {
                    agenciaRepository.persist(agencia);
                    Log.info("Agência incluida com sucesso. ");
                    meterRegistry.counter("agencia_adicionada_counter").increment();

                } else if(agenciaRetorno != null && agenciaRetorno.getSituacaoCadastral().equals(SituacaoCadastral.INATIVO)) {
                    Log.info("Agência encontrada está inativa.");
                    throw new AgenciaNaoAtivaOuNaoEncontradaException();

                } else {

                    Log.info("Agência não encontrada.");
                    meterRegistry.counter("agencia_nao_adicionada_counter").increment();
                    throw new AgenciaNaoAtivaOuNaoEncontradaException();
                }
            });
        } catch(Exception e) {
            Log.info("Erro exception " + e.getLocalizedMessage() + " Exception total " + e);
            throw new AgenciaNaoAtivaOuNaoEncontradaException();
        }
    }

    public Agencia buscarPorId(Long id) {
        return agenciaRepository.findById(id);
    }

    public List<Agencia> buscarTodos() {
        return agenciaRepository.findAll().stream().toList();
    }

    @Transactional
    public void deletar(Long id) {
        agenciaRepository.deleteById(id);
        Log.info("Agência excluida com sucesso.");
    }

    @Transactional
    public void alterar(Agencia agencia) {
        agenciaRepository.update("nome = ?1, razaoSocial = ?2, cnpj = ?3 where id = ?4",
                agencia.getNome(), agencia.getRazaoSocial(), agencia.getCnpj(), agencia.getId());
        Log.info("Agência atualizada com sucesso.");
    }
}
