package edu.umg.datos;

import java.sql.*;
import java.sql.SQLException;

public class Conexion {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/p2a";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASS = "defaultpw";

    // Crea la conexion a la base de datos
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASS);
    }

    // Cierra la conexion al resultset
    public static void close(ResultSet rs){
        try {
            rs.close();
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        }
    }

    // Cierra la conexion al statement
    public static void close(PreparedStatement stmt){
        try {
            stmt.close();
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        }
    }

    // Cierra la conexion a la base de datos
    public static void close(Connection conn){
        try {
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        }
    }
}
