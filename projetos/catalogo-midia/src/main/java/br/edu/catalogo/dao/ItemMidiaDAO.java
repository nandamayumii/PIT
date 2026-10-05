package br.edu.catalogo.dao;

import br.edu.catalogo.model.ItemMidia;
import br.edu.catalogo.util.ConexaoBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO (Data Access Object): único ponto de acesso ao banco para a tabela
 * item_midia. TODAS as consultas usam PreparedStatement, o que impede
 * SQL Injection, pois a entrada do usuário é tratada como dado e nunca
 * como parte do comando SQL.
 */
public class ItemMidiaDAO {

    private static final String COLUNAS =
            "id, titulo, autor_diretor, ano_lancamento, genero, sinopse, tipo_midia, nota, capa_url";

    /**
     * Insere um novo item e preenche o id gerado no próprio objeto.
     *
     * @param item item a ser gravado
     * @throws SQLException em caso de erro de banco
     */
    public void inserir(ItemMidia item) throws SQLException {
        String sql = "INSERT INTO item_midia "
                + "(titulo, autor_diretor, ano_lancamento, genero, sinopse, tipo_midia, nota, capa_url) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preencherParametros(stmt, item);
            stmt.executeUpdate();
            try (ResultSet chaves = stmt.getGeneratedKeys()) {
                if (chaves.next()) {
                    item.setId(chaves.getInt(1));
                }
            }
        }
    }

    /**
     * Atualiza todos os campos de um item existente.
     *
     * @param item item com id preenchido
     * @return true se alguma linha foi alterada
     * @throws SQLException em caso de erro de banco
     */
    public boolean atualizar(ItemMidia item) throws SQLException {
        String sql = "UPDATE item_midia SET titulo = ?, autor_diretor = ?, ano_lancamento = ?, "
                + "genero = ?, sinopse = ?, tipo_midia = ?, nota = ?, capa_url = ? WHERE id = ?";
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            preencherParametros(stmt, item);
            stmt.setInt(9, item.getId());
            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * Exclui um item pelo id.
     *
     * @param id identificador do item
     * @return true se um item foi removido
     * @throws SQLException em caso de erro de banco
     */
    public boolean excluir(int id) throws SQLException {
        String sql = "DELETE FROM item_midia WHERE id = ?";
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * Busca um item pelo id.
     *
     * @param id identificador
     * @return o item ou null se não existir
     * @throws SQLException em caso de erro de banco
     */
    public ItemMidia buscarPorId(int id) throws SQLException {
        String sql = "SELECT " + COLUNAS + " FROM item_midia WHERE id = ?";
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        }
    }

    /**
     * Lista itens com busca opcional por título/autor-diretor e filtro por gênero.
     * O SQL é montado apenas com marcadores "?" e fragmentos fixos; o texto
     * digitado pelo usuário vai sempre como parâmetro.
     *
     * @param termo  texto procurado em título ou autor/diretor (pode ser vazio)
     * @param genero gênero exato para filtrar (pode ser vazio)
     * @return lista de itens ordenada por título
     * @throws SQLException em caso de erro de banco
     */
    public List<ItemMidia> buscar(String termo, String genero) throws SQLException {
        StringBuilder sql = new StringBuilder("SELECT " + COLUNAS + " FROM item_midia WHERE 1 = 1");
        List<String> parametros = new ArrayList<>();

        if (termo != null && !termo.isBlank()) {
            sql.append(" AND (titulo LIKE ? OR autor_diretor LIKE ?)");
            String padrao = "%" + termo.trim() + "%";
            parametros.add(padrao);
            parametros.add(padrao);
        }
        if (genero != null && !genero.isBlank()) {
            sql.append(" AND genero = ?");
            parametros.add(genero.trim());
        }
        sql.append(" ORDER BY titulo");

        List<ItemMidia> itens = new ArrayList<>();
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < parametros.size(); i++) {
                stmt.setString(i + 1, parametros.get(i));
            }
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    itens.add(mapear(rs));
                }
            }
        }
        return itens;
    }

    /**
     * Retorna os gêneros distintos já cadastrados (usado no filtro da tela).
     *
     * @return lista de gêneros em ordem alfabética
     * @throws SQLException em caso de erro de banco
     */
    public List<String> listarGeneros() throws SQLException {
        String sql = "SELECT DISTINCT genero FROM item_midia "
                + "WHERE genero IS NOT NULL AND genero <> '' ORDER BY genero";
        List<String> generos = new ArrayList<>();
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                generos.add(rs.getString(1));
            }
        }
        return generos;
    }

    // ---------- métodos auxiliares (evitam duplicação de código) ----------

    /** Preenche os 8 parâmetros comuns ao INSERT e ao UPDATE. */
    private void preencherParametros(PreparedStatement stmt, ItemMidia item) throws SQLException {
        stmt.setString(1, item.getTitulo());
        stmt.setString(2, item.getAutorDiretor());
        definirInteiro(stmt, 3, item.getAnoLancamento());
        stmt.setString(4, item.getGenero());
        stmt.setString(5, item.getSinopse());
        stmt.setString(6, item.getTipoMidia());
        definirInteiro(stmt, 7, item.getNota());
        stmt.setString(8, item.getCapaUrl());
    }

    private void definirInteiro(PreparedStatement stmt, int posicao, Integer valor) throws SQLException {
        if (valor == null) {
            stmt.setNull(posicao, Types.INTEGER);
        } else {
            stmt.setInt(posicao, valor);
        }
    }

    /** Converte a linha atual do ResultSet em um objeto ItemMidia. */
    private ItemMidia mapear(ResultSet rs) throws SQLException {
        ItemMidia item = new ItemMidia();
        item.setId(rs.getInt("id"));
        item.setTitulo(rs.getString("titulo"));
        item.setAutorDiretor(rs.getString("autor_diretor"));
        int ano = rs.getInt("ano_lancamento");
        item.setAnoLancamento(rs.wasNull() ? null : ano);
        item.setGenero(rs.getString("genero"));
        item.setSinopse(rs.getString("sinopse"));
        item.setTipoMidia(rs.getString("tipo_midia"));
        int nota = rs.getInt("nota");
        item.setNota(rs.wasNull() ? null : nota);
        item.setCapaUrl(rs.getString("capa_url"));
        return item;
    }
}
