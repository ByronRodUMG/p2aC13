package edu.umg.test;

import edu.umg.datos.Conexion;
import edu.umg.datos.UsuarioJDBC;
import edu.umg.domain.Usuario;

import java.security.*; // Para el manejo de contraseñas
import java.sql.*;
import java.util.Scanner;

public class ManejoUsuarios {

    public static void desplegarUsuarios() {
        Connection conexion = null;
        try {
            conexion = Conexion.getConnection();
            // Pasando el AutoCommit a false
            if (conexion.getAutoCommit()) {
                conexion.setAutoCommit(false);
            }

            UsuarioJDBC usuarioJdbc = new UsuarioJDBC(conexion);
            // Creando una lista de usuarios
            // utilizamos el metodo list de UsuarioJDBC
            for (Usuario usuario : usuarioJdbc.select()) {
                System.out.println("usuario = " + usuario);
            }
            conexion.commit(); // Queda permanente en la base de datos
            System.out.println("Se ha hecho commit de la transaccion.");

        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
            System.out.println("Entramos al rollback.");
            try {
                conexion.rollback();
            } catch (SQLException ex1) {
                ex1.printStackTrace(System.out);
            }
        }
    }

    public static void main(String[] args) {

        desplegarUsuarios(); // Despliega la información de la base de datos de usuarios, bloque SELECT

        Scanner sc = new Scanner(System.in);
        // Definimos la variable conexion
        Connection conexion = null;
        try {
            conexion = Conexion.getConnection();
            // El autocommit por default es true, lo pasamos a false
            if (conexion.getAutoCommit()) {
                conexion.setAutoCommit(false);
            }

            UsuarioJDBC usuarioJdbc = new UsuarioJDBC(conexion);

//            // Vamos a seleccionar a un registro, bloque SELECT_BY_ID
//            Usuario seleccionarUsuario = new Usuario();
//            System.out.println("Ingrese el ID del usuario a seleccionar:");
//            seleccionarUsuario.setId_usuario(sc.nextInt());
//            seleccionarUsuario = usuarioJdbc.selectUsuario(seleccionarUsuario);
//            System.out.println("Usuario seleccionado: " + seleccionarUsuario);
//            conexion.commit(); // Queda permanente en la base de datos
//            System.out.println("Se ha hecho commit de la transaccion.");

//            // Vamos a insertar un registro, bloque INSERT
//            Usuario insertarUsuario = new Usuario();
//            System.out.println("Ingrese el nombre del usuario a insertar:");
//            insertarUsuario.setUsername(sc.next());
//            System.out.println("Ingrese la contraseña del usuario a insertar:");
//            insertarUsuario.setPassword(sc.next());
//            usuarioJdbc.insert(insertarUsuario);
//            conexion.commit(); // Queda permanente en la base de datos
//            System.out.println("Se ha hecho commit de la transaccion.");

//            // Vamos a actualizar un registro, bloque UPDATE
//            Usuario actualizarUsuario = new Usuario();
//            System.out.println("Ingrese el ID del usuario a actualizar:");
//            actualizarUsuario.setId_usuario(sc.nextInt());
//            System.out.println("Ingrese el nuevo nombre del usuario:");
//            actualizarUsuario.setUsername(sc.next());
//            System.out.println("Ingrese la nueva contraseña del usuario:");
//            actualizarUsuario.setPassword(sc.next());
//            usuarioJdbc.update(actualizarUsuario);
//            conexion.commit(); // Queda permanente en la base de datos
//            System.out.println("Se ha hecho commit de la transaccion.");

            // Vamos a validar un usuario, bloque VALIDATE
            // Actualmente hay tres usuarios
            // username = "Miranda" y password = "passwabc"
            // username = "Alma" y password = "passwjkl"
            // username = "Joelix" y password = "passwxyz"
            Usuario validarUsuario = new Usuario();
            System.out.println("Ingrese el nombre del usuario a validar:");
            validarUsuario.setUsername(sc.next());
            System.out.println("Ingrese la contraseña del usuario a validar:");
            validarUsuario.setPassword(sc.next());
            boolean esValido = usuarioJdbc.autenticarUsuario(validarUsuario);
            if (esValido) {
                System.out.println("El usuario es válido.");
            } else {
                System.out.println("El usuario es inválido.");
            }

//            // Vamos a eliminar un registro, bloque DELETE
//            Usuario eliminarUsuario = new Usuario();
//            System.out.println("Ingrese el ID del usuario a eliminar:");
//            eliminarUsuario.setId_usuario(sc.nextInt());
//            usuarioJdbc.delete(eliminarUsuario);
//            conexion.commit(); // Queda permanente en la base de datos
//            System.out.println("Se ha hecho commit de la transaccion.");

        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
            System.out.println("Entramos al rollback");
            try {
                conexion.rollback();
            } catch (SQLException ex1) {
                ex1.printStackTrace(System.out);
            }
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
