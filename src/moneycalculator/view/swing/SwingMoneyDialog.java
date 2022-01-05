package moneycalculator.view.swing;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import moneycalculator.controller.Command;
import moneycalculator.controller.ExchangeCommand;
import moneycalculator.model.Currency;
import moneycalculator.model.Money;
import moneycalculator.persistence.ExchangeRateLoader;
import moneycalculator.view.MoneyDialog;
import moneycalculator.view.MoneyDisplay;

public class SwingMoneyDialog extends JPanel implements MoneyDialog {
    private final List<Currency> currencies;
    private Currency currencyFrom;
    private Currency currencyTo;
    private String amount;
    private final Command command;
    private final ExchangeRateLoader loader;
    private final MoneyDisplay display;

    public SwingMoneyDialog(List<Currency> currencies, ExchangeRateLoader loader, MoneyDisplay display) {
        this.currencies = currencies;
        this.loader = loader;
        this.display = display;
        this.command = new ExchangeCommand(this.loader, this, this.display);
        this.add(getAmount());
        this.add(getCurrencyFrom());
        this.add(getCurrencyTo());
    }
    
    @Override
    public Money getFrom() {
        Money money = null;
        try {
            money = new Money(Double.parseDouble(amount), currencyFrom);
        } catch (Exception e) {
            amount = String.valueOf(0);
            money = new Money(Double.parseDouble(amount), currencyFrom);
        }
        return money;
    }
    
    @Override
    public Currency getTo() {
        return currencyTo;
    }

    private JTextField getAmount() {
        JTextField textField = new JTextField("");
        textField.setColumns(10);
        textField.getDocument().addDocumentListener(newAmount());
        amount = textField.getText();
        return textField;
    }

    private JComboBox getCurrencyFrom() {
        JComboBox comboBox = new JComboBox(getCurrencies());
        comboBox.addItemListener(newCurrencyFrom());
        currencyFrom = (Currency) comboBox.getSelectedItem();
        return comboBox;
    }
    
    private JComboBox getCurrencyTo() {
        JComboBox comboBox = new JComboBox(getCurrencies());
        comboBox.addItemListener(newCurrencyTo());
        currencyTo = (Currency) comboBox.getSelectedItem();
        return comboBox;
    }

    private Object[] getCurrencies() {
        return this.currencies.toArray();
    }

    private DocumentListener newAmount() {
        return new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateAmount(e.getDocument());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateAmount(e.getDocument());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateAmount(e.getDocument());
            }
            
            private void updateAmount(Document document) {
                try {
                    amount = document.getText(0, document.getLength());
                    if (amount.equals("")) {
                        amount = String.valueOf(0);
                    }
                    command.execute();
                } catch (BadLocationException ex) {
                    Logger.getLogger(SwingMoneyDialog.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
    }

    private ItemListener newCurrencyFrom() {
        return (ItemEvent e) -> {
            if (e.getStateChange() == ItemEvent.DESELECTED) return;
            currencyFrom = (Currency) e.getItem();
            command.execute();
        };
    }
    
    private ItemListener newCurrencyTo() {
        return (ItemEvent e) -> {
            if (e.getStateChange() == ItemEvent.DESELECTED) return;
            currencyTo = (Currency) e.getItem();
            command.execute();
        };
    }

}