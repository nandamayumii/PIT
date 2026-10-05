package br.edu.catalogo.service;

import java.util.List;

/**
 * Exceção lançada quando os dados enviados pelo usuário são inválidos.
 * Guarda TODAS as mensagens de erro para exibi-las de uma vez na tela.
 */
public class ValidacaoException extends Exception {

    private static final long serialVersionUID = 1L;

    private final List<String> erros;

    public ValidacaoException(List<String> erros) {
        super(String.join("; ", erros));
        this.erros = List.copyOf(erros);
    }

    public List<String> getErros() {
        return erros;
    }
}
