package br.com.curso.quarkus.service.http;

import br.com.curso.quarkus.domain.Agencia;
import br.com.curso.quarkus.domain.Endereco;
import br.com.curso.quarkus.domain.http.AgenciaHttp;
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
    public void deveNaoCadastrarQuandoClientRetornarNull() {
        Agencia agencia = AgenciaFixture.criarAgencia();
        Mockito.when(situacaoCadastralHttpService.buscarPorCnpj("123")).thenReturn(null);
        Assertions.assertThrows(AgenciaNaoAtivaOuNaoEncontradaException.class, () -> agenciaService.cadastrar(agencia));
        Mockito.verify(agenciaRepository, Mockito.never()).persist(agencia);
    }

    @Test
    public void deveCadastrarQuandoClientRetornarSituacaoCadastralAtivo() {
        Agencia agencia = AgenciaFixture.criarAgencia();
        Mockito.when(situacaoCadastralHttpService.buscarPorCnpj("123")).thenReturn(AgenciaFixture.criarAgenciaHttp(SituacaoCadastral.INATIVO));
        agenciaService.cadastrar(agencia);
        Mockito.verify(agenciaRepository).persist(agencia);
    }

    @Test
    public void deveCadastrarQuandoClientRetornarSituacaoCadastralInativo() {
        Agencia agencia = AgenciaFixture.criarAgencia();
        Mockito.when(situacaoCadastralHttpService.buscarPorCnpj("123")).thenReturn(AgenciaFixture.criarAgenciaHttp(SituacaoCadastral.INATIVO));
        agenciaService.cadastrar(agencia);
        Mockito.verify(agenciaRepository, Mockito.never()).persist(agencia);
    }



}