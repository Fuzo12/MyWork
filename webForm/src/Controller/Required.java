package Controller;

public class Required implements Validator<String>{

    public Required() {
    }

    @Override
    public final String validate(String x) throws EmailValidationException {
        if (x.length() <= 0) {
            throw new EmailValidationException("\nError: value empty");
        //validate @ e "."
        } else if (!x.contains("@") || !x.contains(".")) {
            throw new EmailValidationException("\nError: email not in correct format");
        } else {
            return "True";
        }
    }

    @Override
    public final boolean verifyIfValid(String result) {
        return result == "True";
    }

}



