package entities;


import Controller.Validator;

public class NumberField extends Field<Integer>{
    String label;
    String name = "Test entities.NumberField";

    public NumberField(){}

    public NumberField(String label, Validator[] validatorArray){
        super(label, validatorArray);
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
