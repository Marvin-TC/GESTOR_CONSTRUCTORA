package org.example.views;
import org.example.dto.ProyectoTablaDto;
import org.example.dto.SolicitudTablaDto;
import org.example.models.MaterialModel;
import org.example.models.ProyectosModel;
import org.example.models.SolicitudMaterialModel;
import org.example.models.UsuarioModel;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class FormularioProyectos extends JFrame {
    private JTextField textNombreProyecto;
    private JTextField textFechaInicio;
    private JTextField textFechaFinal;
    private JComboBox comboEncargado;
    private JTabbedPane tabbedPane1;
    private JTextArea textAreaDescripcion;
    private JTable tableProyectos;
    private JTextField textField4;
    private JButton buscarButton;
    private JPanel panelMain;
    private JTextField textUbicacion;
    private JTextField textMetrosCuadrado;
    private JButton agregarNuevoButton;
    private JButton guardarProyectoButton;
    private JTextField textNombreD;
    private JTextField textUbicacionD;
    private JProgressBar progressBarAvance;
    private JTextField textFechaInicioD;
    private JTextField textFechaFinalD;
    private JTextField textFechaRealFinalD;
    private JTextField textEncargadoD;
    private JTextField textAreaConstruccionD;
    private JLabel labelAvance;
    private JTable tableMateriales;
    private JTextField textBuscarMaterial;
    private JButton btnBuscador;
    private JScrollPane scrol;
    private JTextField textNombreMaterial;
    private JTextField textUnidadMedida;
    private JTextField textPesoMedida;
    private JButton btnGuardarMaterial;
    private JButton btnEliminarMaterial;
    private JButton btnActualizarMaterial;
    private JPanel contenedorActualizacionMateriales;
    private JButton btnLimpiarCamposMaterial;
    private JPanel contenedorGuardarMateriales;
    private JButton btnLimpiarBusqueda;
    private JPanel SOLICITUDES;
    private JButton activasButton;
    private JButton rangoDeFechaButton;
    private JButton porMaterialButton;
    private JButton vencidasButton;
    private JTable tablaSolicitudes;
    private JScrollPane scrollPaneSolicitudes;

    JPopupMenu menu = new JPopupMenu();
    JMenuItem editarItem = new JMenuItem("Editar");
    JMenuItem eliminarItem = new JMenuItem("Eliminar");
    JMenuItem solicitarMaterial = new JMenuItem("Solicitar material");

    List<ProyectoTablaDto> listaProyectosActivos = new ArrayList<>();
    List<MaterialModel> listaMateriales = new ArrayList<>();
    List<SolicitudTablaDto> listaSolicitudes = new ArrayList<>();


    //items seleccionados de las tablas
    MaterialModel materialSeleccionado = new MaterialModel();


    public FormularioProyectos() throws ParseException {
        super("Gestor de Materiales para Proyectos"); // Título de la ventana
        setContentPane(panelMain);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null); // Centrar ventana
        cargarListadoDeProyectosActivos();
        cargarEncargadosProyecto();
        cargarListaMateriales();
        cargarListaSolicitudesMaterial();
        menu.add(editarItem);
        menu.add(eliminarItem);
        menu.add(solicitarMaterial);

        MaskFormatter mask = new MaskFormatter("####-##-##");
        JFormattedTextField textFechaInicio = new JFormattedTextField(mask);
        JFormattedTextField textFechaFinal  = new JFormattedTextField(mask);


        tableProyectos.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    mostrarMenu(e);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    mostrarMenu(e);
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) { // clic izquierdo
                    int row = tableProyectos.rowAtPoint(e.getPoint());
                    if (row >= 0) {
                        tableProyectos.setRowSelectionInterval(row, row);
                        if (e.getClickCount() == 1) {
                            ProyectoTablaDto proyecto = listaProyectosActivos.get(row);
                            int id = proyecto.getId();
                            setearDetallesProyecto((long)id);
                        }
                    }
                }
            }

            private void mostrarMenu(MouseEvent e) {
                int row = tableProyectos.rowAtPoint(e.getPoint());
                if (row >= 0 && row < tableProyectos.getRowCount()) {
                    tableProyectos.setRowSelectionInterval(row, row); // seleccionar fila bajo el cursor
                    menu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });

        tableMateriales.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                opcionesClickIzquierdo(e);
               }

            @Override
            public void mousePressed(MouseEvent e) {
                opcionesClickIzquierdo(e);
            }

            private void opcionesClickIzquierdo(MouseEvent e)
        {
            if (e.getButton() == MouseEvent.BUTTON1) { // clic izquierdo
                int row = tableMateriales.rowAtPoint(e.getPoint());
                if (row >= 0) {
                    tableMateriales.setRowSelectionInterval(row, row);
                    if (e.getClickCount() == 1) {
                        MaterialModel material = listaMateriales.get(row);
                        int id = material.getId();
                        setearDetallesMaterial(id);
                        contenedorActualizacionMateriales.setVisible(true);
                        btnGuardarMaterial.setEnabled(false);
                    }
                }
            }
        }
        });

        editarItem.addActionListener(e -> {
            int fila = tableProyectos.getSelectedRow();
            if (fila != -1) {
                Object id = tableProyectos.getValueAt(fila, 0);
                System.out.println("Editar fila con ID: " + id);
                // Aquí podrías abrir un formulario o actualizar la fila
            }
        });
        eliminarItem.addActionListener(e -> {
            int fila = tableProyectos.getSelectedRow();
            if (fila != -1) {
                Object id = tableProyectos.getValueAt(fila, 0);
                System.out.println("Eliminar fila con ID: " + id);
                // Confirmar y eliminar
            }
        });
        solicitarMaterial.addActionListener(e -> {
            int fila = tableProyectos.getSelectedRow();
            if (fila != -1) {
                Object id = tableProyectos.getValueAt(fila, 0);
                try {
                    ProyectoTablaDto proyecto = listaProyectosActivos.get(fila);
                    new FormularioSolicitudMaterial(proyecto).setVisible(true); // pasar el objeto
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "No se pudo abrir el formulario");
                }
            }
        });

        guardarProyectoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarNuevoProyecto();
            }
        });
        btnGuardarMaterial.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarNuevoMaterial();
            }
        });
        btnLimpiarCamposMaterial.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limpiarFormularioMateriales();
                btnGuardarMaterial.setEnabled(true);
                contenedorActualizacionMateriales.setVisible(false);
                materialSeleccionado=null;
            }
        });
        btnEliminarMaterial.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarMaterial();
            }
        });
        btnActualizarMaterial.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarMaterial();
            }
        });
        btnBuscador.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cargarListaMaterialesBuscador(textBuscarMaterial.getText().trim());
            }
        });
        btnLimpiarBusqueda.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cargarListaMateriales();
            }
        });
    }


    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new FormularioProyectos().setVisible(true);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        });
    }


    //CARGAR LISTAS
    private void cargarListadoDeProyectosActivos() {
        try {
            ProyectosModel proyectosModel = new ProyectosModel();
            listaProyectosActivos.clear();
            listaProyectosActivos = proyectosModel.listarActivos();
            DefaultTableModel modelo = new DefaultTableModel(
                    new String[]{"ID", "Nombre","Encargado", "Fecha Inicio","Avance"}, 0
            );
            for (ProyectoTablaDto a : listaProyectosActivos) {
                modelo.addRow(new Object[]{a.getId(), a.getNombre(), a.getEncargado(), a.getFechaInicio(), a.getAvance()});
            }
            tableProyectos.setModel(modelo);
            int totalWidth = tableProyectos.getPreferredScrollableViewportSize().width;
            tableProyectos.getColumnModel().getColumn(0).setPreferredWidth((int) (totalWidth * 0.10)); // ID
            tableProyectos.getColumnModel().getColumn(1).setPreferredWidth((int) (totalWidth * 0.35)); // Nombre
            tableProyectos.getColumnModel().getColumn(2).setPreferredWidth((int) (totalWidth * 0.35)); // Fecha
            tableProyectos.getColumnModel().getColumn(3).setPreferredWidth((int) (totalWidth * 0.10)); // Responsable
            tableProyectos.getColumnModel().getColumn(4).setPreferredWidth((int) (totalWidth * 0.10)); // Avance
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void cargarListaMateriales() {
        try {
            listaMateriales.clear();
            MaterialModel model = new MaterialModel();
            listaMateriales = model.listarTodos();
            DefaultTableModel modelo = new DefaultTableModel(
                    new String[]{"ID", "Nombre","Medida", "Peso (lbs)"}, 0
            );
            for (MaterialModel a : listaMateriales) {
                modelo.addRow(new Object[]{a.getId(), a.getNombre(), a.getUnidad_medida(), a.getPeso()});
            }
            tableMateriales.setModel(modelo);
            int totalWidth = tableMateriales.getPreferredScrollableViewportSize().width;
            tableMateriales.getColumnModel().getColumn(0).setPreferredWidth((int) (totalWidth * 0.05)); // ID
            tableMateriales.getColumnModel().getColumn(1).setPreferredWidth((int) (totalWidth * 0.65)); // Nombre
            tableMateriales.getColumnModel().getColumn(2).setPreferredWidth((int) (totalWidth * 0.15)); // medida
            tableMateriales.getColumnModel().getColumn(3).setPreferredWidth((int) (totalWidth * 0.15)); // peso
            DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
            rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
            tableMateriales.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void cargarListaMaterialesBuscador(String textBuscado){
        try {
            listaMateriales.clear();
            MaterialModel model = new MaterialModel();
            listaMateriales = model.buscarPorNombreLike(textBuscado);
            DefaultTableModel modelo = new DefaultTableModel(
                    new String[]{"ID", "Nombre","Medida", "Peso (lbs)"}, 0
            );
            for (MaterialModel a : listaMateriales) {
                modelo.addRow(new Object[]{a.getId(), a.getNombre(), a.getUnidad_medida(), a.getPeso()});
            }
            tableMateriales.setModel(modelo);
            int totalWidth = tableMateriales.getPreferredScrollableViewportSize().width;
            tableMateriales.getColumnModel().getColumn(0).setPreferredWidth((int) (totalWidth * 0.05)); // ID
            tableMateriales.getColumnModel().getColumn(1).setPreferredWidth((int) (totalWidth * 0.65)); // Nombre
            tableMateriales.getColumnModel().getColumn(2).setPreferredWidth((int) (totalWidth * 0.15)); // medida
            tableMateriales.getColumnModel().getColumn(3).setPreferredWidth((int) (totalWidth * 0.15)); // peso
            DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
            rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
            tableMateriales.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void cargarListaSolicitudesMaterial(){
        try {
            listaSolicitudes.clear();
            SolicitudMaterialModel solicitudMaterialModel = new SolicitudMaterialModel();
            listaSolicitudes = solicitudMaterialModel.listarSolicitudesPendientesNoTratadas();
            DefaultTableModel modelo = new DefaultTableModel(
                    new String[]{"ID","Solicitado para el", "Cantidad","Medida", "Material","Descripción","Solicitado por"}, 0
            );
            for (SolicitudTablaDto a : listaSolicitudes) {
                modelo.addRow(new Object[]{a.getId(),a.getFecha_solicitud(),a.getCantidad_solicitada(),
                        a.getUnidad_medida(),a.getNombre(),a.getComentario(),a.getNombreCompleto()});
            }
            tablaSolicitudes.setModel(modelo);
            int totalWidth = tableMateriales.getPreferredScrollableViewportSize().width;
            tablaSolicitudes.getColumnModel().getColumn(0).setPreferredWidth((int) (totalWidth * 0.05)); // ID
            tablaSolicitudes.getColumnModel().getColumn(1).setPreferredWidth((int) (totalWidth * 0.15)); // Fecha
            tablaSolicitudes.getColumnModel().getColumn(2).setPreferredWidth((int) (totalWidth * 0.10)); // Cantidad
            tablaSolicitudes.getColumnModel().getColumn(3).setPreferredWidth((int) (totalWidth * 0.10)); // Medida
            tablaSolicitudes.getColumnModel().getColumn(4).setPreferredWidth((int) (totalWidth * 0.30)); // Material
            tablaSolicitudes.getColumnModel().getColumn(5).setPreferredWidth((int) (totalWidth * 0.20)); // Comentario
            tablaSolicitudes.getColumnModel().getColumn(6).setPreferredWidth((int) (totalWidth * 0.10)); // Solicitado por
            DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
            leftRenderer.setHorizontalAlignment(SwingConstants.LEFT);
            tablaSolicitudes.getColumnModel().getColumn(2).setCellRenderer(leftRenderer); // Cantidad alineada
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //SETEAR INFORMACION
    private void setearDetallesProyecto(Long id) {
        try {

            ProyectosModel proyect = new ProyectosModel();
            ProyectosModel proyecto = proyect.buscarPorId(id);
            System.out.println(proyecto.toString());
            labelAvance.setText("Avance: "+proyecto.getAvancePorcentaje()+ " %");
            progressBarAvance.setValue(proyecto.getAvancePorcentaje().intValue());
            textNombreD.setText(proyecto.getNombre());
            textUbicacionD.setText(proyecto.getDireccion());
            textFechaInicioD.setText(proyecto.getFechaInicio() != null ? proyecto.getFechaInicio().toString() : "");
            textFechaFinalD.setText(proyecto.getFechaFinal() != null ? proyecto.getFechaFinal().toString() : "");
            textFechaRealFinalD.setText(proyecto.getFechaFinalReal() != null ? proyecto.getFechaFinalReal().toString() : "");
            textAreaConstruccionD.setText(proyecto.getAreaConstruccion().toString());

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void setearDetallesMaterial(int id) {
        try {
            MaterialModel modelo = new MaterialModel();
            materialSeleccionado = modelo.buscarPorId(id);

            if (materialSeleccionado != null) {
                textNombreMaterial.setText(materialSeleccionado.getNombre());
                textUnidadMedida.setText(materialSeleccionado.getUnidad_medida());

                BigDecimal peso = materialSeleccionado.getPeso();
                textPesoMedida.setText(peso != null ? peso.toString() : "0.00");
            } else {
                JOptionPane.showMessageDialog(null, "Material no encontrado.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al buscar el material: " + e.getMessage());
        }
    }


    //CARGAR COMBOBOX
    private void cargarEncargadosProyecto() {
        try {
            UsuarioModel user = new UsuarioModel();
            List<UsuarioModel> lista = user.listarUsuariosPorRol(2);
            DefaultComboBoxModel<UsuarioModel> model = new DefaultComboBoxModel<>();
            for (UsuarioModel usuario : lista) {
                model.addElement(usuario);
            }
            comboEncargado.setModel(model);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    //CRUD
        //proyecto
    private void guardarNuevoProyecto() {
        try {
            UsuarioModel seleccionado = (UsuarioModel) comboEncargado.getSelectedItem();
            if (seleccionado == null) {
                throw new IllegalArgumentException("Debe seleccionar un encargado.");
            }
            System.out.println(seleccionado.getId());

            ProyectosModel nuevoProyecto = new ProyectosModel();
            nuevoProyecto.setNombre(textNombreProyecto.getText().trim());
            nuevoProyecto.setDireccion(textUbicacion.getText().trim());
            // Validar y parsear fechas
            String fechaInicio = textFechaInicio.getText().trim();
            String fechaFinal = textFechaFinal.getText().trim();
            LocalDate fechaI = LocalDate.parse(fechaInicio);
            LocalDate fechaF = LocalDate.parse(fechaFinal);
            if (fechaF.isBefore(fechaI)) {
                throw new IllegalArgumentException("La fecha final no puede ser anterior a la fecha de inicio.");
            }
            nuevoProyecto.setFechaInicio(fechaI);
            nuevoProyecto.setFechaFinal(fechaF);
            nuevoProyecto.setEncargadoPrincipal(seleccionado.getId());

            // Validar número
            String metrosTexto = textMetrosCuadrado.getText().trim();
            if (metrosTexto.isEmpty()) {
                throw new IllegalArgumentException("Debe ingresar el área de construcción.");
            }
            BigDecimal area =new BigDecimal(metrosTexto);
            nuevoProyecto.setAreaConstruccion(area);

            nuevoProyecto.setDescripcion(textAreaDescripcion.getText().trim());

            // Guardar
            nuevoProyecto.guardar(nuevoProyecto);
            cargarListadoDeProyectosActivos();
            limpiarFormularioproyecto();
            JOptionPane.showMessageDialog(this, "Proyecto guardado correctamente");

        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Formato de fecha incorrecto. Use yyyy-MM-dd.");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Área de construcción inválida. Solo se permiten números.");
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al guardar el proyecto: " + e.getMessage());
            e.printStackTrace();
        }
    }
        //materialModel
    private void guardarNuevoMaterial() {
        try {
            MaterialModel material = new MaterialModel();
            material.setNombre(textNombreMaterial.getText().trim());
            material.setUnidad_medida(textUnidadMedida.getText().trim());
            String pesoMaterial = textPesoMedida.getText().trim();
            if (pesoMaterial.isEmpty()) {
                throw new IllegalArgumentException("Debe ingresar el peso del material según su unidad de medida.");
            }
            BigDecimal peso = new BigDecimal(pesoMaterial);
            material.setPeso(peso);
            // Guardar
            material.guardar(material);
            cargarListaMateriales();
            limpiarFormularioMateriales();
            JOptionPane.showMessageDialog(this, "material guardado correctamente.");
        }  catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Peso de los materiales inválida. Solo se permiten números.");
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al guardar el material: " + e.getMessage());
            e.printStackTrace();
        }
    }
    private void eliminarMaterial() {
        try {
            if (materialSeleccionado == null) {
                throw new IllegalArgumentException("Debe seleccionar un material para eliminarlo.");
            }
            materialSeleccionado.eliminarLogicamentePorId(materialSeleccionado.getId());
            JOptionPane.showMessageDialog(this,"Material Eliminado");
            cargarListaMateriales();
            limpiarFormularioMateriales();
            btnGuardarMaterial.setEnabled(true);
            materialSeleccionado=null;
            contenedorActualizacionMateriales.setVisible(false);
        }catch (Exception e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        }

    }
    private void actualizarMaterial() {
        try {
            if (materialSeleccionado == null) {
                throw new IllegalArgumentException("Debe seleccionar un material.");
            }
            materialSeleccionado.setNombre(textNombreMaterial.getText().trim());
            materialSeleccionado.setUnidad_medida(textUnidadMedida.getText().trim());
            String pesoMaterial = textPesoMedida.getText().trim();
            if (pesoMaterial.isEmpty()) {
                throw new IllegalArgumentException("Debe ingresar el peso del material según su unidad de medida.");
            }
            BigDecimal peso = new BigDecimal(pesoMaterial);
            materialSeleccionado.setPeso(peso);
            // actualizar
            materialSeleccionado.actualizar(materialSeleccionado);
            cargarListaMateriales();
            limpiarFormularioMateriales();
            btnGuardarMaterial.setEnabled(true);
            contenedorActualizacionMateriales.setVisible(false);
            JOptionPane.showMessageDialog(this, "material Actualizado correctamente.");
        }  catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Peso de los materiales inválida. Solo se permiten números.");
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al guardar el material: " + e.getMessage());
            e.printStackTrace();
        }
    }


    //LIMPIAR CAJAS DE TEXTO, ETIQUETAS TABLAS ETC
    private void limpiarFormularioMateriales() {
        textNombreMaterial.setText("");
        textPesoMedida.setText("");
        textUnidadMedida.setText("");
    }
    private void limpiarFormularioproyecto() {
        textNombreProyecto.setText("");
        textUbicacion.setText("");
        textFechaInicio.setText("");
        textFechaFinal.setText("");
        textAreaDescripcion.setText("");
        textMetrosCuadrado.setText("");
    }

}
