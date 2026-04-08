package com.example.application.views.principal;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.datepicker.DatePicker.DatePickerI18n;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;

import com.example.application.models.Tarea;
import com.example.application.services.TareaService;
import com.vaadin.flow.router.PageTitle;

import java.util.Arrays;

@Route(value= "crear")
@PageTitle("Crear Tarea")
public class CrearTareaView extends VerticalLayout implements HasUrlParameter<String> {

    private String estado;

    @Override
    public void setParameter(BeforeEvent event, String parameter) {
        this.estado = parameter;
    }

    public CrearTareaView() {

        H2 titulo = new H2("➕ Nueva página");

        TextField nombre = new TextField("Nombre");
        TextArea descripcion = new TextArea("Descripción");

        DatePicker fecha = new DatePicker("Fecha");

        // 🔥 CONFIGURAR ESPAÑOL
        DatePickerI18n i18n = new DatePickerI18n();

        i18n.setMonthNames(Arrays.asList(
                "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
                "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
        ));

        i18n.setWeekdays(Arrays.asList(
                "Domingo", "Lunes", "Martes", "Miércoles",
                "Jueves", "Viernes", "Sábado"
        ));

        i18n.setWeekdaysShort(Arrays.asList(
                "Dom", "Lun", "Mar", "Mié", "Jue", "Vie", "Sáb"
        ));

        i18n.setToday("Hoy");
        i18n.setCancel("Cancelar");

        fecha.setI18n(i18n);

        Button guardar = new Button("Guardar");

        guardar.addClickListener(e -> {

            Tarea nueva = new Tarea(
                    nombre.getValue(),
                    descripcion.getValue(),
                    estado,
                    fecha.getValue()
            );

            TareaService.tareas.add(nueva);

            getUI().ifPresent(ui -> ui.navigate(""));
        });

        add(titulo, nombre, descripcion, fecha, guardar);
    }
}