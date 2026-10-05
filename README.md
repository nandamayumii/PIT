# Catálogo Simples de Livros, Filmes e Séries

Aplicação web para catalogar livros, filmes e séries, com cadastro, listagem,
detalhes, edição, exclusão e busca por título ou autor/diretor.

**Tecnologias:** Java, Servlets, JSP/JSTL, JDBC, MySQL 8.4, Maven e Tomcat 10.1.

## Estrutura
- `model/ItemMidia`: entidade
- `dao/ItemMidiaDAO`: acesso ao banco (PreparedStatement)
- `service/ItemMidiaService`: validação e regras de negócio
- `controller/ItemMidiaServlet`: controlador (rota `/itens`)
- `webapp/WEB-INF/views/*.jsp`: telas
- `sql/schema.sql`: criação do banco e dados de exemplo

## Como executar
1. Instale JDK 17 ou superior, Maven, MySQL 8.4 e Tomcat 10.1.
2. Crie o banco:
   `mysql --default-character-set=utf8mb4 -u root -p < sql/schema.sql`
3. Edite `src/main/resources/db.properties` com seu usuário e senha do MySQL.
4. Gere o pacote: `mvn clean package`
5. Copie `target/catalogo.war` para a pasta `webapps` do Tomcat e inicie o servidor.
6. Acesse http://localhost:8080/catalogo/

## Testes
`mvn test` executa os testes JUnit das validações.
