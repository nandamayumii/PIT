package br.edu.catalogo.controller;

import br.edu.catalogo.dao.ItemMidiaDAO;
import br.edu.catalogo.model.ItemMidia;
import br.edu.catalogo.service.ItemMidiaService;
import br.edu.catalogo.service.ValidacaoException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controlador (Servlet) do catálogo. Apenas recebe as requisições HTTP,
 * delega o trabalho ao Service e encaminha para a JSP adequada.
 * Ações (parâmetro "acao"): listar (padrão), novo, editar, detalhes, salvar, excluir.
 */
@WebServlet("/itens")
public class ItemMidiaServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(ItemMidiaServlet.class.getName());
    private static final String VIEWS = "/WEB-INF/views/";

    private final ItemMidiaService servico = new ItemMidiaService(new ItemMidiaDAO());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String acao = req.getParameter("acao");
        if (acao == null || acao.isBlank()) {
            acao = "listar";
        }
        try {
            switch (acao) {
                case "novo" -> encaminhar(req, resp, "form.jsp");
                case "editar" -> editar(req, resp);
                case "detalhes" -> detalhes(req, resp);
                default -> listar(req, resp);
            }
        } catch (SQLException e) {
            tratarErroBanco(e, req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String acao = req.getParameter("acao");
        try {
            if ("salvar".equals(acao)) {
                salvar(req, resp);
            } else if ("excluir".equals(acao)) {
                excluir(req, resp);
            } else {
                resp.sendRedirect(req.getContextPath() + "/itens");
            }
        } catch (SQLException e) {
            tratarErroBanco(e, req, resp);
        }
    }

    // ---------------- ações ----------------

    private void listar(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, ServletException, IOException {
        String q = req.getParameter("q");
        String genero = req.getParameter("genero");
        req.setAttribute("itens", servico.buscar(q, genero));
        req.setAttribute("generos", servico.listarGeneros());
        moverMensagemDaSessao(req);
        encaminhar(req, resp, "lista.jsp");
    }

    private void detalhes(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, ServletException, IOException {
        ItemMidia item = carregarItem(req);
        if (item == null) {
            avisarERedirecionar(req, resp, "erro", "Item não encontrado.");
            return;
        }
        req.setAttribute("item", item);
        encaminhar(req, resp, "detalhes.jsp");
    }

    private void editar(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, ServletException, IOException {
        ItemMidia item = carregarItem(req);
        if (item == null) {
            avisarERedirecionar(req, resp, "erro", "Item não encontrado.");
            return;
        }
        req.setAttribute("item", item);
        encaminhar(req, resp, "form.jsp");
    }

    private void salvar(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, ServletException, IOException {
        try {
            ItemMidia item = servico.criarItem(
                    req.getParameter("id"), req.getParameter("titulo"),
                    req.getParameter("autorDiretor"), req.getParameter("ano"),
                    req.getParameter("genero"), req.getParameter("sinopse"),
                    req.getParameter("tipoMidia"), req.getParameter("nota"),
                    req.getParameter("capaUrl"));
            boolean novo = item.getId() == null;
            servico.salvar(item);
            avisarERedirecionar(req, resp, "sucesso",
                    novo ? "Item cadastrado com sucesso!" : "Item atualizado com sucesso!");
        } catch (ValidacaoException e) {
            // Volta ao formulário mostrando os erros; os valores digitados vêm de ${param}
            req.setAttribute("erros", e.getErros());
            encaminhar(req, resp, "form.jsp");
        }
    }

    private void excluir(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, IOException {
        Integer id = lerId(req);
        if (id != null && servico.excluir(id)) {
            avisarERedirecionar(req, resp, "sucesso", "Item excluído com sucesso.");
        } else {
            avisarERedirecionar(req, resp, "erro", "Não foi possível excluir: item não encontrado.");
        }
    }

    // ---------------- auxiliares ----------------

    private ItemMidia carregarItem(HttpServletRequest req) throws SQLException {
        Integer id = lerId(req);
        return id == null ? null : servico.buscarPorId(id);
    }

    /** Lê o parâmetro "id"; devolve null se estiver ausente ou não for número. */
    private Integer lerId(HttpServletRequest req) {
        try {
            return Integer.valueOf(req.getParameter("id"));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private void encaminhar(HttpServletRequest req, HttpServletResponse resp, String view)
            throws ServletException, IOException {
        req.getRequestDispatcher(VIEWS + view).forward(req, resp);
    }

    /** Guarda uma mensagem na sessão e redireciona (padrão Post/Redirect/Get). */
    private void avisarERedirecionar(HttpServletRequest req, HttpServletResponse resp,
                                     String tipo, String texto) throws IOException {
        HttpSession sessao = req.getSession();
        sessao.setAttribute("mensagemTipo", tipo);
        sessao.setAttribute("mensagemTexto", texto);
        resp.sendRedirect(req.getContextPath() + "/itens");
    }

    /** Mostra a mensagem guardada na sessão uma única vez. */
    private void moverMensagemDaSessao(HttpServletRequest req) {
        HttpSession sessao = req.getSession(false);
        if (sessao != null && sessao.getAttribute("mensagemTexto") != null) {
            req.setAttribute("mensagemTipo", sessao.getAttribute("mensagemTipo"));
            req.setAttribute("mensagemTexto", sessao.getAttribute("mensagemTexto"));
            sessao.removeAttribute("mensagemTipo");
            sessao.removeAttribute("mensagemTexto");
        }
    }

    /** Registra o erro técnico no log e mostra mensagem amigável ao usuário. */
    private void tratarErroBanco(SQLException e, HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        LOG.log(Level.SEVERE, "Erro de acesso ao banco de dados", e);
        resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        encaminhar(req, resp, "erro.jsp");
    }
}
