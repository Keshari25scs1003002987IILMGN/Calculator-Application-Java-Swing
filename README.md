# Calculator Application (Java Swing)

A GUI calculator built with Java Swing. The calculation logic is separated from the interface and uses `BigDecimal`, so results like `0.1 + 0.2` correctly give `0.3`.

## Features
- Addition, subtraction, multiplication, division
- Percent, square root, sign change (+/-), decimal numbers
- Clear and backspace
- Shows the pending operation above the main display
- Divide-by-zero and invalid-input handling
- Full keyboard support (digits, `+ - * /`, `Enter`, `Backspace`, `C`)

## Project Structure
```
src/com/cyrostack/calculator/
├── Main.java               # starts the app
├── CalculatorGUI.java      # Swing window and buttons
└── CalculatorEngine.java   # all calculation logic (no GUI code)
test/
└── CalculatorEngineTest.java   # 14 self-checking tests
```

## How to Run (JDK 8+)
```bash
mkdir out
javac -d out src/com/cyrostack/calculator/*.java
java -cp out com.cyrostack.calculator.Main
```

### Run the tests
```bash
javac -cp out -d out test/CalculatorEngineTest.java
java -cp out CalculatorEngineTest
```

## Author
Your Name - Cyrostack Internship Project
