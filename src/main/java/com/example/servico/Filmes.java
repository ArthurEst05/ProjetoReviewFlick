package com.example.servico;

import java.util.List;

public class Filmes {

    private ApiServico apiServico;

    public Filmes() {
        this.apiServico = new ApiServico();
    }

    public List<EnderecoDto> buscarFilmes(String nomeFilme) throws Exception {
    return apiServico.getListaEnderecoDto(nomeFilme);
}

public EnderecoDto buscarDetalhesFilme(String nomeFilme) throws Exception {
    return apiServico.getEnderecoDto(nomeFilme);
}

}
