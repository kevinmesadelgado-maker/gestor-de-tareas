package com.example.application.models;

import java.time.LocalDate;

public class Tarea {

    private String nombre;
    private String descripcion;
    private String estado;
    private LocalDate fecha;

    public Tarea(String nombre, String descripcion, String estado, LocalDate fecha) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.estado = estado;
        this.fecha = fecha;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getEstado() {
        return estado;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }
}