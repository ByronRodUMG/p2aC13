package edu.umg.test;

import edu.umg.datos.Conexion;
import edu.umg.datos.PersonaJDBC;
import edu.umg.domain.Persona;
import java.util.Scanner;

import java.sql.*;

public class ManejoPersonas {

    public static void desplegarPersonas(){
        Connection conexion = null;
        try {
            conexion = Conexion.getConnection();
            // El AutoCommit por default es true, lo pasamos a false
            if (conexion.getAutoCommit()) {
                conexion.setAutoCommit(false);
            }

            PersonaJDBC personaJdbc = new PersonaJDBC(conexion);
            // Vamos a listar las personas
            // utilizamos el metodo list de PersonaJDBC
            // que devuelve un arraylist de objetos persona
            // y lo recorremos con un for each
            for (Persona persona : personaJdbc.select()) {
                System.out.println("persona = " + persona);
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

        desplegarPersonas(); // Despliega la información de la base de datos de personas, bloque SELECT

        Scanner sc = new Scanner(System.in);
        // Definimos la variable conexion
        Connection conexion = null;
        try {
            conexion = Conexion.getConnection();
            // El autocommit por default es true, lo pasamos a false
            if (conexion.getAutoCommit()) {
                conexion.setAutoCommit(false);
            }

            PersonaJDBC personaJdbc = new PersonaJDBC(conexion);

            // Vamos a seleccionar a un registro, bloque SELECT_BY_ID
            Persona seleccionarPersona = new Persona();
            System.out.println("Ingrese el ID de la persona a seleccionar:");
            seleccionarPersona.setId_persona(sc.nextInt());
            seleccionarPersona = personaJdbc.selectPersona(seleccionarPersona);
            System.out.println("Se ha seleccionado a la persona: " + seleccionarPersona);
            conexion.commit(); // Queda permanente en la base de datos
            System.out.println("Se ha hecho commit de la transaccion.");

//            // Se inserta una persona, bloque INSERT
//            Persona nuevaPersona = new Persona();
//            nuevaPersona.setNombre("Carlos");
//            nuevaPersona.setApellido("Santos");
//            nuevaPersona.setEmail("newuser123@gmail.com");
//            nuevaPersona.setTelefono("123456789");
//            personaJdbc.insert(nuevaPersona);
//            conexion.commit(); // Queda permanente en la base de datos
//            System.out.println("Se ha hecho commit de la transaccion");

//            // Vamos a actalizar los datos, bloque UPDATE
//            Persona cambioPersona = new Persona();
//            cambioPersona.setId_persona(3);
//            cambioPersona.setNombre("Roberto");
//            cambioPersona.setApellido("Garcia");
//            cambioPersona.setEmail("robgarcia@gmail.com");
//            cambioPersona.setTelefono("934284952");
//            personaJdbc.update(cambioPersona);
//            conexion.commit(); // Queda permanente en la base de datos
//            System.out.println("Se ha hecho commit de la transaccion");

//            // Vamos a eliminar un registro, bloque DELETE
//            Persona eliminarPersona = new Persona();
//            eliminarPersona.setId_persona(3);
//            personaJdbc.delete(eliminarPersona);
//            conexion.commit(); // Queda permanente en la base de datos
//            System.out.println("Se ha hecho commit de la transaccion");


        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
            System.out.println("Entramos al rollback");
            try {
                conexion.rollback();
            } catch (SQLException ex1) {
                ex1.printStackTrace(System.out);
            }
        }
    }
}

