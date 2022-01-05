package moneycalculator;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import moneycalculator.controller.Command;
import moneycalculator.controller.ExchangeCommand;
import moneycalculator.model.Currency;
import moneycalculator.persistence.ExchangeRateLoader;
import moneycalculator.view.MoneyDisplay;
import moneycalculator.view.swing.SwingMoneyDialog;
import moneycalculator.view.swing.SwingMoneyDisplay;

public class MainFrame extends JFrame {
    private MoneyDisplay display;
    private SwingMoneyDialog dialog;
    private final List<Currency> currencies;
    private final ExchangeRateLoader loader;
    private Command command;
    
    
    public MainFrame(List<Currency> currencies, ExchangeRateLoader loader) {
        this.currencies = currencies;
        this.loader = loader;
        this.setTitle("Money Calculator");
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setSize(400, 200);
        this.setLocationRelativeTo(null);
        this.add(moneyDisplay(), BorderLayout.CENTER);
        this.add(moneyDialog(), BorderLayout.NORTH);
        this.setVisible(true);
    }

    public MoneyDisplay getDisplay() {
        return display;
    }

    public SwingMoneyDialog getDialog() {
        return dialog;
    }
    
    private Component moneyDisplay() {
        SwingMoneyDisplay moneyDisplay = new SwingMoneyDisplay();
        this.display = moneyDisplay;
        return moneyDisplay;
    }
    
    private Component moneyDialog() {
        SwingMoneyDialog moneyDialog = new SwingMoneyDialog(this.currencies, this.loader, display);
        this.dialog = moneyDialog;
        return moneyDialog;
    }
}
