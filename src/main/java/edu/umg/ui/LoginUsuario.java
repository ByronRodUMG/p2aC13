package edu.umg.ui;

import edu.umg.datos.UsuarioJDBC;
import edu.umg.domain.Usuario;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

// Por falta de tiempo y para evitar utilizar el editor de formularios de IntelliJ,
// la mayor parte del código de esta clase de UI fue generado por ChatGPT y Copilot
// con modificaciones mías. Esta parte del programa es ejecutable desde
// main.java.
// El resto del programa, fue escrito por mí con base en lo investigado.
// Todas las funciones de las clases DAO funcionan correctamente en las
// clases del package test (Las clases de Manejo), como la encriptación y validación
// de contraseñas.
public class LoginUsuario extends JFrame {
    private JLabel label1;
    private JLabel label2;
    private JLabel label3;
    private JLabel label4;
    private JLabel label5;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel resultLabel;

    public LoginUsuario() {
        setTitle("Login de Usuario");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(8, 2));

        label1 = new JLabel("Usuarios precargados:");
        label2 = new JLabel("User:       Password:");
        label3 = new JLabel("Miranda  passwabc");
        label4 = new JLabel("Alma        passwjkl");
        label5 = new JLabel("Joelix      passwxyz");

        usernameField = new JTextField();
        JLabel usernameLabel = new JLabel("Usuario:");
        JLabel passwordLabel = new JLabel("Contraseña:");
        passwordField = new JPasswordField();
        loginButton = new JButton("Iniciar sesión");
        resultLabel = new JLabel();

        panel.add(label1);
        panel.add(new JLabel()); // Espacio en blanco
        panel.add(label2);
        panel.add(new JLabel()); // Espacio en blanco
        panel.add(label3);
        panel.add(new JLabel()); // Espacio en blanco
        panel.add(label4);
        panel.add(new JLabel()); // Espacio en blanco
        panel.add(label5);
        panel.add(new JLabel()); // Espacio en blanco
        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(loginButton);
        panel.add(resultLabel);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Usuario check = new Usuario();
                check.setUsername(usernameField.getText());
                check.setPassword(new String(passwordField.getPassword()));

                UsuarioJDBC usuarioJDBC = new UsuarioJDBC();
                try {
                    boolean isAuthenticated = usuarioJDBC.autenticarUsuario(check);
                    if (isAuthenticated) {
                        resultLabel.setText("Inicio de sesión exitoso.");
                    } else {
                        resultLabel.setText("Inicio de sesión fallido.");
                    }
                } catch (SQLException | NoSuchAlgorithmException ex) {
                    ex.printStackTrace();
                }
            }
        });

        add(panel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                LoginUsuario frame = new LoginUsuario();
                frame.setVisible(true);
            }
        });
    }
}