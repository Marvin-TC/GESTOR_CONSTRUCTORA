package org.example.models;
import org.example.JDBCUtil;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UsuarioModel {
    private int id;
    private String nombres;
    private String apellidos;
    private String dpi;
    private String telefono;
    private String correo;
    private String contrasena;
    private LocalDate fechaNacimiento;
    private BigDecimal salario;
    private LocalDate fechaInicioLaburo;
    private Integer rolId;
    private String departamento;
    private Boolean activo;
    private String direccion;

    // Constructor vacío
    public UsuarioModel() {
    }

    public UsuarioModel(int id, String nombres, String apellidos, String dpi, String telefono, String correo, String contrasena, LocalDate fechaNacimiento, BigDecimal salario, LocalDate fechaInicioLaburo, Integer rolId, String departamento, Boolean activo, String direccion) {
        this.id = id;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.dpi = dpi;
        this.telefono = telefono;
        this.correo = correo;
        this.contrasena = contrasena;
        this.fechaNacimiento = fechaNacimiento;
        this.salario = salario;
        this.fechaInicioLaburo = fechaInicioLaburo;
        this.rolId = rolId;
        this.departamento = departamento;
        this.activo = activo;
        this.direccion = direccion;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getDpi() {
        return dpi;
    }

    public void setDpi(String dpi) {
        this.dpi = dpi;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public BigDecimal getSalario() {
        return salario;
    }

    public void setSalario(BigDecimal salario) {
        this.salario = salario;
    }

    public LocalDate getFechaInicioLaburo() {
        return fechaInicioLaburo;
    }

    public void setFechaInicioLaburo(LocalDate fechaInicioLaburo) {
        this.fechaInicioLaburo = fechaInicioLaburo;
    }

    public Integer getRolId() {
        return rolId;
    }

    public void setRolId(Integer rolId) {
        this.rolId = rolId;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    @Override
    public String toString() {
        return nombres + " " + apellidos; // Esto es lo que se muestra en el JComboBox
    }

    ///  metodos para accesar a la base de datos
    private static final String SQL_INSERTAR = """
        INSERT INTO usuarios (
            nombres, apellidos, dpi, telefono, correo, contrasena,
            fecha_nacimiento, salario, fecha_inicio_laburo, rol_id,
            departamento, activo, direccion
        ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
    """;

    private static final String SQL_LISTAR_POR_ROL_INGENIERO = "SELECT * FROM usuarios WHERE rol_id = ?";
    private static final String SQL_LISTAR_POR_ROLES_CONSTRUCCION = "SELECT * FROM usuarios WHERE rol_id IN (?, ?, ?, ?)";
    private static final String SQL_LISTAR_TODOS = "SELECT * FROM usuarios";
    private static final String SQL_BUSCAR_POR_ID = "SELECT * FROM usuarios WHERE id = ?";
    private static final String SQL_LOGIN = "SELECT * FROM usuarios WHERE correo = ? AND contrasena = ?";


    public List<UsuarioModel> listarUsuariosPorRolesConstruccion(int rolId1,int rolId2,int rolId3,int rolId4) throws Exception {
        List<UsuarioModel> lista = new ArrayList<>();
        try (Connection con = JDBCUtil.getConection();
             PreparedStatement ps = con.prepareStatement(SQL_LISTAR_POR_ROLES_CONSTRUCCION)) {
            ps.setInt(1, rolId1);
            ps.setInt(2, rolId2);
            ps.setInt(3, rolId3);
            ps.setInt(4, rolId4);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearUsuario(rs));
                }
            }
        }
        return lista;
    }


    public UsuarioModel logearUsuario(String correo, String contrasena) throws Exception {
        UsuarioModel usuario = null;

        try (Connection con = JDBCUtil.getConection();
             PreparedStatement ps = con.prepareStatement(SQL_LOGIN)) {

            ps.setString(1, correo.trim());
            ps.setString(2, contrasena.trim());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    usuario = mapearUsuario(rs);
                }
            }
        }
        return usuario;
    }

    public void guardar(UsuarioModel u) throws Exception {
        try (Connection con = JDBCUtil.getConection();
             PreparedStatement ps = con.prepareStatement(SQL_INSERTAR)) {

            ps.setString(1, u.getNombres());
            ps.setString(2, u.getApellidos());
            ps.setString(3, u.getDpi());
            ps.setString(4, u.getTelefono());
            ps.setString(5, u.getCorreo());
            ps.setString(6, u.getContrasena());
            ps.setDate(7, u.getFechaNacimiento() != null ? Date.valueOf(u.getFechaNacimiento()) : null);
            ps.setBigDecimal(8, u.getSalario());
            ps.setDate(9, u.getFechaInicioLaburo() != null ? Date.valueOf(u.getFechaInicioLaburo()) : null);
            ps.setInt(10, u.getRolId());
            ps.setString(11, u.getDepartamento());
            ps.setObject(12, u.getActivo(), Types.BOOLEAN);
            ps.setString(13, u.getDireccion());
            ps.executeUpdate();
        }
    }

    public List<UsuarioModel> listarTodos() throws Exception {
        List<UsuarioModel> lista = new ArrayList<>();
        try (Connection con = JDBCUtil.getConection();
             PreparedStatement ps = con.prepareStatement(SQL_LISTAR_TODOS);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearUsuario(rs));
            }
        }
        return lista;
    }

    public List<UsuarioModel> listarUsuariosPorRol(int rolId) throws Exception {
        List<UsuarioModel> lista = new ArrayList<>();
        try (Connection con = JDBCUtil.getConection();
             PreparedStatement ps = con.prepareStatement(SQL_LISTAR_POR_ROL_INGENIERO)) {
            ps.setInt(1, rolId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearUsuario(rs));
                }
            }
        }
        return lista;
    }

    public UsuarioModel buscarPorId(Long id) throws Exception {
        try (Connection con = JDBCUtil.getConection();
             PreparedStatement ps = con.prepareStatement(SQL_BUSCAR_POR_ID)) {

            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearUsuario(rs);
                }
            }
        }
        return null;
    }

    private UsuarioModel mapearUsuario(ResultSet rs) throws Exception {
        UsuarioModel u = new UsuarioModel();
        u.setId(rs.getInt("id"));
        u.setNombres(rs.getString("nombres"));
        u.setApellidos(rs.getString("apellidos"));
        u.setDpi(rs.getString("dpi"));
        u.setTelefono(rs.getString("telefono"));
        u.setCorreo(rs.getString("correo"));
        u.setContrasena(rs.getString("contrasena"));
        Date fn = rs.getDate("fecha_nacimiento");
        if (fn != null) u.setFechaNacimiento(fn.toLocalDate());
        u.setSalario(rs.getBigDecimal("salario"));
        Date fi = rs.getDate("fecha_inicio_laburo");
        if (fi != null) u.setFechaInicioLaburo(fi.toLocalDate());
        u.setRolId(rs.getInt("rol_id"));
        u.setDepartamento(rs.getString("departamento"));
        boolean activo = rs.getBoolean("activo");
        if (rs.wasNull()) {
            u.setActivo(null);
        } else {
            u.setActivo(activo);
        }
        u.setDireccion(rs.getString("direccion"));
        return u;
    }
}