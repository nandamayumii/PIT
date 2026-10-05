package br.edu.catalogo.service;

import br.edu.catalogo.dao.ItemMidiaDAO;
import br.edu.catalogo.model.ItemMidia;

import java.sql.SQLException;
import java.time.Year;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Camada de serviço: concentra as regras de negócio e a validação dos dados.
 * O Servlet fala com o Service; o Service fala com o DAO.
 */
public class ItemMidiaService {

    private final ItemMidiaDAO dao;

    public ItemMidiaService(ItemMidiaDAO dao) {
        this.dao = dao;
    }

    /**
     * Valida os textos recebidos do formulário e converte para um ItemMidia.
     * Não acessa o banco, por isso pode ser testado de forma isolada.
     *
     * @throws ValidacaoException com todas as mensagens de erro encontradas
     */
    public ItemMidia criarItem(String id, String titulo, String autorDiretor, String ano,
                               String genero, String sinopse, String tipoMidia,
                               String nota, String capaUrl) throws ValidacaoException {
        List<String> erros = new ArrayList<>();

        titulo = limpar(titulo);
        autorDiretor = limpar(autorDiretor);
        genero = limpar(genero);
        sinopse = limpar(sinopse);
        tipoMidia = limpar(tipoMidia);
        capaUrl = limpar(capaUrl);

        // Campos obrigatórios e comprimentos máximos (espelham o banco de dados)
        if (titulo.isEmpty()) {
            erros.add("O título é obrigatório.");
        } else if (titulo.length() > 255) {
            erros.add("O título deve ter no máximo 255 caracteres.");
        }
        if (autorDiretor.length() > 255) {
            erros.add("Autor/Diretor deve ter no máximo 255 caracteres.");
        }
        if (genero.length() > 100) {
            erros.add("O gênero deve ter no máximo 100 caracteres.");
        }
        if (sinopse.length() > 5000) {
            erros.add("A sinopse deve ter no máximo 5000 caracteres.");
        }
        if (!Arrays.asList(ItemMidia.TIPOS).contains(tipoMidia)) {
            erros.add("Selecione um tipo válido: Livro, Filme ou Série.");
        }
        if (!capaUrl.isEmpty()
                && !(capaUrl.startsWith("http://") || capaUrl.startsWith("https://"))) {
            erros.add("O link da capa deve começar com http:// ou https://.");
        } else if (capaUrl.length() > 500) {
            erros.add("O link da capa deve ter no máximo 500 caracteres.");
        }

        // Campos numéricos: o ano pode chegar como texto qualquer
        Integer anoNumero = null;
        if (!limpar(ano).isEmpty()) {
            try {
                anoNumero = Integer.valueOf(limpar(ano));
                int limite = Year.now().getValue() + 5;
                if (anoNumero < 1000 || anoNumero > limite) {
                    erros.add("O ano deve estar entre 1000 e " + limite + ".");
                }
            } catch (NumberFormatException e) {
                erros.add("O ano deve ser um número inteiro (ex.: 1999).");
            }
        }
        Integer notaNumero = null;
        if (!limpar(nota).isEmpty()) {
            try {
                notaNumero = Integer.valueOf(limpar(nota));
                if (notaNumero < 0 || notaNumero > 5) {
                    erros.add("A nota deve estar entre 0 e 5.");
                }
            } catch (NumberFormatException e) {
                erros.add("A nota deve ser um número inteiro de 0 a 5.");
            }
        }
        Integer idNumero = null;
        if (!limpar(id).isEmpty()) {
            try {
                idNumero = Integer.valueOf(limpar(id));
            } catch (NumberFormatException e) {
                erros.add("Identificador inválido.");
            }
        }

        if (!erros.isEmpty()) {
            throw new ValidacaoException(erros);
        }
        return new ItemMidia(idNumero, titulo, autorDiretor, anoNumero, genero,
                sinopse, tipoMidia, notaNumero, capaUrl);
    }

    /** Grava o item: insere se não tiver id, atualiza se já tiver. */
    public void salvar(ItemMidia item) throws SQLException {
        if (item.getId() == null) {
            dao.inserir(item);
        } else {
            dao.atualizar(item);
        }
    }

    public boolean excluir(int id) throws SQLException {
        return dao.excluir(id);
    }

    public ItemMidia buscarPorId(int id) throws SQLException {
        return dao.buscarPorId(id);
    }

    public List<ItemMidia> buscar(String termo, String genero) throws SQLException {
        return dao.buscar(termo, genero);
    }

    public List<String> listarGeneros() throws SQLException {
        return dao.listarGeneros();
    }

    /** Remove espaços das pontas e trata null como texto vazio. */
    private String limpar(String texto) {
        return texto == null ? "" : texto.trim();
    }
}
