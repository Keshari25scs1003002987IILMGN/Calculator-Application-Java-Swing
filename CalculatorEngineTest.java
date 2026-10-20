import com.cyrostack.calculator.CalculatorEngine;

/** Simple self-checking tests (no JUnit needed). Run: java -cp out;test CalculatorEngineTest */
public class CalculatorEngineTest {
    private static int failures = 0;

    public static void main(String[] args) {
        check("2 + 3", "5", e -> { e.digit(2); e.operator('+'); e.digit(3); e.equals(); });
        check("0.1 + 0.2", "0.3", e -> { e.digit(0); e.decimalPoint(); e.digit(1); e.operator('+');
            e.digit(0); e.decimalPoint(); e.digit(2); e.equals(); });
        check("10 / 4", "2.5", e -> { e.digit(1); e.digit(0); e.operator('/'); e.digit(4); e.equals(); });
        check("1 / 3", "0.333333333333", e -> { e.digit(1); e.operator('/'); e.digit(3); e.equals(); });
        check("chained 2 + 3 * 4 (left to right)", "20", e -> { e.digit(2); e.operator('+'); e.digit(3);
            e.operator('*'); e.digit(4); e.equals(); });
        check("divide by zero", "Cannot divide by zero", e -> { e.digit(5); e.operator('/'); e.digit(0); e.equals(); });
        check("sqrt 81", "9", e -> { e.digit(8); e.digit(1); e.squareRoot(); });
        check("sqrt of negative", "Invalid input", e -> { e.digit(4); e.negate(); e.squareRoot(); });
        check("percent 50", "0.5", e -> { e.digit(5); e.digit(0); e.percent(); });
        check("negate", "-7", e -> { e.digit(7); e.negate(); });
        check("backspace 123 -> 12", "12", e -> { e.digit(1); e.digit(2); e.digit(3); e.backspace(); });
        check("only one decimal point", "1.5", e -> { e.digit(1); e.decimalPoint(); e.decimalPoint(); e.digit(5); });
        check("clear", "0", e -> { e.digit(9); e.operator('+'); e.digit(9); e.clear(); });
        check("result then new number", "7", e -> { e.digit(2); e.operator('+'); e.digit(2); e.equals(); e.digit(7); });
        System.out.println(failures == 0 ? "\nAll tests passed." : "\n" + failures + " test(s) FAILED.");
        System.exit(failures == 0 ? 0 : 1);
    }

    private static void check(String name, String expected, java.util.function.Consumer<CalculatorEngine> steps) {
        CalculatorEngine e = new CalculatorEngine();
        steps.accept(e);
        boolean ok = e.display().equals(expected);
        if (!ok) failures++;
        System.out.printf("%-4s %-36s -> %s%n", ok ? "PASS" : "FAIL", name, e.display());
    }
}
