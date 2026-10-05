package br.edu.catalogo.service;

import br.edu.catalogo.dao.ItemMidiaDAO;
import br.edu.catalogo.model.ItemMidia;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/** Testes unitários das regras de validação (não dependem do banco). */
class ItemMidiaServiceTest {

    private ItemMidiaService servico;

    @BeforeEach
    void preparar() {
        servico = new ItemMidiaService(new ItemMidiaDAO());
    }

    @Test
    void aceitaDadosValidos() throws ValidacaoException {
        ItemMidia item = servico.criarItem("", "Dom Casmurro", "Machado de Assis", "1899",
                "Romance", "Sinopse", "Livro", "5", "");
        assertEquals("Dom Casmurro", item.getTitulo());
        assertEquals(1899, item.getAnoLancamento());
        assertNull(item.getId());
    }

    @Test
    void rejeitaTituloVazio() {
        ValidacaoException e = assertThrows(ValidacaoException.class, () ->
                servico.criarItem("", "  ", "Autor", "2000", "Drama", "", "Filme", "", ""));
        assertTrue(e.getErros().contains("O título é obrigatório."));
    }

    @Test
    void rejeitaAnoQueNaoEhNumero() {
        ValidacaoException e = assertThrows(ValidacaoException.class, () ->
                servico.criarItem("", "Titulo", "Autor", "abcd", "Drama", "", "Livro", "", ""));
        assertTrue(e.getErros().stream().anyMatch(m -> m.contains("ano deve ser um número")));
    }

    @Test
    void rejeitaTipoInvalidoENotaForaDoIntervalo() {
        ValidacaoException e = assertThrows(ValidacaoException.class, () ->
                servico.criarItem("", "Titulo", "", "", "", "", "Jogo", "9", ""));
        assertEquals(2, e.getErros().size());
    }

    @Test
    void rejeitaLinkDeCapaSemHttp() {
        assertThrows(ValidacaoException.class, () ->
                servico.criarItem("", "Titulo", "", "", "", "", "Livro", "", "javascript:alert(1)"));
    }
}
