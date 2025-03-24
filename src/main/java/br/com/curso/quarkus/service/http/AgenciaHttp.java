package br.com.curso.quarkus.service.http;

public class AgenciaHttp {

    private String nome;
    private String razaoSocial;
    private String cnpj;
    private StituacaoCadastral situacaoCadastral;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public StituacaoCadastral getSituacaoCadastral() {
        return situacaoCadastral;
    }

    public void setSituacaoCadastral(StituacaoCadastral situacaoCadastral) {
        this.situacaoCadastral = situacaoCadastral;
    }
}
