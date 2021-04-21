package Service;

import Domain.PaymentInterface;

public class PaymentAdapter {
    //TODO create field of external system?
    PaymentInterface paymentInterface;
    public PaymentAdapter(PaymentInterface paymentInterface) {
        this.paymentInterface = paymentInterface;

    }

    public boolean pay(double amount, String creditCardNumber) { //TODO fix args & implement
        return paymentInterface.pay(amount,creditCardNumber);
    }
}
