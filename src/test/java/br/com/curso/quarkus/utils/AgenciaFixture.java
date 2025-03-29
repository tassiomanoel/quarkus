package br.com.curso.quarkus.utils;

import br.com.curso.quarkus.domain.Agencia;
import br.com.curso.quarkus.domain.Endereco;
import br.com.curso.quarkus.domain.http.AgenciaHttp;
import br.com.curso.quarkus.service.http.SituacaoCadastral;

public class AgenciaFixture {

    public static Agencia criarAgencia() {
        Endereco endereco = new Endereco(1,"rua alvorada","casa","5B", 5);
        return new Agencia(1, "caixa", "caixa tem", "123", endereco);
    }

    public static AgenciaHttp criarAgenciaHttp(SituacaoCadastral enumSituacao) {
        return new AgenciaHttp(1, "caixa", "caixa tem", "123", enumSituacao);
    }
}
