package entities;

import Controller.EmailValidationException;
import Controller.NegativeLengthException;
import Controller.RangeNumberException;
import Controller.Validator;

import java.util.ArrayList;
import java.util.HashMap;


public class Form extends HashMap<String,Field>{
    String label;
    String username;
    String email;
    int age;
    public ArrayList<String> errors = new ArrayList<String>();


    Form() { }

    public ArrayList<String> validate() throws RangeNumberException, EmailValidationException, NegativeLengthException {

        for (String label : keySet()) {
            var output = get(label);
           fieldCheck(output);
        }

        return errors;
    }

    private void fieldCheck(Field fieldToCheck) throws RangeNumberException, EmailValidationException, NegativeLengthException {
        Validator[] arrayOfValidators = fieldToCheck.validatorArray;

       if (arrayOfValidators != null) {
            for (Validator x : arrayOfValidators) {
                if(x.validate(fieldToCheck.getData()) != "True")
                errors.add(x.validate(fieldToCheck.getData()));
            }
        }
    }


    @Override
    public String toString() {
        return "entities.Form: " + "\nlabel: " + label + "\nusername: " + username + "email: " + email;
    }
}
