package abstracts;

import interfaces.int_mysqlFunctions;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLNonTransientConnectionException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class abs_mysqlFunctions implements int_mysqlFunctions {

    private Connection con = null;
    private Statement stmt = null;
    private ResultSet rs = null;
    private DatabaseMetaData metaData = null;
    private String url = "jdbc:mysql://localhost:3306/?useTimezone=true&serverTimezone=UTC";
    private String usuario = "root";
    private String contraseña = "";
    private int count = 0;
    private ArrayList array;
    private abs_mysql mysql = null;

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
        } catch (SQLNonTransientConnectionException ex) {
            this.mysql.cerrar_conexion(con);
            System.out.println("SQLNonTransientConnectionException:" + ex);
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println("No se puede conectar: " + ex);
        }
        return con;
    }

    @Override
    public Statement crear_estado(Connection con) {
        try {
            this.stmt = con.createStatement();
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
//            if (this.rs != null) {
//                this.rs.close();
//            }
//            if (this.stmt != null) {
//                this.stmt.close();
//            }
        } catch (SQLException e) {
            System.out.println("Can´t close the connection: " + e);
        }
    }

    @Override//no se esta usando
    public ResultSet elegir_bdd(String database, String table_name, String columnas_a_elegir) {
        try {
            this.mysql = new abs_mysql() {
            };
            this.con = this.mysql.conectar(url, usuario, contraseña);
            this.stmt = crear_estado(this.con);
            this.rs = this.stmt.executeQuery("select " + columnas_a_elegir + " from " + database + "." + table_name);
        } catch (SQLException e) {
            System.out.println("Incorrect database, table name or columns: " + e);
        }
        return this.rs;
    }

    @Override//no se esta usando
    public void crear_indice(String database, String table_name, String columna_a_elegir, String nombre_de_indice) {
        try {
            this.mysql = new abs_mysql() {
            };
            this.con = this.mysql.conectar(url, usuario, contraseña);
            this.stmt = crear_estado(this.con);
            this.stmt.execute("alter table " + database + "." + table_name + " add index " + nombre_de_indice + "(" + columna_a_elegir + ")");
            this.mysql.cerrar_conexion(this.con);
        } catch (SQLException e) {
            System.out.println("Incorrect database or table name " + e);
        }
    }

    @Override
    public void limpiar_tabla(String database, String table_name) {
        try {
            this.mysql = new abs_mysql() {
            };
            this.con = this.mysql.conectar(url, usuario, contraseña);
            this.stmt = crear_estado(con);
            this.stmt.execute("truncate table " + database + "." + table_name);
            this.mysql.cerrar_conexion(this.con);
        } catch (SQLException e) {
            System.out.println("Incorrect database or table name " + e);
        }
    }

    @Override
    public void limpiar_fila(String database, String table_name, String columna_referencia, String dato_referencia) {
        try {
            this.mysql = new abs_mysql() {
            };
            this.con = this.mysql.conectar(url, usuario, contraseña);
            this.stmt = crear_estado(con);
            this.stmt.execute("delete from " + database + "." + table_name + " where " + columna_referencia + "='" + dato_referencia + "'");
            this.mysql.cerrar_conexion(this.con);
        } catch (SQLException e) {
            System.out.println("Incorrect database or table name " + e);
        }
    }

    @Override
    public int cantidad_de_columnas(String database, String table_name) {
        this.rs = clausulaSelect_bdd("SELECT Table_Name, COUNT(*) As NumeroCampos FROM Information_Schema.Columns WHERE Table_Name = '" + table_name + "' GROUP BY Table_Name");
        try {
            while (this.rs.next()) {
                this.count = this.rs.getInt(2);
            }
            if (this.count == 0) {
                throw new SQLException();
            }
        } catch (SQLException ex) {
            this.rs = clausulaSelect_bdd("select * from " + database + "." + table_name);
            try {
                this.count = this.rs.getMetaData().getColumnCount();
            } catch (SQLException ex1) {
                System.out.println("No se puede obtener la cantidad de columnas de " + database + "." + table_name + ": " + ex1);
            }
        }
        this.mysql.cerrar_conexion(this.con);
        return this.count;
    }

    @Override
    public int cantidad_de_filas(String database, String table_name) {
        this.rs = clausulaSelect_bdd("select count(*) from " + database + "." + table_name);
        try {
            while (this.rs.next()) {
                this.count = this.rs.getInt(1);
            }
            if (this.count == 0) {
                throw new SQLException();
            }
        } catch (SQLException ex) {
            this.rs = clausulaSelect_bdd("select * from" + database + "." + table_name);
            try {
                boolean ultimo = this.rs.last();
                if (ultimo) {
                    this.count = this.rs.getRow();
                }
            } catch (SQLException ex1) {
                System.out.println("No se puede obtener la cantidad de filas de " + database + "." + table_name + ": " + ex1);
            }
        }
        this.mysql.cerrar_conexion(this.con);
        return this.count;
    }

    @Override
    public ArrayList obtener_registro(String database, String table_name, String columna_de_busqueda, String criterio_de_busqueda) {
        this.array = new ArrayList();
        int cantidad_de_columnas = 0;
        cantidad_de_columnas = cantidad_de_columnas(database, table_name);
        this.rs = clausulaSelect_bdd("select * from " + database + "." + table_name + " where " + columna_de_busqueda + "='" + criterio_de_busqueda + "'");
        try {
            while (this.rs.next()) {
                for (int i = 1; i <= cantidad_de_columnas; i++) {
                    this.array.add(this.rs.getString(i));
                }
            }
        } catch (SQLException ex) {
            //utilizar resulsetmetdata para obtener datos de tabla usando optionpane
        }
        this.mysql.cerrar_conexion(this.con);
        return this.array;
    }

    @Override
    public DatabaseMetaData obtener_metadatos(Connection con) {
        this.mysql = new abs_mysql() {
        };
        this.con = this.mysql.conectar(this.url, this.usuario, this.contraseña);
        try {
            this.metaData = this.con.getMetaData();
        } catch (SQLException ex) {
            System.out.println("No se puede obtener los metaDatos de la conexión MySql: ");
            ex.printStackTrace(System.out);
        }
        return this.metaData;
    }

    @Override
    public ArrayList obtener_lista_de_columnas(String database, String table_name) {
        this.array = new ArrayList();
        this.mysql = new abs_mysql() {
        };
        this.con = this.mysql.conectar(this.url, this.usuario, this.contraseña);
        this.metaData = obtener_metadatos(this.con);
        try {
            this.rs = this.metaData.getColumns(null, null, table_name, null);
            while (rs.next()) {
                this.array.add(rs.getString("COLUMN_NAME"));
            }
        } catch (SQLException ex) {
            System.out.println("No se pudo obtener la lista de columnas de " + database + "." + table_name);
        }
        this.mysql.cerrar_conexion(this.con);
        return this.array;
    }

    @Override
    public ArrayList obtener_lista_de_tablas(String database) {
        this.array = new ArrayList();
        this.mysql = new abs_mysql() {
        };
        this.con = this.mysql.conectar(this.url, this.usuario, this.contraseña);
        this.metaData = obtener_metadatos(this.con);
        try {
            this.rs = this.metaData.getTables(database, null, "%", null);
            while (rs.next()) {
                this.array.add(rs.getString(3));
            }
        } catch (SQLException ex) {
            System.out.println("No se pudo obtener la lista de tablas de " + database);
        }
        this.mysql.cerrar_conexion(this.con);
        return this.array;
    }

    @Override
    public ArrayList obtener_lista_de_bdd() {
        this.array = new ArrayList();
        this.mysql = new abs_mysql() {
        };
        this.con = this.mysql.conectar(this.url, this.usuario, this.contraseña);
        this.metaData = obtener_metadatos(this.con);
        try {
            this.rs = this.metaData.getCatalogs();//obtiene los nombres de todas las bases de datos
            while (rs.next()) {
                this.array.add(rs.getString(1));
            }
        } catch (SQLException ex) {
            try {
                System.out.println("No se pudo obtener la lista de las bases de datos de " + this.metaData.getDatabaseProductName());
            } catch (SQLException ex1) {
                Logger.getLogger(abs_mysqlFunctions.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        this.mysql.cerrar_conexion(this.con);
        return this.array;
    }

    @Override
    public ResultSet clausulaSelect_bdd(String script) {
        try {
            this.mysql = new abs_mysql() {
            };
            this.con = this.mysql.conectar(this.url, this.usuario, this.contraseña);
            this.stmt = crear_estado(this.con);
            this.rs = this.stmt.executeQuery(script);
        } catch (SQLException e) {
            System.out.println("Error de clausulaSelect_bdd: " + e);
        }
        return this.rs;
    }

    @Override
    public void clausulaInsert_bdd(String script) {
        try {
            this.mysql = new abs_mysql() {
            };
            this.con = this.mysql.conectar(this.url, this.usuario, this.contraseña);
            this.stmt = crear_estado(this.con);
            this.stmt.execute(script);
            this.mysql.cerrar_conexion(this.con);
        } catch (SQLException e) {
            System.out.println("Incorrect database, table name or columns: " + e);
        }
    }
}
/*
if (input_combo.isSelected()) {
            input.setSelected(false);
            confirm.setSelected(false);
            message.setSelected(false);
            option.setSelected(false);
            internal.setSelected(false);
            Object opcion = JOptionPane.showInputDialog(null, "Selecciona un color", "Elegir", JOptionPane.QUESTION_MESSAGE, null, colores, colores[1]);
            JOptionPane.showMessageDialog(input, "El color elegido es: " + opcion, "Colores", JOptionPane.INFORMATION_MESSAGE);
        }input_combo.setSelected(false);
 */
