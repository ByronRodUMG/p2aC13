package edu.umg.datos;

import edu.umg.domain.Persona;
import edu.umg.domain.Usuario;

import java.security.*; // Librería para encriptar la contraseña
import java.sql.*;
import java.util.*;

public class UsuarioJDBC {

    private Connection conexionTransaccional;

    private static final String SQL_SELECT = "SELECT id_usuario, username, password FROM usuario";
    // SQL_SELECT_BY_ID es para seleccionar a un usuario por su ID
    private static final String SQL_SELECT_BY_ID = "SELECT id_usuario, username, password FROM usuario WHERE id_usuario = ?";
    // SQL_VALIDATE es para validar el usuario y contraseña al momento de hacer login
    private static final String SQL_VALIDATE = "SELECT password FROM usuario WHERE username = ?";
    private static final String SQL_INSERT = "INSERT INTO usuario(username, password) VALUES(?, ?)";
    private static final String SQL_UPDATE = "UPDATE usuario SET username=?, password=? WHERE id_usuario = ?";
    private static final String SQL_DELETE = "DELETE FROM usuario WHERE id_usuario=?";

    public UsuarioJDBC() {

    }

    public UsuarioJDBC(Connection conexionTransaccional) {
        this.conexionTransaccional = conexionTransaccional;
    }

    // Método para encriptar la contraseña usando SHA-256
    private String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256"); // Algoritmo de encriptación
        byte[] hashBytes = digest.digest(password.getBytes()); // Encripta la contraseña, devuelve un arreglo de bytes

        // Convertir el hash en una representación hexadecimal
        StringBuilder builder = new StringBuilder(); // Para concatenar los bytes
        // Recorre el arreglo de bytes y lo convierte a hexadecimal
        for (byte b : hashBytes) {
            builder.append(String.format("%02x", b)); // %02x es el formato hexadecimal
        }

        return builder.toString();
    }

    // Debido al uso de dos métodos select de tipo distinto, y para evitar repetir código
    // se crea un método que recibe un ResultSet y devuelve un objeto Persona
    private Usuario resultSetParaUsuario(ResultSet rs) throws SQLException {
        int id_usuario = rs.getInt("id_usuario");
        String username = rs.getString("username");
        String password = rs.getString("password");

        Usuario usuarioEncontrado = new Usuario();
        usuarioEncontrado.setId_usuario(id_usuario);
        usuarioEncontrado.setUsername(username);
        usuarioEncontrado.setPassword(password);

        return usuarioEncontrado;
    }

    // Método para seleccionar a todos los usuarios de la base de datos
    public List<Usuario> select() throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Usuario usuario = null;
        List<Usuario> usuarios = new ArrayList<Usuario>();

        try {
            conn = this.conexionTransaccional != null ? this.conexionTransaccional : Conexion.getConnection();
            stmt = conn.prepareStatement(SQL_SELECT);
            rs = stmt.executeQuery();
            while (rs.next()) {
                usuarios.add(resultSetParaUsuario(rs));
            }
        } finally {
            Conexion.close(rs);
            Conexion.close(stmt);
            if (this.conexionTransaccional == null) {
                Conexion.close(conn);
            }
        }

        return usuarios;
    }

    // Método para seleccionar a un usuario por su ID
    public Usuario selectUsuario(Usuario usuario) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Usuario usuarioEncontrado = null;

        try {
            conn = this.conexionTransaccional != null ? this.conexionTransaccional : Conexion.getConnection();
            stmt = conn.prepareStatement(SQL_SELECT_BY_ID);
            stmt.setInt(1, usuario.getId_usuario());
            rs = stmt.executeQuery();

            if (rs.next()) {
                return resultSetParaUsuario(rs);
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

    // Método para insertar un usuario en la base de datos
    // Encripta la contraseña antes de almacenarla usando SHA-256
    public int insert(Usuario usuario) throws SQLException, NoSuchAlgorithmException {
        Connection conn = null;
        PreparedStatement stmt = null;
        int rows = 0;

        try {
            conn = this.conexionTransaccional != null ? this.conexionTransaccional : Conexion.getConnection();
            stmt = conn.prepareStatement(SQL_INSERT);
            stmt.setString(1, usuario.getUsername());

            // Encriptar la contraseña antes de almacenarla en la base de datos
            String hashedPassword = hashPassword(usuario.getPassword());
            stmt.setString(2, hashedPassword);

            System.out.println("Ejecutando query: " + SQL_INSERT);
            rows = stmt.executeUpdate();
            System.out.println("Registros afectados: " + rows);
        } finally {
            Conexion.close(stmt);
            if (this.conexionTransaccional == null) {
                Conexion.close(conn);
            }
        }

        return rows;
    }

    // Update modificado para encriptar la contraseña antes de actualizarla en la base de datos
    public int update(Usuario usuario) throws SQLException, NoSuchAlgorithmException {
        Connection conn = null;
        PreparedStatement stmt = null;
        int rows = 0;

        try {
            conn = this.conexionTransaccional != null ? this.conexionTransaccional : Conexion.getConnection();
            stmt = conn.prepareStatement(SQL_UPDATE);
            stmt.setString(1, usuario.getUsername());

            // Encriptar la nueva contraseña antes de actualizarla en la base de datos
            String hashedPassword = hashPassword(usuario.getPassword());
            stmt.setString(2, hashedPassword);

            stmt.setInt(3, usuario.getId_usuario());

            System.out.println("Ejecutando query: " + SQL_UPDATE);
            rows = stmt.executeUpdate();
            System.out.println("Registros afectados: " + rows);
        } finally {
            Conexion.close(stmt);
            if (this.conexionTransaccional == null) {
                Conexion.close(conn);
            }
        }

        return rows;
    }

    public int delete(Usuario usuario) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        int rows = 0;

        try {
            conn = this.conexionTransaccional != null ? this.conexionTransaccional : Conexion.getConnection();
            stmt = conn.prepareStatement(SQL_DELETE);
            stmt.setInt(1, usuario.getId_usuario());

            System.out.println("Ejecutando query: " + SQL_DELETE);
            rows = stmt.executeUpdate();
            System.out.println("Registros afectados: " + rows);
        } finally {
            Conexion.close(stmt);
            if (this.conexionTransaccional == null) {
                Conexion.close(conn);
            }
        }

        return rows;
    }

    public boolean autenticarUsuario(Usuario Usuario) throws SQLException, NoSuchAlgorithmException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = this.conexionTransaccional != null ? this.conexionTransaccional : Conexion.getConnection();
            stmt = conn.prepareStatement(SQL_VALIDATE);
            stmt.setString(1, Usuario.getUsername());
            rs = stmt.executeQuery();

            if (rs.next()) {
                // Obtener la contraseña almacenada en la base de datos
                String storedPassword = rs.getString("password");

                // Encriptar la contraseña proporcionada y compararla con la almacenada
                String hashedPassword = hashPassword(Usuario.getPassword());

                return hashedPassword.equals(storedPassword);
            } else {
                // Si no se encuentra el usuario, la autenticación falla
                return false;
            }
        } finally {
            Conexion.close(rs);
            Conexion.close(stmt);
            if (this.conexionTransaccional == null) {
                Conexion.close(conn);
            }
        }
    }
}
