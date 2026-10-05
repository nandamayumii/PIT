<%@ include file="_topo.jspf" %>
<c:set var="base" value="${pageContext.request.contextPath}/itens" />

<article class="detalhes">
    <c:if test="${not empty item.capaUrl}">
        <img class="capa" src="<c:out value='${item.capaUrl}'/>" alt="Capa de <c:out value='${item.titulo}'/>">
    </c:if>
    <div>
        <h1><c:out value="${item.titulo}"/></h1>
        <p class="estrelas">
            <c:if test="${item.nota != null}">
                <c:forEach begin="1" end="5" var="n">${n <= item.nota ? '★' : '☆'}</c:forEach>
            </c:if>
        </p>
        <p><strong>Tipo:</strong> <c:out value="${item.tipoMidia}"/></p>
        <p><strong>Autor/Diretor:</strong> <c:out value="${item.autorDiretor}"/></p>
        <p><strong>Ano:</strong> <c:out value="${item.anoLancamento}"/></p>
        <p><strong>Gênero:</strong> <c:out value="${item.genero}"/></p>
        <h2>Sinopse</h2>
        <p class="sinopse"><c:out value="${item.sinopse}"/></p>
        <p>
            <a class="btn" href="${base}?acao=editar&id=${item.id}">Editar</a>
            <a class="btn secundario" href="${base}">Voltar</a>
        </p>
    </div>
</article>
<%@ include file="_rodape.jspf" %>
