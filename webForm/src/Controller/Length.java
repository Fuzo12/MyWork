package Controller;

public class Length implements Validator<String>  {

    int min; // minimum size input

    public Length(int min) {
        this.min = min;
    }

    @Override
    public String validate(String x) throws NegativeLengthException {
        if(x.length() < this.min){
            throw new NegativeLengthException("\nError: less than min");
        } else {
            return "True";
        }
    }

    @Override
    public final boolean verifyIfValid(String result) {
        return result == "True";
    }

    @Override
    public final String toString() {
        return "Controller.Length min:" + min ;
    }
}


