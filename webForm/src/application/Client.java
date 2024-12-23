package application;

import Controller.EmailValidationException;
import Controller.NegativeLengthException;
import Controller.RangeNumberException;
import entities.UserNameForm;

public class Client{
    public static void main(String[] args) throws RangeNumberException, EmailValidationException, NegativeLengthException {
        boolean debug_Client1 = true; //all form fields are ok.
        boolean debug_Client2 = true; // change to true to test to an incorrect input to the form
        boolean debug_interface = false; // interface testing

        if (debug_Client1) {
            UserNameForm form = new UserNameForm();

            form.get("username").setData("tia");
            form.get("email").setData("tia@gmail.com");
            form.get("age").setData(19);

            if (debug_Client2) {
                form.get("username").setData("ti"); //minimum characters = 3
                form.get("email").setData("tiagmail.com"); //detect if is null, if is missing "@" and "."
                form.get("age").setData(13); //age range from 16 to 99

            }
            form.validate();

            //errors output
            System.out.println();
            for (String err : form.errors)
                System.out.println(err);

            //JSON & HTML output
            System.out.println(form.content());
            System.out.println(form.json());


        }
    }
}

