package edu.umg.datos;

import edu.umg.domain.Persona;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PersonaJDBC {
    // En esta capa de datos vamos a manejar la manipulación de datos del objeto persona

    // Esta variable es la que apunta a la base de datos para sus transacciones
    private Connection conexionTransaccional;

    // Constantes para la manipulación de la información
    private static final String SQL_SELECT = "SELECT id_persona, nombre, apellido, email, telefono FROM persona";
    // SQL_SELECT_BY_ID es un string para seleccionar a una persona por su ID
    private static final String SQL_SELECT_BY_ID = "SELECT id_persona, nombre, apellido, email, telefono FROM persona WHERE id_persona = ?";
    private static final String SQL_INSERT = "INSERT INTO persona(nombre, apellido, email, telefono) VALUES(?, ?, ?, ?)";
    private static final String SQL_UPDATE = "UPDATE persona SET nombre=?, apellido=?, email=?, telefono=? WHERE id_persona = ?";
    private static final String SQL_DELETE = "DELETE FROM persona WHERE id_persona=?";


    // Constructor en blanco
    public PersonaJDBC(){

    }

    // Constructor, recibe de parámetro el manejador de la transacción
    public PersonaJDBC(Connection conexionTransaccional){
        this.conexionTransaccional = conexionTransaccional;
    }

    // Debido al uso de dos métodos select de tipo distinto, y para evitar repetir código
    // se crea un método que recibe un ResultSet y devuelve un objeto Persona
    private Persona resultSetParaPersona(ResultSet rs) throws SQLException {
        int id_persona = rs.getInt("id_persona");
        String nombre = rs.getString("nombre");
        String apellido = rs.getString("apellido");
        String email = rs.getString("email");
        String telefono = rs.getString("telefono");

        Persona personaEncontrada = new Persona();
        personaEncontrada.setId_persona(id_persona);
        personaEncontrada.setNombre(nombre);
        personaEncontrada.setApellido(apellido);
        personaEncontrada.setEmail(email);
        personaEncontrada.setTelefono(telefono);

        return personaEncontrada;
    }

    // Método para seleccionar a todas las personas de la base de datos
    public List<Persona> select() throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Persona> personas = new ArrayList<>();

        try {
            conn = this.conexionTransaccional != null ? this.conexionTransaccional : Conexion.getConnection();
            stmt = conn.prepareStatement(SQL_SELECT);
            rs = stmt.executeQuery();

            while (rs.next()) {
                personas.add(resultSetParaPersona(rs));
            }
        } finally {
            Conexion.close(rs);
            Conexion.close(stmt);
            if (this.conexionTransaccional == null) {
                Conexion.close(conn);
            }
        }

        return personas;
    }

    // Método para seleccionar a una persona por su ID
    public Persona selectPersona(Persona persona) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = this.conexionTransaccional != null ? this.conexionTransaccional : Conexion.getConnection();
            stmt = conn.prepareStatement(SQL_SELECT_BY_ID);
            stmt.setInt(1, persona.getId_persona());
            rs = stmt.executeQuery();

            if (rs.next()) {
                return resultSetParaPersona(rs);
            } else {
                return null;
            }
        } finally {
            Conexion.close(rs);
            Conexion.close(stmt);
            if (this.conexionTransaccional == null) {
                Conexion.close(conn);
            }
        }
    }

    public int insert(Persona persona) throws SQLException{
        Connection conn = null;
        PreparedStatement stmt = null;
        int rows = 0;

        try{
            conn = this.conexionTransaccional != null ? this.conexionTransaccional : Conexion.getConnection();
            stmt = conn.prepareStatement(SQL_INSERT);
            stmt.setString(1, persona.getNombre());
            stmt.setString(2, persona.getApellido());
            stmt.setString(3, persona.getEmail());
            stmt.setString(4, persona.getTelefono());

            System.out.println("Ejecutando query: " + SQL_INSERT);
            rows = stmt.executeUpdate();
            System.out.println("Registros afectados: " + rows);
        } finally {
            Conexion.close(stmt);
            if(this.conexionTransaccional == null){
                Conexion.close(conn);
            }
        }

        return rows;
    }

    public int update(Persona persona) throws SQLException{
        Connection conn = null;
        PreparedStatement stmt = null;
        int rows = 0;

        try{
            conn = this.conexionTransaccional != null ? this.conexionTransaccional : Conexion.getConnection();
            stmt = conn.prepareStatement(SQL_UPDATE);
            stmt.setString(1, persona.getNombre());
            stmt.setString(2, persona.getApellido());
            stmt.setString(3, persona.getEmail());
            stmt.setString(4, persona.getTelefono());
            stmt.setInt(5, persona.getId_persona());

            System.out.println("Ejecutando query: " + SQL_UPDATE);
            rows = stmt.executeUpdate();
            System.out.println("Registros afectados: " + rows);
        } finally {
            Conexion.close(stmt);
            if(this.conexionTransaccional == null){
                Conexion.close(conn);
            }
        }

        return rows;
    }

    public int delete(Persona persona) throws SQLException{
        Connection conn = null;
        PreparedStatement stmt = null;
        int rows = 0;

        try{
            conn = this.conexionTransaccional != null ? this.conexionTransaccional : Conexion.getConnection();
            stmt = conn.prepareStatement(SQL_DELETE);
            stmt.setInt(1, persona.getId_persona());

            System.out.println("Ejecutando query: " + SQL_DELETE);
            rows = stmt.executeUpdate();
            System.out.println("Registros afectados: " + rows);
        } finally {
            Conexion.close(stmt);
            if(this.conexionTransaccional == null){
                Conexion.close(conn);
            }
        }

        return rows;
    }

}
