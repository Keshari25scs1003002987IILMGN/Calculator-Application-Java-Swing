package com.cyrostack.calculator;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * Calculator logic, completely separate from the GUI so it can be tested on its own.
 * Uses BigDecimal so that 0.1 + 0.2 gives 0.3 (not 0.30000000000000004).
 */
public class CalculatorEngine {
    private static final MathContext MC = new MathContext(12, RoundingMode.HALF_UP);
    private static final int MAX_DIGITS = 15;

    private String entry = "0";          // number currently being typed / shown
    private BigDecimal accumulator = BigDecimal.ZERO;
    private char pendingOp = 0;          // + - * / or 0 when none
    private boolean startNewEntry = true;
    private boolean error = false;
    private String errorMessage = "";

    // ---- input ----
    public void digit(int d) {
        if (error) clear();
        if (startNewEntry || entry.equals("0")) {
            entry = String.valueOf(d);
            startNewEntry = false;
        } else if (entry.replace("-", "").replace(".", "").length() < MAX_DIGITS) {
            entry += d;
        }
    }

    public void decimalPoint() {
        if (error) clear();
        if (startNewEntry) { entry = "0."; startNewEntry = false; }
        else if (!entry.contains(".")) entry += ".";
    }

    public void operator(char op) {
        if (error) return;
        if (pendingOp != 0 && !startNewEntry) {
            if (!applyPending()) return;
        } else {
            accumulator = value();
        }
        pendingOp = op;
        startNewEntry = true;
    }

    public void equals() {
        if (error || pendingOp == 0) return;
        if (applyPending()) {
            pendingOp = 0;
            startNewEntry = true;
        }
    }

    public void clear() {
        entry = "0";
        accumulator = BigDecimal.ZERO;
        pendingOp = 0;
        startNewEntry = true;
        error = false;
        errorMessage = "";
    }

    public void backspace() {
        if (error) { clear(); return; }
        if (startNewEntry) return;
        entry = entry.substring(0, entry.length() - 1);
        if (entry.isEmpty() || entry.equals("-")) { entry = "0"; startNewEntry = true; }
    }

    public void negate() {
        if (error || value().signum() == 0) return;
        entry = entry.startsWith("-") ? entry.substring(1) : "-" + entry;
    }

    public void percent() {
        if (error) return;
        setResult(value().divide(BigDecimal.valueOf(100), MC));
    }

    public void squareRoot() {
        if (error) return;
        BigDecimal v = value();
        if (v.signum() < 0) { fail("Invalid input"); return; }
        setResult(v.sqrt(MC));
    }

    // ---- output ----
    public String display() { return error ? errorMessage : entry; }

    public String expression() {
        if (error || pendingOp == 0) return "";
        return format(accumulator) + " " + symbol(pendingOp);
    }

    public boolean hasError() { return error; }

    // ---- internals ----
    private boolean applyPending() {
        BigDecimal right = value();
        BigDecimal result;
        switch (pendingOp) {
            case '+': result = accumulator.add(right); break;
            case '-': result = accumulator.subtract(right); break;
            case '*': result = accumulator.multiply(right); break;
            case '/':
                if (right.signum() == 0) { fail("Cannot divide by zero"); return false; }
                result = accumulator.divide(right, MC);
                break;
            default: return true;
        }
        result = result.round(MC);
        accumulator = result;
        entry = format(result);
        startNewEntry = true;
        return true;
    }

    private void setResult(BigDecimal v) {
        entry = format(v);
        startNewEntry = true;
    }

    private BigDecimal value() {
        try { return new BigDecimal(entry.endsWith(".") ? entry + "0" : entry); }
        catch (NumberFormatException e) { return BigDecimal.ZERO; }
    }

    private void fail(String msg) {
        error = true;
        errorMessage = msg;
        pendingOp = 0;
    }

    static String format(BigDecimal v) {
        if (v.signum() == 0) return "0";
        return v.stripTrailingZeros().toPlainString();
    }

    private static String symbol(char op) {
        switch (op) {
            case '*': return "\u00D7";
            case '/': return "\u00F7";
            case '-': return "\u2212";
            default: return String.valueOf(op);
        }
    }
}
