package org.example.dto;

import java.math.BigDecimal;
import java.util.Date;

public class SolicitudTablaDto {
    private int id;
    private String nombre; //nombre del material
    private BigDecimal cantidad_solicitada;
    private String unidad_medida; //bls, unidad, etc
    private Date fecha_solicitud;
    private String comentario;
    private String nombreCompleto;

    public SolicitudTablaDto() {}

    public SolicitudTablaDto(int id, String nombre, BigDecimal cantidad_solicitada, String unidad_medida, Date fecha_solicitud, String comentario, String nombreCompleto) {
        this.id = id;
        this.nombre = nombre;
        this.cantidad_solicitada = cantidad_solicitada;
        this.unidad_medida = unidad_medida;
        this.fecha_solicitud = fecha_solicitud;
        this.comentario = comentario;
        this.nombreCompleto = nombreCompleto;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public BigDecimal getCantidad_solicitada() {
        return cantidad_solicitada;
    }

    public String getUnidad_medida() {
        return unidad_medida;
    }

    public Date getFecha_solicitud() {
        return fecha_solicitud;
    }

    public String getComentario() {
        return comentario;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setCantidad_solicitada(BigDecimal cantidad_solicitada) {
        this.cantidad_solicitada = cantidad_solicitada;
    }

    public void setUnidad_medida(String unidad_medida) {
        this.unidad_medida = unidad_medida;
    }

    public void setFecha_solicitud(Date fecha_solicitud) {
        this.fecha_solicitud = fecha_solicitud;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }
}
