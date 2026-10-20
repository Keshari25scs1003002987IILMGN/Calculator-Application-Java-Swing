package com.cyrostack.calculator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class CalculatorGUI extends JFrame {
    private static final Color BG = new Color(0x1E1E2E);
    private static final Color NUM = new Color(0x313244);
    private static final Color OP = new Color(0xFAB387);
    private static final Color FN = new Color(0x45475A);
    private static final Color EQ = new Color(0xA6E3A1);

    private final CalculatorEngine engine = new CalculatorEngine();
    private final JLabel expressionLabel = new JLabel(" ", SwingConstants.RIGHT);
    private final JLabel displayLabel = new JLabel("0", SwingConstants.RIGHT);

    public CalculatorGUI() {
        super("Calculator");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(0, 8));
        getContentPane().setBackground(BG);
        ((JComponent) getContentPane()).setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        add(buildDisplay(), BorderLayout.NORTH);
        add(buildButtons(), BorderLayout.CENTER);
        setupKeyboard();

        setSize(340, 520);
        setMinimumSize(new Dimension(300, 460));
        setLocationRelativeTo(null);
    }

    private JPanel buildDisplay() {
        JPanel p = new JPanel(new GridLayout(2, 1));
        p.setBackground(BG);
        expressionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        expressionLabel.setForeground(new Color(0xA6ADC8));
        displayLabel.setFont(new Font("Segoe UI", Font.BOLD, 38));
        displayLabel.setForeground(Color.WHITE);
        p.add(expressionLabel);
        p.add(displayLabel);
        p.setPreferredSize(new Dimension(300, 95));
        return p;
    }

    private JPanel buildButtons() {
        String[][] layout = {
            {"C", "\u232B", "\u221A", "%"},
            {"7", "8", "9", "\u00F7"},
            {"4", "5", "6", "\u00D7"},
            {"1", "2", "3", "\u2212"},
            {"\u00B1", "0", ".", "+"},
            {"=", "", "", ""}
        };
        JPanel grid = new JPanel(new GridBagLayout());
        grid.setBackground(BG);
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 1;
        c.insets = new Insets(4, 4, 4, 4);

        for (int r = 0; r < layout.length; r++) {
            for (int col = 0; col < 4; col++) {
                String label = layout[r][col];
                if (label.isEmpty()) continue;
                c.gridx = col;
                c.gridy = r;
                c.gridwidth = label.equals("=") ? 4 : 1;
                grid.add(makeButton(label), c);
            }
        }
        return grid;
    }

    private JButton makeButton(String label) {
        JButton b = new JButton(label);
        b.setFont(new Font("Segoe UI", Font.BOLD, 20));
        b.setFocusPainted(false);
        b.setFocusable(false);
        b.setBorderPainted(false);
        b.setOpaque(true);
        b.setForeground(Color.WHITE);
        b.setBackground(NUM);
        if ("+\u2212\u00D7\u00F7".contains(label)) { b.setBackground(OP); b.setForeground(BG); }
        else if ("C\u232B\u221A%\u00B1".contains(label)) b.setBackground(FN);
        else if (label.equals("=")) { b.setBackground(EQ); b.setForeground(BG); }
        b.addActionListener(e -> press(label));
        return b;
    }

    private void press(String label) {
        switch (label) {
            case "C": engine.clear(); break;
            case "\u232B": engine.backspace(); break;
            case "\u221A": engine.squareRoot(); break;
            case "%": engine.percent(); break;
            case "\u00F7": engine.operator('/'); break;
            case "\u00D7": engine.operator('*'); break;
            case "\u2212": engine.operator('-'); break;
            case "+": engine.operator('+'); break;
            case "\u00B1": engine.negate(); break;
            case ".": engine.decimalPoint(); break;
            case "=": engine.equals(); break;
            default: engine.digit(label.charAt(0) - '0');
        }
        refresh();
    }

    private void refresh() {
        String text = engine.display();
        displayLabel.setText(text);
        displayLabel.setFont(displayLabel.getFont().deriveFont(text.length() > 12 ? 24f : 38f));
        String ex = engine.expression();
        expressionLabel.setText(ex.isEmpty() ? " " : ex);
    }

    private void setupKeyboard() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char ch = e.getKeyChar();
                if (Character.isDigit(ch)) engine.digit(ch - '0');
                else if (ch == '.') engine.decimalPoint();
                else if (ch == '+' || ch == '-' || ch == '*' || ch == '/') engine.operator(ch);
                else if (ch == '=' || ch == '\n') engine.equals();
                else if (ch == '%') engine.percent();
                else if (ch == '\b') engine.backspace();
                else if (ch == 'c' || ch == 'C' || ch == 27) engine.clear();
                else return;
                refresh();
            }
        });
        setFocusable(true);
    }
}
