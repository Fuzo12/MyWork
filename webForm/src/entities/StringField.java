package entities;

import Controller.Validator;
import entities.Field;

public class StringField extends Field<String> {
    String label;

    public StringField(String label, Validator[] validatoresArray) {
        super(label, validatoresArray);
        this.label = label;
    }

    String getLabel(){
        return this.label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return "Label: " + label;
    }
}

