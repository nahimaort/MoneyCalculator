package moneycalculator.view.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JLabel;
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
    private Command command;
    private MoneyDisplay display;
    private final ExchangeRateLoader loader;

    public SwingMoneyDialog(List<Currency> currencies, ExchangeRateLoader loader) {
        this.currencies = currencies;
        this.loader = loader;
        this.setBackground(new Color(255, 255, 255));
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
    
    public void setDisplay(MoneyDisplay moneyDisplay) {
        display = moneyDisplay;
        command = new ExchangeCommand(this.loader, this, this.display);
    }

    private JPanel getAmount() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(255, 255, 255));
        panel.setLayout(new BorderLayout());
        JLabel label = new JLabel("Amount:");
        label.setFont(new Font("Arial", 1, 14));
        label.setForeground(new Color(0, 51, 102));
        panel.add(label, BorderLayout.NORTH);
        JTextField textField = new JTextField("");
        textField.setColumns(10);
        panel.add(textField, BorderLayout.SOUTH);
        textField.getDocument().addDocumentListener(newAmount());
        amount = textField.getText();
        return panel;
    }

    private JPanel getCurrencyFrom() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(255, 255, 255));
        panel.setLayout(new BorderLayout());
        JLabel label = new JLabel("From:");
        label.setFont(new Font("Arial", 1, 14));
        label.setForeground(new Color(0, 51, 102));
        panel.add(label, BorderLayout.NORTH);
        JComboBox comboBox = new JComboBox(getCurrencies());
        panel.add(comboBox, BorderLayout.SOUTH);
        comboBox.addItemListener(newCurrencyFrom());
        currencyFrom = (Currency) comboBox.getSelectedItem();
        return panel;
    }
    
    private JPanel getCurrencyTo() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(255, 255, 255));
        panel.setLayout(new BorderLayout());
        JLabel label = new JLabel("To:");
        label.setFont(new Font("Arial", 1, 14));
        label.setForeground(new Color(0, 51, 102));
        panel.add(label, BorderLayout.NORTH);
        JComboBox comboBox = new JComboBox(getCurrencies());
        panel.add(comboBox, BorderLayout.SOUTH);
        comboBox.addItemListener(newCurrencyTo());
        currencyTo = (Currency) comboBox.getSelectedItem();
        return panel;
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