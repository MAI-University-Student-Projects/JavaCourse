package org.mai.dep810.types_lesson;

import java.math.BigDecimal;
import java.util.Currency;

public class Main {
    public static void main(String[] args) {
        Currency usd = Currency.getInstance("USD");
        Currency gbp = Currency.getInstance("GBP");

        Money usdMoney = new Money(usd, new BigDecimal(100));
        Money tenPound = new Money(gbp, new BigDecimal(10));
        CurrencyExchangeRate poundToUsd = new CurrencyExchangeRate(new BigDecimal(1.5), gbp, usd);

        usdMoney = usdMoney.add(new Money(usd, new BigDecimal(10.18743)));
        System.out.println(usdMoney.getCurrency() + " " + usdMoney.getAmount() + " becomes " + usdMoney.getAmount());

        //should throw DifferentCurrenciesException
        try {
            usdMoney = usdMoney.subtract(tenPound);
        } catch(DifferentCurrenciesException ex) {
            System.out.println("DifferentCurrenciesException thrown");
        }

        usdMoney = usdMoney.subtract(poundToUsd.convert(tenPound));
        System.out.println(usdMoney.getCurrency() + " becomes " + usdMoney.getAmount());

        usdMoney = usdMoney.multiply(new BigDecimal(2.3467367));
        System.out.println(usdMoney.getCurrency() + " becomes " + usdMoney.getAmount());

        usdMoney = usdMoney.divide(new BigDecimal(0.21892718));
        System.out.println(usdMoney.getCurrency() + " becomes " + usdMoney.getAmount());

//        Money usdMoney_part = usdMoney.get_Part(4);
//        System.out.println(usdMoney.getCurrency() + " " + usdMoney.getAmount() + " to " + usdMoney_part.getAmount());
        Money hundr_bucks = new Money(usd, new BigDecimal(50));
        Money[] divid = hundr_bucks.DivideByN(7);
        for (Money money : divid) {
            System.out.println(money.getAmount());
        }
    }
}