<%@ page isErrorPage="true" %>
<%@ include file="_topo.jspf" %>
<div class="alerta erro">
    <h1>Ops! Algo deu errado.</h1>
    <p>Não foi possível acessar o banco de dados no momento. Tente novamente em instantes.
       Se o problema continuar, verifique se o MySQL está em execução e se o arquivo
       <code>db.properties</code> está correto.</p>
    <p><a class="btn" href="${pageContext.request.contextPath}/itens">Voltar ao catálogo</a></p>
</div>
<%@ include file="_rodape.jspf" %>
