package interfaces;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public interface int_mysqlFunctions {
    
    public Connection getCon();
    public Connection conectar(String url, String usuario, String contraseña);
    public Statement crear_estado(Connection con);
    public void cerrar_conexion(Connection con);

    public ResultSet elegir_bdd(String database, String table_name, String columnas_a_elegir);

    public void crear_indice(String database, String table_name, String columna_a_elegir, String nombre_de_indice);

    public void limpiar_tabla(String database, String table_name);

    public void limpiar_fila(String database, String table_name, String columna_referencia, String dato_referencia);

    public int cantidad_de_columnas(String database, String table_name);

    public int cantidad_de_filas(String database, String table_name);

    public ArrayList obtener_registro(String database, String table_name, String columna_de_busqueda, String criterio_de_busqueda);

    public DatabaseMetaData obtener_metadatos(Connection con);

    public ArrayList obtener_lista_de_columnas(String database, String table_name);

    public ArrayList obtener_lista_de_tablas(String database);

    public ArrayList obtener_lista_de_bdd();

    public ResultSet clausulaSelect_bdd(String script);

    public void clausulaInsert_bdd(String script);
}
