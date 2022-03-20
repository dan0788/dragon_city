package interfaces;

import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

public interface int_swingFunctions {
    public void añadir_items_a_ComboBox(JComboBox comboBoxName, ArrayList array);
    public void añadir_datos_a_Table(JTable tableName, String table_name, String database, int min_width, int max_width, boolean editable) throws SQLException;
    
    public TableColumnModel crear_modelo_de_tabla(JTable tableName, int min_width, int max_width);
    public DefaultTableModel añadir_columnas_a_tabla(JTable tableName, String[] matriz_de_columnas, int cantidad_de_columnas, boolean editable);
    public void añadir_registros_a_tabla(JTable tableName, DefaultTableModel dm, String database, String table_name, String[] matriz_de_columnas, int cantidad_de_filas, int cantidad_de_columnas);
}
