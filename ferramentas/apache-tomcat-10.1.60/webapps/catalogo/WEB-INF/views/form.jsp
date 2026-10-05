<%@ include file="_topo.jspf" %>
<c:set var="base" value="${pageContext.request.contextPath}/itens" />

<%-- Se houve erro de validação, reexibe o que o usuário digitou (${param});
     caso contrário, usa os dados do item (edição) ou deixa vazio (novo). --%>
<c:set var="comErro" value="${not empty erros}" />
<c:set var="vId"     value="${comErro ? param.id : item.id}" />
<c:set var="vTitulo" value="${comErro ? param.titulo : item.titulo}" />
<c:set var="vAutor"  value="${comErro ? param.autorDiretor : item.autorDiretor}" />
<c:set var="vAno"    value="${comErro ? param.ano : item.anoLancamento}" />
<c:set var="vGenero" value="${comErro ? param.genero : item.genero}" />
<c:set var="vSinopse" value="${comErro ? param.sinopse : item.sinopse}" />
<c:set var="vTipo"   value="${comErro ? param.tipoMidia : item.tipoMidia}" />
<c:set var="vNota"   value="${comErro ? param.nota : item.nota}" />
<c:set var="vCapa"   value="${comErro ? param.capaUrl : item.capaUrl}" />

<h1><c:out value="${empty vId ? 'Novo item' : 'Editar item'}"/></h1>

<c:if test="${comErro}">
    <div class="alerta erro">
        <strong>Corrija os campos abaixo:</strong>
        <ul>
            <c:forEach var="e" items="${erros}"><li><c:out value="${e}"/></li></c:forEach>
        </ul>
    </div>
</c:if>

<form class="formulario" method="post" action="${base}">
    <input type="hidden" name="acao" value="salvar">
    <input type="hidden" name="id" value="<c:out value='${vId}'/>">

    <label>Título *
        <input type="text" name="titulo" maxlength="255" required value="<c:out value='${vTitulo}'/>">
    </label>

    <label>Tipo *
        <select name="tipoMidia" required>
            <option value="">Selecione...</option>
            <option value="Livro" ${vTipo == 'Livro' ? 'selected' : ''}>Livro</option>
            <option value="Filme" ${vTipo == 'Filme' ? 'selected' : ''}>Filme</option>
            <option value="Série" ${vTipo == 'Série' ? 'selected' : ''}>Série</option>
        </select>
    </label>

    <label>Autor / Diretor
        <input type="text" name="autorDiretor" maxlength="255" value="<c:out value='${vAutor}'/>">
    </label>

    <label>Ano de publicação / lançamento
        <input type="text" name="ano" inputmode="numeric" maxlength="4" value="<c:out value='${vAno}'/>">
    </label>

    <label>Gênero
        <input type="text" name="genero" maxlength="100" list="sugestoes-genero"
               value="<c:out value='${vGenero}'/>">
        <datalist id="sugestoes-genero">
            <option value="Romance"><option value="Drama"><option value="Comédia">
            <option value="Ficção"><option value="Ação"><option value="Aventura">
            <option value="Documentário">
        </datalist>
    </label>

    <label>Nota (0 a 5)
        <select name="nota">
            <option value="">Sem nota</option>
            <c:forEach begin="0" end="5" var="n">
                <option value="${n}" ${vNota == n ? 'selected' : ''}>${n}</option>
            </c:forEach>
        </select>
    </label>

    <label>Link da capa (opcional)
        <input type="url" name="capaUrl" maxlength="500" placeholder="https://..."
               value="<c:out value='${vCapa}'/>">
    </label>

    <label>Sinopse
        <textarea name="sinopse" rows="5" maxlength="5000"><c:out value="${vSinopse}"/></textarea>
    </label>

    <div class="botoes">
        <button type="submit" class="btn">Salvar</button>
        <a class="btn secundario" href="${base}">Cancelar</a>
    </div>
</form>
<%@ include file="_rodape.jspf" %>
