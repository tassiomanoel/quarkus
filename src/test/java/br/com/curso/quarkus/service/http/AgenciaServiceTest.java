package br.com.curso.quarkus.service.http;

import br.com.curso.quarkus.domain.Agencia;
import br.com.curso.quarkus.exceptions.AgenciaNaoAtivaOuNaoEncontradaException;
import br.com.curso.quarkus.repository.AgenciaRepository;
import br.com.curso.quarkus.service.AgenciaService;
import br.com.curso.quarkus.utils.AgenciaFixture;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

@QuarkusTest
public class AgenciaServiceTest {

    @InjectMock
    private AgenciaRepository agenciaRepository;

    @InjectMock
    @RestClient
    private SituacaoCadastralHttpService situacaoCadastralHttpService;

    @Inject
    private AgenciaService agenciaService;


    @Test
    public void deveLancarExcecaoQuandoClientRetornarNull() {
        Agencia agencia = AgenciaFixture.criarAgencia();
        Mockito.when(situacaoCadastralHttpService.buscarPorCnpj(agencia.getCnpj())).thenReturn(null);

        Assertions.assertThrows(AgenciaNaoAtivaOuNaoEncontradaException.class,
                () -> agenciaService.cadastrar(agencia));
        Mockito.verify(agenciaRepository, Mockito.never()).persist(agencia);
    }

    @Test
    public void deveCadastrarQuandoClientRetornarSituacaoCadastralAtivo() {
        Agencia agencia = AgenciaFixture.criarAgencia();
        Mockito.when(situacaoCadastralHttpService.buscarPorCnpj(agencia.getCnpj()))
                .thenReturn(AgenciaFixture.criarAgenciaHttp(SituacaoCadastral.ATIVO));

        agenciaService.cadastrar(agencia);
        Mockito.verify(agenciaRepository, Mockito.times(1)).persist(agencia);
    }

    @Test
    public void deveLancarExcecaoQuandoClientRetornarSituacaoCadastralInativo() {
        Agencia agencia = AgenciaFixture.criarAgencia();
        Mockito.when(situacaoCadastralHttpService.buscarPorCnpj(agencia.getCnpj()))
                .thenReturn(AgenciaFixture.criarAgenciaHttp(SituacaoCadastral.INATIVO));

        Assertions.assertThrows(AgenciaNaoAtivaOuNaoEncontradaException.class,
                () -> agenciaService.cadastrar(agencia));
        Mockito.verify(agenciaRepository, Mockito.never()).persist(agencia);
    }
}