package org.mai.dep810.types_lesson;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;

public class CurrencyExchangeRate {

    private Currency _from;
    private Currency _to;
    private BigDecimal _rate;

    public CurrencyExchangeRate(BigDecimal rate, Currency from, Currency to) {
        if(!(rate.setScale(to.getDefaultFractionDigits(), RoundingMode.DOWN).compareTo(BigDecimal.ZERO) > 0)) {
            throw new IncorrectExchangeRateException("Exchange rate must be > 0");
        }
        this._from = from;
        this._to = to;
        this._rate = rate;
    }

    public Money convert(Money m) {
        if (!(m.getCurrency().equals(this._from))) {
            throw new IncorrectExchangeRateException("Not alignable converter");
        }
        BigDecimal amount_n = m.getAmount().multiply(this._rate);
        return new Money(this._to, amount_n.setScale(this._to.getDefaultFractionDigits(), RoundingMode.DOWN));
        //throw new NotImplementedException();
    }
}
