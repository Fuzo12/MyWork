package entities;

import Controller.*;

public class UserNameForm extends Form {

    public UserNameForm(){
        super();

        this.put("username", new StringField("Username",new Validator[]{new Length(3)}));
        this.put("email", new StringField("Email", new Validator[]{new Required()}));
        this.put("age", new NumberField("Age", new Validator[]{new NumberRange(16,99)}));
    }



    // métodos:    =======================================================================

    public String content(){
        //format HTML
        return
                "\n<form>\n" +
                        "\t\t<label for=\'email\'>email:</label>\n" +
                        "\t\t<input name=\'email\' type=\'text\' value=\'" + get("email").getData() + "\'/><br>\n" +
                        "\t\t<label for=\'age\'>age:</label>\n" +
                        "\t\t<input name=\'age\' type=\'number\' value=\'" + get("age").getData() + "\'/><br>\n" +
                        "\t\t<label for=\'username\'>username:</label>\n" +
                        "\t\t<input name=\'username\' type=\'text\' value=\'" + get("username").getData() + "\'/><br>\n" +
                "</form>";
    }
    //JSON
    public String json(){
        return "{" +
               "\'email\':" + '\'' + get("email").getData() + '\'' +
               ",\'age\':" + get("age").getData() +
                ", \'username\':" + '\'' + get("username").getData() + '\'' +
               '}';
    }

    @Override
    public String toString() {
        return "toString do entities.UserNameForm";
    }
}
