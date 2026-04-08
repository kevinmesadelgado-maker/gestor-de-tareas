package com.example.application.views.principal;

import com.example.application.models.Tarea;
import com.example.application.services.TareaService;
import com.example.application.views.MainLayout;

import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Menu;

import org.vaadin.lineawesome.LineAwesomeIconUrl;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.Locale;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;


@PageTitle("Calendario")
@Route(value = "calendario", layout = MainLayout.class)
@Menu(order = 1, title = "Calendario", icon = LineAwesomeIconUrl.CALENDAR)
public class CalendarView extends VerticalLayout {

    private YearMonth mesActual = YearMonth.now();

    public CalendarView() {
        actualizarVista();
    }

    private void actualizarVista() {

        removeAll();

        // 🔹 HEADER (botones + título)
        HorizontalLayout header = new HorizontalLayout();
        header.setWidthFull();
        header.setJustifyContentMode(JustifyContentMode.BETWEEN);
        header.setAlignItems(Alignment.CENTER);

        Button anterior = new Button(new Icon(VaadinIcon.ANGLE_LEFT));
        Button siguiente = new Button(new Icon(VaadinIcon.ANGLE_RIGHT));


        String nombreMes = mesActual.getMonth()
                .getDisplayName(TextStyle.FULL, new Locale("es"));

        // Capitalizar bonito (Abril en vez de abril)
        nombreMes = nombreMes.substring(0,1).toUpperCase() + nombreMes.substring(1);

        H2 titulo = new H2("📅 " + nombreMes + " " + mesActual.getYear());

        anterior.addClickListener(e -> {
            mesActual = mesActual.minusMonths(1);
            actualizarVista();
        });

        siguiente.addClickListener(e -> {
            mesActual = mesActual.plusMonths(1);
            actualizarVista();
        });

        header.add(anterior, titulo, siguiente);

        // 🔹 CONTENEDOR CALENDARIO
        VerticalLayout calendario = new VerticalLayout();
        calendario.setWidthFull();
        calendario.setSpacing(false);

        // 🔹 DÍAS DE LA SEMANA
        HorizontalLayout diasSemana = new HorizontalLayout();
        diasSemana.setWidthFull();

        String[] dias = {"Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom"};

        for (String d : dias) {
            Div dia = new Div();
            dia.setText(d);

            dia.getStyle()
                    .set("flex", "1")
                    .set("text-align", "center")
                    .set("font-weight", "bold");

            diasSemana.add(dia);
        }

        calendario.add(diasSemana);

        LocalDate primerDia = mesActual.atDay(1);
        int inicio = primerDia.getDayOfWeek().getValue();
        int diasMes = mesActual.lengthOfMonth();

        int contador = 1;

        //  FILAS (semanas)
        for (int fila = 0; fila < 6; fila++) {

            HorizontalLayout semana = new HorizontalLayout();
            semana.setWidthFull();

            for (int col = 1; col <= 7; col++) {

                VerticalLayout celda = new VerticalLayout();
                celda.setPadding(false);
                celda.setSpacing(false);

                celda.getStyle()
                        .set("flex", "1")
                        .set("border", "1px solid #ddd")
                        .set("min-height", "100px");

                // 🔹 espacios vacíos antes del inicio
                if (fila == 0 && col < inicio) {
                    semana.add(celda);
                    continue;
                }

                // 🔹 después del final del mes
                if (contador > diasMes) {
                    semana.add(celda);
                    continue;
                }

                LocalDate fecha = mesActual.atDay(contador);

                // 🔹 número del día
                Span numero = new Span(String.valueOf(contador));
                numero.getStyle().set("font-weight", "bold");

                celda.add(numero);

                //  TAREAS EN ESE DÍA
                for (Tarea t : TareaService.tareas) {

                    if (fecha.equals(t.getFecha())) {

                        Span tarea = new Span("• " + t.getNombre());

                        tarea.getStyle()
                                .set("font-size", "12px")
                                .set("padding", "2px")
                                .set("border-radius", "4px");

                        //  color por estado
                        switch (t.getEstado()) {
                            case "Pendiente":
                                tarea.getStyle().set("background", "#fff3cd");
                                break;
                            case "En curso":
                                tarea.getStyle().set("background", "#cfe2ff");
                                break;
                            case "Finalizado":
                                tarea.getStyle().set("background", "#d1e7dd");
                                break;
                        }

                        celda.add(tarea);
                    }
                }

                // CLICK EN DÍA
                celda.addClickListener(e -> {
                    System.out.println("Click en: " + fecha);
                });

                semana.add(celda);
                contador++;
            }

            calendario.add(semana);
        }

        add(header, calendario);
    }
}