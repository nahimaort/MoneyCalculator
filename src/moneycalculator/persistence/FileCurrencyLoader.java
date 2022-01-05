package moneycalculator.persistence;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import moneycalculator.model.Currency;

public class FileCurrencyLoader implements CurrencyLoader {
    private final String fileName;

    public FileCurrencyLoader(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public List<Currency> load() {
        List<Currency> currencies = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(this.fileName));
            while (true) {
                String line = reader.readLine();
                if (line == null)
                    break;
                currencies.add(stringToCurrency(line));
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileCurrencyLoader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FileCurrencyLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return currencies;
    }

    private Currency stringToCurrency(String line) {
        String[] parameters = line.split(", ");
        return new Currency(parameters[0], parameters[1], parameters[2]);
    }
}
