package entities;

import Controller.Validator;

import java.util.Arrays;

//Classe Genérica porque conforme o parametro vai assumir um tipo para cada implementação (entities.StringField e entities.NumberField)
public class Field<E> {

    String label;
    private E value;
    public Validator[] validatorArray;

    Field(){}

    public Field(String label, Validator[] validatorArray) {
        this.label = label;
        this.validatorArray = validatorArray;
    }

    public void setData(E value){
        this.value = value;
    }

    public E getData(){
        return this.value;
    }

    @Override
    public String toString() {
        return "entities.Field:\n" + "label= " + label + "\nvalidatorArray= " + Arrays.toString(validatorArray);
    }
}
