package moneycalculator.persistence;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import moneycalculator.model.Currency;
import moneycalculator.model.ExchangeRate;

public class WebServiceExchangeRateLoader implements ExchangeRateLoader {

    @Override
    public ExchangeRate load(Currency from, Currency to) {
        return new ExchangeRate(readRate(read(from, to)), from, to);
    }
    
    private String read(Currency from, Currency to) {
        URL url = null;
        try {
            url = new URL("https://cdn.jsdelivr.net/gh/fawazahmed0/currency-api@1/latest/currencies/" + 
                    from.getCode().toLowerCase() + "/" + to.getCode().toLowerCase() + ".json");
        } catch (MalformedURLException ex) {
            Logger.getLogger(WebServiceExchangeRateLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        BufferedReader in = null;
        String line = null;
        try {
            in = new BufferedReader(new InputStreamReader(url.openStream()));
            while ((line = in.readLine()) != null) {
                if (line.contains(to.getCode().toLowerCase())) break;
            }
            in.close();
        } catch (IOException ex) {
            Logger.getLogger(WebServiceExchangeRateLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return line;
    }
    
   private Double readRate(String line) {
       return Double.parseDouble(line.split(": ")[1]);
   }
}
