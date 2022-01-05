package moneycalculator;

import java.util.List;
import moneycalculator.model.Currency;
import moneycalculator.persistence.CurrencyLoader;
import moneycalculator.persistence.ExchangeRateLoader;
import moneycalculator.persistence.FileCurrencyLoader;
import moneycalculator.persistence.WebServiceExchangeRateLoader;

public class Main {

    public static void main(String[] args) {
        CurrencyLoader currencyLoader = new FileCurrencyLoader("currencies");
        List<Currency> currencies = currencyLoader.load();
        ExchangeRateLoader exchangeRateLoader = new WebServiceExchangeRateLoader();
        MainFrame mainFrame = new MainFrame(currencies, exchangeRateLoader);
        
    }
}
