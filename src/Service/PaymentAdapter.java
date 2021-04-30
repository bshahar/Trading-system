package Service;

import Domain.PaymentInterface;

public class PaymentAdapter {
    PaymentInterface paymentInterface;
    public PaymentAdapter(PaymentInterface paymentInterface) {
        this.paymentInterface = paymentInterface;

    }

    public boolean pay(double amount, String creditCardNumber) { //TODO fix args & implement
        return paymentInterface.pay(amount,creditCardNumber);
    }
}
