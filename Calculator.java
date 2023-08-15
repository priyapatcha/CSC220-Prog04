package prog04;

import java.util.Stack;
import java.util.Scanner;

import javax.swing.SwingUtilities;

import prog02.UserInterface;
import prog02.GUI;
import prog02.ConsoleUI;

public class Calculator {
  static final String OPERATORS = "()+-*/u^";
  static final int[] PRECEDENCE = { -1, -1, 1, 1, 2, 2, 3, 4 };

  Stack<Character> operatorStack = new Stack<Character>();
  Stack<Double> numberStack = new Stack<Double>();
  UserInterface ui = new GUI("Calculator");

  static boolean previousTokenWasNumberOrRightParenthesis = false;

  Calculator (UserInterface ui) { this.ui = ui; }

  void emptyStacks () {
    while (!numberStack.empty())
      numberStack.pop();
    while (!operatorStack.empty())
      operatorStack.pop();
  }

  String numberStackToString () {
    String s = "numberStack: ";
    Stack<Double> helperStack = new Stack<Double>();
    // EXERCISE
    // Put every element of numberStack into helperStack

    while (!numberStack.empty()) {
      double numObj = numberStack.pop();
      helperStack.push(numObj);
    }

    while (!helperStack.empty()) {
      double helpObj = helperStack.pop();

      s = s + " " + helpObj;

      numberStack.push(helpObj);
    }
    // You will need to use a loop.  What kind?
    // What condition? When can you stop moving elements out of numberStack?
    // What method do you use to take an element out of numberStack?
    // What method do you use to put that element into helperStack?



    // Now put everything back, but also add each one to s:
    // s = s + " " + number;



    return s;
  }

  String operatorStackToString () {
    String s = "operatorStack: ";
    // EXERCISE
    Stack<Character> operatorHelperStack = new Stack<>();

    while (!operatorStack.empty()) {
      Character operatorObj = operatorStack.pop();
      operatorHelperStack.push(operatorObj);
    }

    while (!operatorHelperStack.empty()) {
      Character opHelpObj = operatorHelperStack.pop();

      s = s + " " + opHelpObj;

      operatorStack.push(opHelpObj);
    }

    return s;
  }

  void displayStacks () {
    ui.sendMessage(numberStackToString() + "\n" +
                   operatorStackToString());
  }

  void doNumber (double x) {
    numberStack.push(x);
    displayStacks();
    previousTokenWasNumberOrRightParenthesis = true;
  }

  void doOperator (char op) {
    if (op == '-' && !previousTokenWasNumberOrRightParenthesis) {
      processOperator('u');
    }
    else {
      processOperator(op);
    }
    displayStacks();

    if (op == ')') {
      previousTokenWasNumberOrRightParenthesis = true;
    }
    else previousTokenWasNumberOrRightParenthesis = false;
  }

  double doEquals () {
    while (!operatorStack.empty())
      evaluateTopOperator();

    return numberStack.pop();
  }

  static Integer precedence(char op) {
    int userOperator = OPERATORS.indexOf(op);
    return PRECEDENCE[userOperator];
  }
    
  double evaluateOperator (double a, char op, double b) {
    switch (op) {
    case '+':
      return a + b;
    case '-':
      return a - b;
    case '*':
        return a*b;
    case '/':
        return a/b;
    case '^':
        return Math.pow(a,b);
      // EXERCISE
    }
    System.out.println("Unknown operator " + op);
    return 0;
  }

  void evaluateTopOperator () {
    char op = operatorStack.pop();
    // EXERCISE
    if (op == 'u') {
      double a = numberStack.pop();
      numberStack.push(-a);
    }
    else {
      double num1 = numberStack.pop();
      double num2 = numberStack.pop();

      double result = evaluateOperator(num2, op, num1);

      numberStack.push(result);
    }

    displayStacks();
    return;
  }

  void processOperator (char op) {

    if (op == '(') {
      operatorStack.push(op);
    }
    else if (op == ')') {
      while (operatorStack.peek() != '(') {
        evaluateTopOperator();
      }
      operatorStack.pop();
    }
    else if (op == 'u'){
      operatorStack.push(op);
    }
    else {
      int opPrecedence = precedence(op);

      while (operatorStack.empty() != true && precedence(operatorStack.peek()) >= precedence(op)) {
        evaluateTopOperator();
      }
      operatorStack.push(op);
    }
  }
  
  static boolean checkTokens (UserInterface ui, Object[] tokens) {
      for (Object token : tokens)
        if (token instanceof Character &&
            OPERATORS.indexOf((Character) token) == -1) {
          ui.sendMessage(token + " is not a valid operator.");
          return false;
        }
      return true;
  }

  static void processExpressions (UserInterface ui, Calculator calculator) {
    while (true) {
      previousTokenWasNumberOrRightParenthesis = false;
      String line = ui.getInfo("Enter arithmetic expression or cancel.");
      if (line == null)
        return;
      Object[] tokens = Tokenizer.tokenize(line);
      if (!checkTokens(ui, tokens))
        continue;
      try {
        for (Object token : tokens)
          if (token instanceof Double)
            calculator.doNumber((Double) token);
          else          
            calculator.doOperator((Character) token);
        double result = calculator.doEquals();
        ui.sendMessage(line + " = " + result);
      } catch (Exception e) {
        ui.sendMessage("Bad expression.");
        calculator.emptyStacks();
      }
    }
  }

  public static void main (String[] args) {
    UserInterface ui = new ConsoleUI();
    Calculator calculator = new Calculator(ui);
    processExpressions(ui, calculator);
  }
}
