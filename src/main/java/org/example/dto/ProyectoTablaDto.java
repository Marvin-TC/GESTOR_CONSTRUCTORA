package org.example.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ProyectoTablaDto {
    private int id;
    private String nombre;
    private LocalDate fechaInicio;
    private String encargado;
    private BigDecimal avance;

    public ProyectoTablaDto(int id, String nombre, LocalDate fechaInicio, String encargado, BigDecimal avance) {
        this.id = id;
        this.nombre = nombre;
        this.fechaInicio = fechaInicio;
        this.encargado = encargado;
        this.avance = avance;
    }

    public ProyectoTablaDto() {}

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public String getEncargado() {
        return encargado;
    }

    public BigDecimal getAvance() {
        return avance;
    }

    //setters


    public void setId(int id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public void setEncargado(String encargado) {
        this.encargado = encargado;
    }

    public void setAvance(BigDecimal avance) {
        this.avance = avance;
    }
}
