package org.example.models;

import org.example.JDBCUtil;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SolicitudMaterialModel {
    private int id;
    private int proyectoId;
    private int materialId;
    private int SolicitadoPor;
    private BigDecimal cantidadSolicitada;
    private Date fechaRespuesta;
    private Date fechaSolicitud;
    private String comentario;
    private String estado;
    private boolean tratado;

    public SolicitudMaterialModel() {
    }

    public SolicitudMaterialModel(int id, int proyectoId, int materialId, int solicitadoPor, BigDecimal cantidadSolicitada, Date fechaRespuesta, Date fechaSolicitud, String comentario, String estado, boolean tratado) {
        this.id = id;
        this.proyectoId = proyectoId;
        this.materialId = materialId;
        SolicitadoPor = solicitadoPor;
        this.cantidadSolicitada = cantidadSolicitada;
        this.fechaRespuesta = fechaRespuesta;
        this.fechaSolicitud = fechaSolicitud;
        this.comentario = comentario;
        this.estado = estado;
        this.tratado = tratado;
    }

    //geters

    public int getId() {
        return id;
    }

    public int getProyectoId() {
        return proyectoId;
    }

    public int getMaterialId() {
        return materialId;
    }

    public int getSolicitadoPor() {
        return SolicitadoPor;
    }

    public BigDecimal getCantidadSolicitada() {
        return cantidadSolicitada;
    }

    public Date getFechaRespuesta() {
        return fechaRespuesta;
    }

    public Date getFechaSolicitud() {
        return fechaSolicitud;
    }

    public String getComentario() {
        return comentario;
    }

    public String getEstado() {
        return estado;
    }

    //setters


    public void setId(int id) {
        this.id = id;
    }

    public void setProyectoId(int proyectoId) {
        this.proyectoId = proyectoId;
    }

    public void setMaterialId(int materialId) {
        this.materialId = materialId;
    }

    public void setSolicitadoPor(int solicitadoPor) {
        SolicitadoPor = solicitadoPor;
    }

    public void setCantidadSolicitada(BigDecimal cantidadSolicitada) {
        this.cantidadSolicitada = cantidadSolicitada;
    }

    public void setFechaRespuesta(Date fechaRespuesta) {
        this.fechaRespuesta = fechaRespuesta;
    }

    public void setFechaSolicitud(Date fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setTratado(boolean tratado) {
        this.tratado = tratado;
    }

    public boolean isTratado() {
        return tratado;
    }

    /// metodos CRUD

    private static final String SQL_INSERTAR = " INSERT INTO solicitudes_materiales (proyecto_id, material_id, solicitado_por, cantidad_solicitada, fecha_solicitud, comentario) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SQL_ACTUALIZAR = " UPDATE solicitudes_materiales SET proyecto_id = ?, material_id = ?, solicitado_por = ?, cantidad_solicitada = ?, fecha_solicitud = ?, estado = ?, comentario = ?, fecha_respuesta = ?, tratado = ? WHERE id = ?";
    private static final String SQL_ELIMINAR_LOGICO = " UPDATE solicitudes_materiales SET tratado = TRUE, estado = 'RECHAZADA' WHERE id = ? ";
    private static final String SQL_LISTAR_PENDIENTES_NO_TRATADOS = "SELECT * FROM solicitudes_materiales WHERE estado = 'PENDIENTE' AND tratado = FALSE";


    public void guardar(SolicitudMaterialModel s) throws Exception {
        try (Connection con = JDBCUtil.getConection();
             PreparedStatement ps = con.prepareStatement(SQL_INSERTAR)) {
            ps.setInt(1, s.getProyectoId());
            ps.setInt(2, s.getMaterialId());
            ps.setInt(3, s.getSolicitadoPor());
            ps.setBigDecimal(4, s.getCantidadSolicitada());
            ps.setDate(5, new java.sql.Date(s.getFechaSolicitud().getTime()));
            ps.setString(6, s.getComentario());
            ps.executeUpdate();
        }
    }

    public void actualizar(SolicitudMaterialModel s) throws Exception {
        try (Connection con = JDBCUtil.getConection();
             PreparedStatement ps = con.prepareStatement(SQL_ACTUALIZAR)) {

            ps.setInt(1, s.getProyectoId());
            ps.setInt(2, s.getMaterialId());
            ps.setInt(3, s.getSolicitadoPor());
            ps.setBigDecimal(4, s.getCantidadSolicitada());
            ps.setDate(5, new java.sql.Date(s.getFechaSolicitud().getTime()));
            ps.setString(6, s.getEstado());
            ps.setString(7, s.getComentario());
            if (s.getFechaRespuesta() != null) {
                ps.setDate(8, new java.sql.Date(s.getFechaRespuesta().getTime()));
            } else {
                ps.setNull(8, Types.DATE);
            }
            ps.setBoolean(9, s.isTratado());
            ps.setInt(10, s.getId());

            ps.executeUpdate();
        }
    }

    public List<SolicitudMaterialModel> listarPendientesNoTratados() throws Exception {
        List<SolicitudMaterialModel> lista = new ArrayList<>();
        try (Connection con = JDBCUtil.getConection();
             PreparedStatement ps = con.prepareStatement(SQL_LISTAR_PENDIENTES_NO_TRATADOS);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearSolicitud(rs));
            }
        }
        return lista;
    }

    private SolicitudMaterialModel mapearSolicitud(ResultSet rs) throws Exception {
        SolicitudMaterialModel solicitud = new SolicitudMaterialModel();
        solicitud.setId(rs.getInt("id"));
        solicitud.setProyectoId(rs.getInt("proyecto_id"));
        solicitud.setMaterialId(rs.getInt("material_id"));
        solicitud.setSolicitadoPor(rs.getInt("solicitado_por"));
        solicitud.setCantidadSolicitada(rs.getBigDecimal("cantidad_solicitada"));
        solicitud.setFechaSolicitud(rs.getDate("fecha_solicitud"));
        solicitud.setEstado(rs.getString("estado"));
        solicitud.setComentario(rs.getString("comentario"));
        solicitud.setFechaRespuesta(rs.getDate("fecha_respuesta"));
        solicitud.setTratado(rs.getBoolean("tratado"));
        return solicitud;
    }

}
