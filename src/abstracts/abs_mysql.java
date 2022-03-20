package abstracts;

import interfaces.int_mysql;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLNonTransientConnectionException;
import java.sql.Statement;

public abstract class abs_mysql implements int_mysql {

    private Connection con = null;
    private Statement stmt = null;
    private ResultSet rs = null;

    @Override
    public Connection getCon() {
        return con;
    }

    @Override
    public Connection conectar(String url, String usuario, String contraseña) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");//.newInstance();
            con = DriverManager.getConnection(url, usuario, contraseña);
            System.out.println("BDD conectada");
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println("No se puede conectar: " + ex);
        }
        return con;
    }

    @Override
    public Statement crear_estado(Connection con) {
        try {
            stmt = con.createStatement();
        } catch (SQLNonTransientConnectionException ex) {

        } catch (SQLException e) {
            System.out.println("Error de conexión: " + e);
        }
        return stmt;
    }

    @Override
    public void cerrar_conexion(Connection con) {
        try {
            if (con != null) {
                con.close();
                System.out.println("Conexión cerrada");
            }
            if (this.rs != null) {
                this.rs.close();
            }
            if (this.stmt != null) {
                this.stmt.close();
            }
        } catch (SQLException e) {
            System.out.println("Can´t close the connection: " + e);
        }
    }
}
