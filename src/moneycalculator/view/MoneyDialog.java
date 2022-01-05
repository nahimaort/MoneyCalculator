package moneycalculator.view;

import moneycalculator.model.Currency;
import moneycalculator.model.Money;

public interface MoneyDialog {
    Currency getTo();
    Money getFrom();
}
