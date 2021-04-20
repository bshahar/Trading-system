package Domain;

public class DemoPayment implements PaymentInterface{
    @Override
    public boolean pay(double amount, String creditCardNumber) {
        return !(amount <= 0);
    }
}
