package org.mai.dep810.types_lesson;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;

public class Money {
    private Currency currency;
    private BigDecimal amount;

    public Money(Currency currency, BigDecimal amount) {
        if(amount.setScale(currency.getDefaultFractionDigits(), RoundingMode.DOWN).compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Money amount must be >= 0");
        }
        this.currency = currency;
        this.amount = amount.setScale(this.currency.getDefaultFractionDigits(), RoundingMode.DOWN);
    }

    public Currency getCurrency() {
        return currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Money add(Money m) {
        if(!this.currency.equals(m.currency))
            throw new DifferentCurrenciesException("different currency operation");
        System.out.println("Adding " + m.amount + this.currency);
        return new Money(this.currency, this.amount.add(m.amount));
        //throw new NotImplementedException();
    }

    public Money subtract(Money m) {
        if(!this.currency.equals(m.currency))
            throw new DifferentCurrenciesException("different currency operation");
        System.out.println("Substracting " + m.amount + this.currency);
        return new Money(this.currency, this.amount.subtract(m.amount));
        //throw new NotImplementedException();
    }

    public Money multiply(BigDecimal ratio) {
        if(ratio.setScale(currency.getDefaultFractionDigits(), RoundingMode.DOWN).compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Multiplier must be >= 0");
        }
        System.out.println("Multiplying by " + ratio + " with " + this.amount + this.currency);
        return new Money(this.currency, this.amount.multiply(ratio).setScale(2, RoundingMode.DOWN));
        //throw new NotImplementedException();
    }

    public Money divide(BigDecimal ratio) {
        if(ratio.setScale(currency.getDefaultFractionDigits(), RoundingMode.DOWN).equals(BigDecimal.ZERO)) {
            throw new IllegalArgumentException("Denominator must be != 0");
        }
        System.out.println("Dividing by " + ratio + " with " + this.amount + this.currency);
        return new Money(this.currency, this.amount.divide(ratio,2, RoundingMode.HALF_DOWN));
        //throw new NotImplementedException();
    }

    public Money[] DivideByN(int dnmtr) {
        if (!(dnmtr > 0)) {
            throw new IllegalArgumentException("Part division prmtr must be > 0");
        }
        Money[] arr = new Money[dnmtr];
        Money tmp_res = new Money(this.currency, this.amount);
        BigDecimal sum_tmp = new BigDecimal(0).setScale(2);
        for(int i = 0; i < dnmtr; i++) {
            arr[i] = tmp_res.divide(new BigDecimal(dnmtr - i));
            tmp_res = tmp_res.subtract(arr[i]);
            sum_tmp = sum_tmp.add(arr[i].getAmount());
        }
        System.out.println("Summ:" + sum_tmp);
        return arr;
    }
}
