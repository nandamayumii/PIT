<%@ include file="_topo.jspf" %>
<c:set var="base" value="${pageContext.request.contextPath}/itens" />

<c:if test="${not empty mensagemTexto}">
    <div class="alerta ${mensagemTipo}"><c:out value="${mensagemTexto}"/></div>
</c:if>

<h1>Itens do catálogo</h1>

<%-- Busca simples por título ou autor/diretor + filtro por gênero --%>
<form class="busca" method="get" action="${base}">
    <input type="text" name="q" placeholder="Buscar por título ou autor/diretor"
           value="<c:out value='${param.q}'/>">
    <select name="genero">
        <option value="">Todos os gêneros</option>
        <c:forEach var="g" items="${generos}">
            <option value="<c:out value='${g}'/>" ${param.genero == g ? 'selected' : ''}>
                <c:out value="${g}"/>
            </option>
        </c:forEach>
    </select>
    <button type="submit" class="btn">Buscar</button>
    <a class="btn secundario" href="${base}">Limpar</a>
</form>

<c:choose>
    <c:when test="${empty itens}">
        <p class="vazio">Nenhum item encontrado.</p>
    </c:when>
    <c:otherwise>
        <table>
            <thead>
            <tr>
                <th>Título</th><th>Autor/Diretor</th><th>Ano</th>
                <th>Gênero</th><th>Tipo</th><th>Nota</th><th>Ações</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="i" items="${itens}">
                <tr>
                    <td><c:out value="${i.titulo}"/></td>
                    <td><c:out value="${i.autorDiretor}"/></td>
                    <td><c:out value="${i.anoLancamento}"/></td>
                    <td><c:out value="${i.genero}"/></td>
                    <td><c:out value="${i.tipoMidia}"/></td>
                    <td class="estrelas">
                        <c:if test="${i.nota != null}">
                            <c:forEach begin="1" end="5" var="n">${n <= i.nota ? '★' : '☆'}</c:forEach>
                        </c:if>
                    </td>
                    <td class="acoes">
                        <a href="${base}?acao=detalhes&id=${i.id}">Ver</a>
                        <a href="${base}?acao=editar&id=${i.id}">Editar</a>
                        <form method="post" action="${base}"
                              onsubmit="return confirm('Excluir este item?');">
                            <input type="hidden" name="acao" value="excluir">
                            <input type="hidden" name="id" value="${i.id}">
                            <button type="submit" class="link perigo">Excluir</button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </c:otherwise>
</c:choose>
<%@ include file="_rodape.jspf" %>
