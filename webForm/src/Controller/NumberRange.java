package Controller;

public class NumberRange implements Validator<Integer> {
    int max;
    int min;

    public NumberRange(int min, int max) { // Construtor
        this.max = max;
        this.min = min;
    }

    @Override
    public String validate(Integer nr) throws RangeNumberException{
        if (nr < this.min || nr > this.max){
            throw new RangeNumberException("\nError: value not in range");
        } else {return "True";}
    }

    @Override
    public final boolean verifyIfValid(String result) {
        return result == "True";
    }

    @Override
    public final String toString() {
        return "Controller.NumberRange:\n" + "max=" + max + "\nmin=" + min ;
    }
}
