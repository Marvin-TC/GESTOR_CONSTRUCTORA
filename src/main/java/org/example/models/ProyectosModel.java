package org.example.models;

import org.example.JDBCUtil;
import org.example.dto.ProyectoTablaDto;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProyectosModel {
    private Long id;
    private String nombre;
    private String direccion;
    private LocalDate fechaInicio;
    private LocalDate fechaFinal;
    private LocalDate fechaFinalReal;
    private int encargadoPrincipal;
    private BigDecimal costoProyecto;
    private BigDecimal areaConstruccion;
    private BigDecimal avancePorcentaje;
    private boolean finalizacionCompleta;
    private String estado; // ENUM: finalizado, inconcluso, pausado, terminado, cancelado por el cliente, iniciado
    private String descripcion;

    public ProyectosModel() {}

    public ProyectosModel(Long id, String nombre, String direccion, LocalDate fechaInicio, LocalDate fechaFinal, LocalDate fechaFinalReal, int encargadoPrincipal, BigDecimal costoProyecto, BigDecimal areaConstruccion, BigDecimal avancePorcentaje, boolean finalizacionCompleta, String estado, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.fechaInicio = fechaInicio;
        this.fechaFinal = fechaFinal;
        this.fechaFinalReal = fechaFinalReal;
        this.encargadoPrincipal = encargadoPrincipal;
        this.costoProyecto = costoProyecto;
        this.areaConstruccion = areaConstruccion;
        this.avancePorcentaje = avancePorcentaje;
        this.finalizacionCompleta = finalizacionCompleta;
        this.estado = estado;
        this.descripcion = descripcion;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public LocalDate getFechaFinal() {
        return fechaFinal;
    }

    public LocalDate getFechaFinalReal() {
        return fechaFinalReal;
    }

    public int getEncargadoPrincipal() {
        return encargadoPrincipal;
    }

    public BigDecimal getCostoProyecto() {
        return costoProyecto;
    }

    public BigDecimal getAreaConstruccion() {
        return areaConstruccion;
    }

    public BigDecimal getAvancePorcentaje() {
        return avancePorcentaje;
    }

    public boolean isFinalizacionCompleta() {
        return finalizacionCompleta;
    }

    public String getEstado() {
        return estado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public void setFechaFinal(LocalDate fechaFinal) {
        this.fechaFinal = fechaFinal;
    }

    public void setFechaFinalReal(LocalDate fechaFinalReal) {
        this.fechaFinalReal = fechaFinalReal;
    }

    public void setEncargadoPrincipal(int encargadoPrincipal) {
        this.encargadoPrincipal = encargadoPrincipal;
    }

    public void setCostoProyecto(BigDecimal costoProyecto) {
        this.costoProyecto = costoProyecto;
    }

    public void setAreaConstruccion(BigDecimal areaConstruccion) {
        this.areaConstruccion = areaConstruccion;
    }

    public void setAvancePorcentaje(BigDecimal avancePorcentaje) {
        this.avancePorcentaje = avancePorcentaje;
    }

    public void setFinalizacionCompleta(boolean finalizacionCompleta) {
        this.finalizacionCompleta = finalizacionCompleta;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return "ProyectosModel{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", direccion='" + direccion + '\'' +
                ", fechaInicio=" + fechaInicio +
                ", fechaFinal=" + fechaFinal +
                ", fechaFinalReal=" + fechaFinalReal +
                ", encargadoPrincipal=" + encargadoPrincipal +
                ", costoProyecto=" + costoProyecto +
                ", areaConstruccion=" + areaConstruccion +
                ", avancePorcentaje=" + avancePorcentaje +
                ", finalizacionCompleta=" + finalizacionCompleta +
                ", estado='" + estado + '\'' +
                ", descripcion='" + descripcion + '\'' +
                '}';
    }

    /// metodos para accesar a la base de datos
    private static final String SQL_LISTAR_TODOS = "SELECT * FROM proyectos";
    private static final String SQL_LISTAR_ACTIVOS =
            "SELECT p.id, p.nombre, p.fecha_inicio, p.avance_porcentaje, " +
                    "CONCAT(r.abreviatura, ' ',u.nombres, ' ',u.apellidos) AS encargado " +
                    "FROM proyectos p " +
                    "LEFT JOIN usuarios u ON p.encargado_principal = u.id " +
                    "LEFT JOIN roles r ON u.rol_id=r.id " +
                    "WHERE p.finalizacion_completa = FALSE ";

    private static final String SQL_BUSCAR_POR_ID = "SELECT * FROM proyectos WHERE id = ?";
    private static final String SQL_INSERTAR = "INSERT INTO proyectos (nombre, direccion, fecha_inicio, fecha_final, fecha_final_real, encargado_principal, costo_proyecto, area_construccion, avance_porcentaje, finalizacion_completa, estado, descripcion) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_ACTUALIZAR = "UPDATE proyectos SET nombre = ?, direccion = ?, fecha_inicio = ?, fecha_final = ?, fecha_final_real = ?, encargado_principal = ?, costo_proyecto = ?, area_construccion = ?, avance_porcentaje = ?, finalizacion_completa = ?, estado = ?, descripcion = ? WHERE id = ?";
    private static final String SQL_ELIMINAR_LOGICO = "UPDATE proyectos SET finalizacion_completa = TRUE WHERE id = ?";

    public List<ProyectosModel> listarTodos() throws Exception {
        List<ProyectosModel> lista = new ArrayList<>();
        try (Connection con = JDBCUtil.getConection();
             PreparedStatement ps = con.prepareStatement(SQL_LISTAR_TODOS);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapearProyecto(rs));
            }
        }
        return lista;
    }

    public List<ProyectoTablaDto> listarActivos() throws Exception {
        List<ProyectoTablaDto> lista = new ArrayList<>();
        try (Connection con = JDBCUtil.getConection();
             PreparedStatement ps = con.prepareStatement(SQL_LISTAR_ACTIVOS);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ProyectoTablaDto dto = new ProyectoTablaDto();
                dto.setId(rs.getInt("id"));
                dto.setNombre(rs.getString("nombre"));
                dto.setFechaInicio(rs.getDate("fecha_inicio").toLocalDate());
                dto.setEncargado(rs.getString("encargado"));
                dto.setAvance(rs.getBigDecimal("avance_porcentaje"));
                lista.add(dto);
            }
        }
        return lista;
    }

    public ProyectosModel buscarPorId(Long id) throws Exception {
        try (Connection con = JDBCUtil.getConection();
             PreparedStatement ps = con.prepareStatement(SQL_BUSCAR_POR_ID)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapearProyecto(rs);
                else return null;
            }
        }
    }

    public void guardar(ProyectosModel p) throws Exception {
        try (Connection con = JDBCUtil.getConection();
             PreparedStatement ps = con.prepareStatement(SQL_INSERTAR)) {
            ps.setString(1, p.getNombre());
            ps.setString(2, p.getDireccion());
            ps.setDate(3, Date.valueOf(p.getFechaInicio()));
            ps.setDate(4, Date.valueOf(p.getFechaFinal()));
            ps.setNull(5, Types.DATE);
            ps.setInt(6, p.getEncargadoPrincipal());
            System.out.println(p.getEncargadoPrincipal());
            ps.setBigDecimal(7,  BigDecimal.ZERO); //costo del proyecto
            ps.setBigDecimal(8, p.getAreaConstruccion() != null ? p.getAreaConstruccion() : BigDecimal.ZERO); //area de construccion
            ps.setBigDecimal(9, p.getAvancePorcentaje() != null ? p.getAvancePorcentaje() : BigDecimal.ZERO); // avance de porcentaje
            ps.setBoolean(10,false);
            ps.setString(11, p.getEstado() != null ? p.getEstado() : "iniciado"); // Estado (por defecto "iniciado")
            ps.setString(12, p.getDescripcion());
            ps.executeUpdate();
        }
    }

    public void actualizar(ProyectosModel p) throws Exception {
        try (Connection con = JDBCUtil.getConection();
             PreparedStatement ps = con.prepareStatement(SQL_ACTUALIZAR)) {
            ps.setString(1, p.getNombre());
            ps.setString(2, p.getDireccion());
            ps.setDate(3, java.sql.Date.valueOf(p.getFechaInicio()));
            ps.setDate(4, java.sql.Date.valueOf(p.getFechaFinal()));
            ps.setDate(5, java.sql.Date.valueOf(p.getFechaFinalReal()));
            ps.setInt(6, p.getEncargadoPrincipal());
            ps.setBigDecimal(7, p.getCostoProyecto());
            ps.setBigDecimal(8, p.getAreaConstruccion());
            ps.setBigDecimal(9, p.getAvancePorcentaje());
            ps.setBoolean(10, p.isFinalizacionCompleta());
            ps.setString(11, p.getEstado());
            ps.setString(12, p.getDescripcion());
            ps.setLong(13, p.getId());
            ps.executeUpdate();
        }
    }

    public void eliminarLogico(Long id) throws Exception {
        try (Connection con = JDBCUtil.getConection();
             PreparedStatement ps = con.prepareStatement(SQL_ELIMINAR_LOGICO)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }

    private ProyectosModel mapearProyecto(ResultSet rs) throws Exception {
        ProyectosModel p = new ProyectosModel();
        p.setId(rs.getLong("id"));
        p.setNombre(rs.getString("nombre"));
        p.setDireccion(rs.getString("direccion"));
        Date fi = rs.getDate("fecha_inicio");
        if (fi != null) p.setFechaInicio(fi.toLocalDate());
        Date ff = rs.getDate("fecha_final");
        if (ff != null) p.setFechaFinal(ff.toLocalDate());
        Date ffr = rs.getDate("fecha_final_real");
        if (ffr != null) p.setFechaFinalReal(ffr.toLocalDate());
        p.setEncargadoPrincipal(rs.getInt("encargado_principal")); // Asumes que no es null
        p.setCostoProyecto(rs.getBigDecimal("costo_proyecto")); // null se asigna bien
        p.setAreaConstruccion(rs.getBigDecimal("area_construccion"));
        p.setAvancePorcentaje(rs.getBigDecimal("avance_porcentaje"));
        // Manejo correcto del Boolean nullable
        boolean finalizado = rs.getBoolean("finalizacion_completa");
        if (rs.wasNull()) {
            p.setFinalizacionCompleta(false); // No se sabe si fue true o false
        } else {
            p.setFinalizacionCompleta(finalizado);
        }
        // Estado puede ser null, o puedes forzar "iniciado" si lo prefieres
        String estado = rs.getString("estado");
        p.setEstado(estado != null ? estado : "iniciado");
        p.setDescripcion(rs.getString("descripcion"));
        return p;
    }
}