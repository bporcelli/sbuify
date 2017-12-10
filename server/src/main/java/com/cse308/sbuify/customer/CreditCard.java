package com.cse308.sbuify.customer;

import java.io.Serializable;

/**
 * Simple representation of a credit card.
 */
public class CreditCard implements Serializable {

    private String cardNumber;

    private String cvc;

    private String expMonth;

    private String expYear;

    public CreditCard() {
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCvc() {
        return cvc;
    }

    public void setCvc(String cvc) {
        this.cvc = cvc;
    }

    public String getExpMonth() {
        return expMonth;
    }

    public void setExpMonth(String expMonth) {
        this.expMonth = expMonth;
    }

    public String getExpYear() {
        return expYear;
    }

    public void setExpYear(String expYear) {
        this.expYear = expYear;
    }

    public boolean isValid() {
        return isValidCC(cardNumber);
    }

    /**
     * Validates a credit card number using the Luhn algorithm. Courtesy of Chris Wareham.
     * @param ccNumber The number to validate.
     * @return true if the number is valid, otherwise false.
     */
    public static boolean isValidCC(String ccNumber) {
        int sum = 0;
        boolean alternate = false;
        for (int i = ccNumber.length() - 1; i >= 0; i--)
        {
            int n = Integer.parseInt(ccNumber.substring(i, i + 1));
            if (alternate)
            {
                n *= 2;
                if (n > 9)
                {
                    n = (n % 10) + 1;
                }
            }
            sum += n;
            alternate = !alternate;
        }
        return (sum % 10 == 0);
    }
}
