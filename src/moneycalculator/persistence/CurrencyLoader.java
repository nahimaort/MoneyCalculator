package moneycalculator.persistence;

import java.util.List;
import moneycalculator.model.Currency;

public interface CurrencyLoader {
    List<Currency> load();
}
