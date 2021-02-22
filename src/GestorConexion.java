
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import oracle.jdbc.driver.OracleDriver;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Javi
 */
public class GestorConexion {

    Connection conn1 = null;

    public int GestorConexion() { //cambio el constructor por un método que retorna un int para poder conectar con la base de datos mediante un boton

        int aux = 1; //esta variable auxiliar recibe 0 en caso de conectar a la bbdd, 1 en el caso de no poder conectar y -1 si ha habido algún error  
        try {
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
            conn1 = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521/NFL_oracle", "root", "1234");
            if (conn1 != null) {
                aux = 0;
            } else {
                aux = 1;
            }
        } catch (SQLException ex) {
            aux = -1;
            System.out.println(ex.toString());
        }
        return aux;
    }

    public void cerrar_conexion() {
        try {
            conn1.close();
            if (conn1.isClosed()) {
                System.out.println("Desconectado de la bbdd");
            }

        } catch (SQLException ex) {
            System.out.println("Error al cerrar la conexion a la bbdd");
        }
    }

    //este método consulta los datos de la tabla superbowls y los muestra todos por pantalla
    public String selectSB() {
        String query = "SELECT * FROM SuperBowls s, Equipos e WHERE e.SuperBowl.Cod_SB = s.Cod_SB";
        String salida = "";
        String aux = "";
        try {
            Statement sta = conn1.createStatement();
            ResultSet rs = sta.executeQuery(query);
            while (rs.next()) {

                aux = ("\n-------------------------------------------------"
                        + "\n Equipo: " + rs.getString("Nombre_equipo")
                        + "\n Número de victorias: " + rs.getString("Cantidad_victorias")
                        + "\n Año de las victorias: " + rs.getString("Fecha_victoria")
                        + "\n-------------------------------------------------");
                salida = salida + aux;
            }
            rs.close();
            sta.close();
            return salida;
        } catch (SQLException ex) {
            return ex.toString();
        }
    }

    //este método hace lo mismo que el anterior pero sobre la tabla datos
    public String selectDatos() {
        String query = "SELECT * FROM DatosEquipo d, Equipos e WHERE e.Datos.Cod_dato = d.Cod_dato";
        String salida = "";
        String aux = "";
        try {
            Statement sta = conn1.createStatement();
            ResultSet rs = sta.executeQuery(query);
            while (rs.next()) {

                aux = ("\n-------------------------------------------------"
                        + "\n Equipo: " + rs.getString("Nombre_equipo")
                        + "\n Conferencia: " + rs.getString("Conferencia")
                        + "\n División: " + rs.getString("Division")
                        + "\n Propietario: " + rs.getString("Propietario")
                        + "\n Fundación: " + rs.getString("Fundacion")
                        + "\n Entrada en la NFL: " + rs.getString("Entrada_NFL")
                        + "\n-------------------------------------------------");
                salida = salida + aux;
            }
            rs.close();
            sta.close();
            return salida;
        } catch (SQLException ex) {
            return ex.toString();
        }
    }

    //este método hace lo mismo que los dos anteriores pero muestra los datos de todos los equipos
    public String selectEquipos() {
        String query = "SELECT * FROM Equipos e, DatosEquipo d, SuperBowls s WHERE e.Datos.Cod_dato = d.Cod_dato AND e.SuperBowl.Cod_SB = s.Cod_SB";
        String salida = "";
        String aux = "";
        try {
            Statement sta = conn1.createStatement();
            ResultSet rs = sta.executeQuery(query);
            while (rs.next()) {

                aux = ("\n-------------------------------------------------"
                        + "\n Nombre del equipo: " + rs.getString("Nombre_equipo")
                        + "\n Ciudad de residencia: " + rs.getString("Ciudad_equipo")
                        + "\n Estadio del equipo: " + rs.getString("Estadio_equipo")
                        + "\n Conferencia: " + rs.getString("Conferencia")
                        + "\n División: " + rs.getString("Division")
                        + "\n Propietario: " + rs.getString("Propietario")
                        + "\n Fundación: " + rs.getString("Fundacion")
                        + "\n Entrada en la NFL: " + rs.getString("Entrada_NFL")
                        + "\n Número de victorias: " + rs.getString("Cantidad_victorias")
                        + "\n Año de las victorias: " + rs.getString("Fecha_victoria")
                        + "\n-------------------------------------------------");
                salida = salida + aux;
            }
            rs.close();
            sta.close();
            return salida;
        } catch (SQLException ex) {
            return ex.toString();
        }
    }

    //este método busca unequipo por su nombre, introducido por el usuario y devuelve todos los datos de dicho equipo
    public String buscarEquipo(String nombre) {
        String query = "SELECT * FROM Equipos, DatosEquipo, SuperBowls WHERE Nombre_equipo = ?";
        String salida = "";
        try {
            PreparedStatement pst = conn1.prepareStatement(query);
            pst.setString(1, nombre);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                //mientras haya datos en el rs de la query los va sumando a la salida con un salto de linea
                salida = ("\n-------------------------------------------------"
                        + "\n Nombre del equipo: " + rs.getString("Nombre_equipo")
                        + "\n Ciudad de residencia: " + rs.getString("Ciudad_equipo")
                        + "\n Estadio del equipo: " + rs.getString("Estadio_equipo")
                        + "\n Conferencia: " + rs.getString("Conferencia")
                        + "\n División: " + rs.getString("Division")
                        + "\n Propietario: " + rs.getString("Propietario")
                        + "\n Fundación: " + rs.getString("Fundacion")
                        + "\n Entrada en la NFL: " + rs.getString("Entrada_NFL")
                        + "\n Número de victorias: " + rs.getString("Cantidad_victorias")
                        + "\n Año de las victorias: " + rs.getString("Fecha_victoria")
                        + "\n-------------------------------------------------");
            }
            rs.close();
            pst.close();
            return salida;
        } catch (SQLException ex) {
            return ex.toString();
        }
    }

    //este método modifica un equipo, la seleccion del equipo es elegida por el usuario mediante un combobox que le da valor al parámetro de entrada equipoModificado y
    //los datos nuevos son, también, introduccidos por el usuario
    public void modificarEquipo(String nombre_equipo, String ciudad, String estadio, int equipoModificado) {
        try {

            Statement sta = conn1.createStatement();
            sta.executeUpdate("UPDATE Equipos "
                    + "SET Nombre_equipo = '" + nombre_equipo + "', Ciudad_equipo = '" + ciudad + "', Estadio_equipo = '" + estadio + 
                    "' WHERE Cod_equipo = '" + equipoModificado + "'");
            sta.close();
            System.out.println("Datos modificados correctamente");
        } catch (SQLException ex) {
            System.out.println(ex.toString());
        }
    }

    public void insertarDatosEquipo(String conferencia, String division, String propietario, String fundacion, String entradaNFL, int cod_dato) {

        try {
            Statement sta = conn1.createStatement();
            sta.executeUpdate("INSERT INTO DatosEquipo VALUES('" + conferencia + "', '" + division + "', '" + propietario + "', '" + fundacion + "', "
                    + "'" + entradaNFL + "', '" + cod_dato + "')");
            sta.close();
            System.out.println("Datos añadidos correctamente");
        } catch (SQLException ex) {
            System.out.println(ex.toString());
        }
    }

    public void insertarSuperBowls(String fecha_victoria, String cantidad_victorias, int cod_sb) {

        try {
            Statement sta = conn1.createStatement();
            sta.executeUpdate("INSERT INTO SuperBowls VALUES('" + fecha_victoria + "', '" + cantidad_victorias + "', '" + cod_sb + "')");
            sta.close();
            System.out.println("Datos añadidos correctamente");
        } catch (SQLException ex) {
            System.out.println(ex.toString());
        }
    }

    public void insertarEquipo(int cod_equipo, String nombre_equipo, String ciudad_equipo, String estadio_equipo, int datos, int superBowl) {

        try {
            Statement sta = conn1.createStatement();
            sta.executeUpdate("INSERT INTO Equipos VALUES"
                    + "('" + cod_equipo + "', '" + nombre_equipo + "', '" + ciudad_equipo + "', '" + estadio_equipo + "', "
                    + "(SELECT REF(d) FROM DatosEquipo d WHERE d.Cod_dato = '" + datos + "'), (SELECT REF(sb) FROM SuperBowls sb WHERE sb.Cod_sb = '" + superBowl + "'))");
            sta.close();
            System.out.println("Datos añadidos correctamente");
        } catch (SQLException ex) {
            System.out.println(ex.toString());
        }
    }

    public void borrarEquipo(String dato) {
        try {
            Statement sta = conn1.createStatement();
            sta.executeUpdate("DELETE FROM Equipos WHERE nombre_equipo = '" + dato + "'");
            sta.close();
            System.out.println("Datos borrados correctamente");
        } catch (SQLException ex) {
            System.out.println(ex.toString());
        }
    }

    //este método devuelve un array list con el id y el nombre de todos los equipos para mas adelante llenar el combobox
    public ArrayList<String> comboBoxEquipos() {
        ArrayList<String> list = new ArrayList<String>();

        String query = "SELECT * FROM Equipos";
        try {
            Statement sta = conn1.createStatement();
            ResultSet rs = sta.executeQuery(query);
            while (rs.next()) {
                //se van añadiendo a la lista el id, un guion con espacios y el nombre para usarlo mas adelante en la selección de uno de los datos del combobox
                list.add(rs.getInt("Cod_equipo") + " - " + rs.getString("Nombre_equipo"));
            }
            rs.close();
            sta.close();
            return list;
        } catch (SQLException ex) {
            System.out.println(ex.toString());
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    public ArrayList<String> comboBoxSuperBowls() {
        ArrayList<String> list = new ArrayList<String>();

        String query = "SELECT * FROM SuperBowls";
        try {
            Statement sta = conn1.createStatement();
            ResultSet rs = sta.executeQuery(query);
            while (rs.next()) {
                list.add(rs.getInt("Cod_sb") + " - " + rs.getString("Fecha_victoria"));
            }
            rs.close();
            sta.close();
            return list;
        } catch (SQLException ex) {
            System.out.println(ex.toString());
            return null;
        } catch (Exception e) {
            return null;
        }
    }
    
    public ArrayList<String> comboBoxDatos() {
        ArrayList<String> list = new ArrayList<String>();

        String query = "SELECT * FROM DatosEquipo";
        try {
            Statement sta = conn1.createStatement();
            ResultSet rs = sta.executeQuery(query);
            while (rs.next()) {
                list.add(rs.getInt("Cod_dato") + " - " + rs.getString("Division"));
            }
            rs.close();
            sta.close();
            return list;
        } catch (SQLException ex) {
            System.out.println(ex.toString());
            return null;
        } catch (Exception e) {
            return null;
        }
    }
}
