package com.example.application.views.base;


import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public abstract class AbstractView extends VerticalLayout {

    public AbstractView() {
        setSizeFull();
        setPadding(true);
        setSpacing(true);
    }

    // método abstracto (obliga a implementar)
    protected abstract void configurarVista();

}