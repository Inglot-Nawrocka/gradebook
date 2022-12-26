package com.gradebook.views.components;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.HasValueAndElement;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import lombok.Getter;
import org.vaadin.crudui.crud.CrudOperation;
import org.vaadin.crudui.form.AbstractAutoGeneratedCrudFormFactory;

import java.util.List;


public class CustomCrudFormFactory<T> extends AbstractAutoGeneratedCrudFormFactory<T> {
    @Getter
    Button operationButton;
    @Getter
    Button cancelButton;

    public CustomCrudFormFactory(Class<T> domainType) {
        super(domainType);
    }

    @Override
    public Component buildNewForm(CrudOperation operation, T domainObject, boolean readOnly, ComponentEventListener<ClickEvent<Button>> cancelButtonClickListener, ComponentEventListener<ClickEvent<Button>> operationButtonClickListener) {
        FormLayout formLayout = new FormLayout();
        formLayout.setSizeFull();

        List<HasValueAndElement> fields = buildFields(operation, domainObject, readOnly);
        fields.forEach(field ->
                        formLayout.getElement().appendChild(field.getElement()));

        Component footerLayout = buildFooter(operation, domainObject, cancelButtonClickListener, operationButtonClickListener);

        com.vaadin.flow.component.orderedlayout.VerticalLayout mainLayout = new VerticalLayout(formLayout, footerLayout);
        mainLayout.setFlexGrow(1, formLayout);
        mainLayout.setHorizontalComponentAlignment(FlexComponent.Alignment.END, footerLayout);
        mainLayout.setMargin(false);
        mainLayout.setPadding(false);
        mainLayout.setSpacing(true);

        return mainLayout;
    }

    @Override
    public String buildCaption(CrudOperation operation, T domainObject) {
        return null;
    }
    @Override
    protected Component buildFooter(CrudOperation operation, T domainObject, ComponentEventListener<ClickEvent<Button>> cancelButtonClickListener, ComponentEventListener<ClickEvent<Button>> operationButtonClickListener) {
        operationButton = buildOperationButton(operation, domainObject, operationButtonClickListener);
        cancelButton = buildCancelButton(cancelButtonClickListener);

        HorizontalLayout footerLayout = new HorizontalLayout();
        footerLayout.setSizeUndefined();
        footerLayout.setSpacing(true);
        footerLayout.setPadding(false);

        if (cancelButton != null) {
            footerLayout.add(cancelButton);
        }

        if (operationButton != null) {
            footerLayout.add(operationButton);
        }

        return footerLayout;
    }

}