package edu.umg.ui;

import edu.umg.datos.UsuarioJDBC;
import edu.umg.domain.Usuario;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class UsuarioSwingUI extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel resultLabel;

    public UsuarioSwingUI() {
        setTitle("Login de Usuario");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));

        JLabel usernameLabel = new JLabel("Usuario:");
        usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Contraseña:");
        passwordField = new JPasswordField();
        loginButton = new JButton("Iniciar sesión");
        resultLabel = new JLabel();

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
                UsuarioSwingUI frame = new UsuarioSwingUI();
                frame.setVisible(true);
            }
        });
    }
}
