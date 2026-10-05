package br.edu.catalogo.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Fábrica de conexões JDBC. Lê url, usuário e senha do arquivo db.properties,
 * evitando deixar credenciais "chumbadas" no código-fonte.
 */
public final class ConexaoBD {

    private static final Properties CONFIG = new Properties();

    static {
        try (InputStream in = ConexaoBD.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (in == null) {
                throw new IllegalStateException("Arquivo db.properties não encontrado no classpath.");
            }
            CONFIG.load(in);
            Class.forName("com.mysql.cj.jdbc.Driver"); // garante o registro do driver no Tomcat
        } catch (IOException | ClassNotFoundException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private ConexaoBD() {
    }

    /**
     * Abre uma nova conexão com o banco. Quem chamar deve fechá-la
     * (use try-with-resources).
     *
     * @return conexão aberta
     * @throws SQLException se não for possível conectar
     */
    public static Connection getConexao() throws SQLException {
        return DriverManager.getConnection(
                CONFIG.getProperty("db.url"),
                CONFIG.getProperty("db.usuario"),
                CONFIG.getProperty("db.senha"));
    }
}
