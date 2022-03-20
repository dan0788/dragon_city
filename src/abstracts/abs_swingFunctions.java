package abstracts;

import interfaces.int_swingFunctions;
import java.awt.Component;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

public abstract class abs_swingFunctions implements int_swingFunctions {

    private ArrayList array = null;
    private abs_mysqlFunctions mysqlFunctions = null;
    private ResultSet rs = null;

    @Override
    public void añadir_items_a_ComboBox(JComboBox comboBoxName, ArrayList array) {
        TreeSet<String> ts = new TreeSet<>(array);
        for (Object obj : ts) {
            comboBoxName.addItem(obj);
        }
    }

    @Override
    public void añadir_datos_a_Table(JTable tableName, String table_name, String database, int min_width, int max_width, boolean editable) throws SQLException {
        this.mysqlFunctions = new abs_mysqlFunctions() {
        };
        int cantidad_de_filas = this.mysqlFunctions.cantidad_de_filas(database, table_name);
        int cantidad_de_columnas = this.mysqlFunctions.cantidad_de_columnas(database, table_name);
        this.array = new ArrayList();
        this.array = this.mysqlFunctions.obtener_lista_de_columnas(database, table_name);
        String[] matriz_de_columnas = new String[cantidad_de_columnas];
        for (int i = 0; i < cantidad_de_columnas; i++) {
            matriz_de_columnas[i] = this.array.get(i).toString();
        }
        /////////////////////////tablecolumnmodel
        this.crear_modelo_de_tabla(tableName, min_width, max_width);
        //////////////////////defaulttablemodel
        DefaultTableModel dm = this.añadir_columnas_a_tabla(tableName, matriz_de_columnas, cantidad_de_columnas, editable);
        //////////////////////
        this.añadir_registros_a_tabla(tableName, dm, database, table_name, matriz_de_columnas, cantidad_de_filas, cantidad_de_columnas);
    }

    @Override
    public TableColumnModel crear_modelo_de_tabla(JTable tableName, int min_width, int max_width) {
        TableColumnModel columnModel = tableName.getColumnModel();//obtiene un objeto de tipo TableColumnModel
//        for (int column = 0; column < tableName.getColumnCount(); column++) {
//            int width = min_width; // Min width
//            for (int row = 0; row < tableName.getRowCount(); row++) {
//                TableCellRenderer renderer = tableName.getCellRenderer(row, column);
//                Component comp = tableName.prepareRenderer(renderer, row, column);
//                width = Math.max(comp.getPreferredSize().width + 1, width);
//            }
//            if (width > max_width) {
//                width = max_width;
//            }
//            columnModel.getColumn(column).setMinWidth(width);
//        }
        return columnModel;
    }

    @Override
    public DefaultTableModel añadir_columnas_a_tabla(JTable tableName, String[] matriz_de_columnas, int cantidad_de_columnas, boolean editable) {
        DefaultTableModel dm = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int i, int j) {
                return editable;
            }
        };
        dm.setColumnIdentifiers(matriz_de_columnas);
        tableName.setModel(dm);
        for (int i = 0; i < cantidad_de_columnas; i++) {
            TableColumn columna = tableName.getColumn(matriz_de_columnas[i]);//esta clase permite modificar las columnas
            char[] a = matriz_de_columnas[i].toCharArray();//convierte un String a cadena de char
            if (i == 0) {//ID
                columna.setPreferredWidth(a.length * 17);
                columna.setMinWidth(a.length * 13);
                columna.setMaxWidth(a.length * 25);
            }/* else {
                columna.setPreferredWidth(a.length * 13);
                columna.setMinWidth(a.length * 13);
            }*/
        }
        return dm;
    }

    @Override
    public void añadir_registros_a_tabla(JTable tableName, DefaultTableModel dm, String database, String table_name, String[] matriz_de_columnas, int cantidad_de_filas, int cantidad_de_columnas) {
        this.mysqlFunctions = new abs_mysqlFunctions() {
        };
        int i = 0;
        String txt;
        for (int h = 0; h < cantidad_de_filas; h++) {
            matriz_de_columnas = new String[cantidad_de_columnas];
            for (int j = 0; j < matriz_de_columnas.length; j++) {
                this.rs = this.mysqlFunctions.clausulaSelect_bdd("select * from " + database + "." + table_name);
                for (int k = 0; k <= h; k++) {
                    try {
                        while (this.rs.next()) {
                            i++;
                            txt = this.rs.getString(j + 1);
                            matriz_de_columnas[j] = txt;
                            break;
                        }
                    } catch (SQLException ex) {
                        System.out.println("Error de añadir_registros_a_tabla");
                        Logger.getLogger(abs_swingFunctions.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            dm.addRow(matriz_de_columnas);
        }
        DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();
        tcr.setHorizontalAlignment(SwingConstants.CENTER);
        for (int j = 0; j < matriz_de_columnas.length; j++) {
            tableName.getColumnModel().getColumn(j).setCellRenderer(tcr);
        }
    }
}
