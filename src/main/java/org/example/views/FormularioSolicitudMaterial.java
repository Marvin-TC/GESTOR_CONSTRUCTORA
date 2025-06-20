package org.example.views;
import org.example.dto.ProyectoTablaDto;
import org.example.models.MaterialModel;
import org.example.models.SolicitudMaterialModel;
import org.example.models.UsuarioModel;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FormularioSolicitudMaterial extends JFrame {
    private JComboBox ComboMateriales;
    private JPanel PanelMain;
    private JTextField textCantidad;
    private JLabel label;
    private JTextField textFecha;
    private JTextArea textAreaComentario;
    private JComboBox ComboEncargado;
    private JButton guardarButton;
    private JLabel textDescripcion;
    private List<MaterialModel> listMateriales = new ArrayList<>();
    private ProyectoTablaDto proyecto;

    public FormularioSolicitudMaterial(ProyectoTablaDto proyectosModel) throws ParseException {
        this.proyecto=proyectosModel;
        setTitle("Solicitar Materiales");
        setSize(400, 500);
        setContentPane(PanelMain);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        cargarEncargadoSolicitud();
        cargarListaMateriales();

        JTextComponent editor = (JTextComponent) ComboMateriales.getEditor().getEditorComponent();
        editor.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String texto = editor.getText();
                ComboMateriales.removeAllItems();
                for (MaterialModel m : listMateriales) {
                    if (m.getNombre().toLowerCase().contains(texto.toLowerCase())) {
                        ComboMateriales.addItem(m);
                    }
                }
                editor.setText(texto); // Mantener lo que ya escribió
                ComboMateriales.setPopupVisible(true); // Mostrar sugerencias
            }
        });


        guardarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                guardarSolicitud();
            }
        });

        ComboMateriales.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MaterialModel materialSeleccionado = (MaterialModel) ComboMateriales.getSelectedItem();
                if (materialSeleccionado != null) {
                    textDescripcion.setText("medida: "+materialSeleccionado.getUnidad_medida()+" peso en libras: "+materialSeleccionado.getPeso());
                }
            }
        });

    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new FormularioSolicitudMaterial(null).setVisible(true);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void llenarComboBox()
    {
        MaterialModel materiales = new MaterialModel();
        listMateriales.clear();
        try {
            listMateriales = materiales.listarTodos();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private void cargarEncargadoSolicitud() {
        try {
            UsuarioModel user = new UsuarioModel();
            //2 ingeniero civil, 3 arquitecto, 4, residente de obra, 5 maestro de obra
            List<UsuarioModel> lista = user.listarUsuariosPorRolesConstruccion(2,3,4,5);
            DefaultComboBoxModel<UsuarioModel> model = new DefaultComboBoxModel<>();
            for (UsuarioModel usuario : lista) {
                model.addElement(usuario);
            }
            ComboEncargado.setModel(model);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void cargarListaMateriales() {
        try {
            listMateriales.clear();
            MaterialModel materialModel = new MaterialModel();
            listMateriales = materialModel.listarTodos();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void guardarSolicitud()
    {
        try {
            //guardar
            SolicitudMaterialModel solicitudMaterialModel = new SolicitudMaterialModel();
            solicitudMaterialModel.setProyectoId(proyecto.getId());
            MaterialModel material = (MaterialModel) ComboMateriales.getSelectedItem();
            UsuarioModel usuario = (UsuarioModel) ComboEncargado.getSelectedItem();
            solicitudMaterialModel.setMaterialId(material.getId());
            solicitudMaterialModel.setSolicitadoPor(usuario.getId());
            String cantidad = textCantidad.getText().trim();
            if (cantidad.isEmpty()) {
                throw new IllegalArgumentException("Debe ingresar la cantidad de material.");
            }
            BigDecimal peso = new BigDecimal(cantidad);
            solicitudMaterialModel.setCantidadSolicitada(peso);
            String fechaSText = textFecha.getText().trim();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false);
            try {
                Date fechaSolicitud = sdf.parse(fechaSText);
                Date fechaHoy = new Date();
                if (fechaSolicitud.before(fechaHoy)) {
                    throw new IllegalArgumentException("La fecha de solicitud no puede ser anterior a hoy.");
                }
                solicitudMaterialModel.setFechaSolicitud(fechaSolicitud);

            } catch (ParseException e) {
                throw new IllegalArgumentException("La fecha ingresada no tiene un formato válido (dd/MM/yyyy).");
            }
            solicitudMaterialModel.setComentario(textAreaComentario.getText());
            JOptionPane.showMessageDialog(this, "Solicitud de materail registrado.");
            solicitudMaterialModel.guardar(solicitudMaterialModel);
            dispose();
        }  catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "cantidad de los materiales inválida. Solo se permiten números.");
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al guardar la solicitud: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
