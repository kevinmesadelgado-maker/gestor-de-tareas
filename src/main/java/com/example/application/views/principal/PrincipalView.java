package com.example.application.views.principal;

import com.example.application.models.Tarea;
import com.example.application.services.TareaService;
import com.example.application.views.base.AbstractView;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.datepicker.DatePicker;

import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import org.vaadin.lineawesome.LineAwesomeIconUrl;

@PageTitle("Inicio")
@Route("")
@Menu(order = 0, icon = LineAwesomeIconUrl.FILE)
public class PrincipalView extends AbstractView {

    private VerticalLayout pendientes = new VerticalLayout();
    private VerticalLayout enCurso = new VerticalLayout();
    private VerticalLayout finalizado = new VerticalLayout();

    public PrincipalView() {
        configurarVista();
        addAttachListener(e -> actualizarVista());
    }

    @Override
    protected void configurarVista() {

        H2 header = new H2("Gestor de Tareas");

        HorizontalLayout tablero = new HorizontalLayout(
                pendientes, enCurso, finalizado
        );
        tablero.setSizeFull();

        add(header, tablero);
    }

    private void configurarColumna(VerticalLayout columna, String titulo, String color, String estado, int cantidad) {

        columna.setWidth("33%");
        columna.getStyle()
                .set("background", color)
                .set("padding", "10px")
                .set("border-radius", "10px");

        HorizontalLayout header = new HorizontalLayout();
        header.setWidthFull();
        header.setJustifyContentMode(JustifyContentMode.BETWEEN);

        H3 tituloCol = new H3(titulo + " (" + cantidad + ")");

        header.add(tituloCol);

        // esto es para que no se pueda agregar tareas a la columna de Finalizado       

          if (!estado.equals("Finalizado")) {

            Button agregar = new Button("+ Agregar tarea");

            agregar.addClickListener(e ->
                    getUI().ifPresent(ui -> ui.navigate("crear/" + estado))
            );

            header.add(agregar);
        }

        columna.add(header);
    }

    private void actualizarVista() {

        pendientes.removeAll();
        enCurso.removeAll();
        finalizado.removeAll();

        int cPendiente = 0;
        int cEnCurso = 0;
        int cFinalizado = 0;

        for (Tarea t : TareaService.tareas) {
            switch (t.getEstado()) {
                case "Pendiente": cPendiente++; break;
                case "En curso": cEnCurso++; break;
                case "Finalizado": cFinalizado++; break;
            }
        }

        configurarColumna(pendientes, "Pendiente", "#fff3cd", "Pendiente", cPendiente);
        configurarColumna(enCurso, "En curso", "#cfe2ff", "En curso", cEnCurso);
        configurarColumna(finalizado, "Finalizado", "#d1e7dd", "Finalizado", cFinalizado);

        for (Tarea t : TareaService.tareas) {

            VerticalLayout card = crearCard(t);

            switch (t.getEstado()) {
                case "Pendiente": pendientes.add(card); break;
                case "En curso": enCurso.add(card); break;
                case "Finalizado": finalizado.add(card); break;
            }
        }
    }

    private VerticalLayout crearCard(Tarea t) {

        VerticalLayout card = new VerticalLayout();
        card.setWidthFull();

        card.getStyle()
                .set("background", "white")
                .set("padding", "10px")
                .set("border-radius", "8px")
                .set("box-shadow", "2px 2px 5px rgba(0,0,0,0.1)");

        // su header con nombre y su menu 
        HorizontalLayout header = new HorizontalLayout();
        header.setWidthFull();
        header.setJustifyContentMode(JustifyContentMode.BETWEEN);

        H4 nombre = new H4(t.getNombre());

        MenuBar menu = new MenuBar();
        MenuItem opciones = menu.addItem("⋮");

       
        opciones.getSubMenu().addItem("Editar", e -> {

            Dialog dialog = new Dialog();

            TextField nombreField = new TextField("Nombre");
            nombreField.setValue(t.getNombre());

            TextArea descripcionField = new TextArea("Descripción");
            descripcionField.setValue(t.getDescripcion());

            DatePicker fechaField = new DatePicker("Fecha");
            fechaField.setValue(t.getFecha());

            Button guardar = new Button("Guardar", ev -> {
                t.setNombre(nombreField.getValue());
                t.setDescripcion(descripcionField.getValue());
                t.setFecha(fechaField.getValue());

                dialog.close();
                actualizarVista();
            });

            dialog.add(nombreField, descripcionField, fechaField, guardar);
            dialog.open();
        });

        
        opciones.getSubMenu().addItem("Eliminar", e -> {

            Dialog confirm = new Dialog();

            Paragraph texto = new Paragraph("¿Seguro que quieres eliminar esta tarea?");

            Button si = new Button("Sí", ev -> {
                TareaService.tareas.remove(t);
                confirm.close();
                actualizarVista();
            });

            Button no = new Button("Cancelar", ev -> confirm.close());

            confirm.add(texto, new HorizontalLayout(si, no));
            confirm.open();
        });

        if (!t.getEstado().equals("Pendiente")) {
            opciones.getSubMenu().addItem("Pendiente", e -> {
                t.setEstado("Pendiente");
                actualizarVista();
            });
        }

        if (!t.getEstado().equals("En curso")) {
            opciones.getSubMenu().addItem("En curso", e -> {
                t.setEstado("En curso");
                actualizarVista();
            });
        }

        if (!t.getEstado().equals("Finalizado")) {
            opciones.getSubMenu().addItem("Finalizado", e -> {
                t.setEstado("Finalizado");
                actualizarVista();
            });
        }

        header.add(nombre, menu);

        
        Span descripcion = new Span(t.getDescripcion());
        descripcion.getStyle().set("font-size", "12px");

        card.add(header, descripcion);

        return card;
    }
}