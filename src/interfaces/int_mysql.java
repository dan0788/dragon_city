package interfaces;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public interface int_mysql {
    
    public Connection getCon();
    public Connection conectar(String url, String usuario, String contraseña);
    public Statement crear_estado(Connection con);
    public void cerrar_conexion(Connection con);
}
