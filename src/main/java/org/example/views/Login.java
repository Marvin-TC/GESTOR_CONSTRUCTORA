package org.example.views;

import org.example.models.MaterialModel;
import org.example.models.UsuarioModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.text.ParseException;

public class Login extends JFrame {
    private JPanel panelMain;
    private JTextField textUsuarios;
    private JTextField textContraseña;
    private JButton loginButton;

    public Login() throws ParseException {
        super("login"); // Título de la ventana
        setContentPane(panelMain);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);
        setLocationRelativeTo(null); // Centrar ventana
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!textUsuarios.getText().isEmpty())
                {
                    if (!textContraseña.getText().isEmpty())
                    {
                        try {
                            UsuarioModel modelo = new UsuarioModel();
                            UsuarioModel usuarioLogeado = modelo.logearUsuario(textUsuarios.getText(), textContraseña.getText());
                            if (usuarioLogeado != null) {
                                FormularioProyectos ventanaPrincipal = new FormularioProyectos();
                                ventanaPrincipal.setVisible(true);
                            } else {
                                JOptionPane.showMessageDialog(null, "Correo o contraseña incorrectos");
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(null, "Error al iniciar sesión: " + ex.getMessage());
                        }



                    }else{JOptionPane.showMessageDialog(null, "ingrese su contraseña");}
                }else{JOptionPane.showMessageDialog(null, "Ingrese su correo");}
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new Login().setVisible(true);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
