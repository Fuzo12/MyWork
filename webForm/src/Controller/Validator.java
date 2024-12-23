package Controller;

public interface Validator <N> {

    public String validate(N x) throws RangeNumberException, EmailValidationException, NegativeLengthException;

    public boolean verifyIfValid(String result);

}
