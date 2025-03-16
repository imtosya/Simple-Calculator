import java.util.*;

public class Main {

    private static List<String> history = new ArrayList<>();  // To store past calculations

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean continueCalculator = true;

        while (continueCalculator) {
            System.out.println("Welcome to the Calculator!");
            System.out.print("Please enter your arithmetic expression: ");
            String expression = scanner.nextLine().replaceAll(" ", "");

            // Check if the user wants to exit the program
            if (expression.equalsIgnoreCase("exit")) {
                System.out.println("Thank you for using the Calculator!");
                continueCalculator = false;
                break;
            }

            // Handle history feature
            if (expression.equalsIgnoreCase("history")) {
                showHistory();
                continue;
            }

            try {
                // Evaluate functions like abs(), pow(), sqrt(), round() first, then process other arithmetic operations
                expression = processFunctions(expression);

                // Initialize a stack for operators and another one for operands
                Stack<Character> operators = new Stack<>();
                Stack<Double> operands = new Stack<>();
                StringBuilder number = new StringBuilder();

                // Loop through the expression
                for (int i = 0; i < expression.length(); i++) {
                    char c = expression.charAt(i);

                    // If the character is a digit or a decimal point, build the number
                    if (Character.isDigit(c) || c == '.') {
                        number.append(c);
                    } else {
                        // If there's a number being built, add it to the operands stack
                        if (number.length() > 0) {
                            operands.push(Double.parseDouble(number.toString()));
                            number.setLength(0); // Reset the number builder
                        }

                        // If it's an opening parenthesis, push to operators stack
                        if (c == '(') {
                            operators.push(c);
                        }
                        // If it's a closing parenthesis, pop from stack until an opening parenthesis is found
                        else if (c == ')') {
                            while (!operators.isEmpty() && operators.peek() != '(') {
                                char operation = operators.pop();
                                double b = operands.pop();
                                double a = operands.pop();
                                operands.push(performOperation(a, b, operation));
                            }
                            operators.pop(); // Pop the '('
                        }
                        // Otherwise, handle operators
                        else {
                            while (!operators.isEmpty() && precedence(operators.peek()) >= precedence(c)) {
                                char operation = operators.pop();
                                double b = operands.pop();
                                double a = operands.pop();
                                operands.push(performOperation(a, b, operation));
                            }
                            operators.push(c);
                        }
                    }
                }

                // If there's a number being built, add it to the operands stack
                if (number.length() > 0) {
                    operands.push(Double.parseDouble(number.toString()));
                }

                // Process remaining operations in the operator stack
                while (!operators.isEmpty()) {
                    char operation = operators.pop();
                    double b = operands.pop();
                    double a = operands.pop();
                    operands.push(performOperation(a, b, operation));
                }

                // Final result
                double result = operands.pop();
                System.out.println("Result: " + result);

                // Add result to history
                history.add(expression + " = " + result);

                // Ask the user whether they want to continue
                System.out.print("Do you want to continue? (y/n): ");
                String continueInput = scanner.nextLine().toLowerCase();
                if (continueInput.equals("n")) {
                    System.out.println("Thank you for using the Calculator!");
                    continueCalculator = false; // Set the flag to false to exit the loop
                }

            } catch (Exception e) {
                System.out.println("Invalid expression! Please check the format.");
            }
        }

        scanner.close();
    }

    // Method to determine precedence of operators
    public static int precedence(char operation) {
        if (operation == '*' || operation == '/' || operation == '%') return 2;
        if (operation == '+' || operation == '-') return 1;
        return 0;
    }

    // Method to perform the operation
    public static double performOperation(double a, double b, char operation) {
        switch (operation) {
            case '+': return a + b;
            case '-': return a - b;
            case '*': return a * b;
            case '/':
                if (b == 0) {
                    System.out.println("Error: Division by zero!");
                    return 0;
                }
                return a / b;
            case '%': return a % b;
            default:
                System.out.println("Invalid operator");
                return 0;
        }
    }

    // Method to process functions like abs(), pow(), sqrt(), round()
    public static String processFunctions(String expression) {
        // Handle abs()
        expression = handleAbs(expression);
        // Handle pow()
        expression = handlePow(expression);
        // Handle sqrt()
        expression = handleSqrt(expression);
        // Handle round()
        expression = handleRound(expression);

        return expression;
    }

    // Handle abs function
    private static String handleAbs(String expression) {
        while (expression.contains("abs")) {
            int start = expression.indexOf("abs");
            int end = expression.indexOf(')', start);
            String inside = expression.substring(start + 4, end);
            double value = Double.parseDouble(inside);
            expression = expression.substring(0, start) + Math.abs(value) + expression.substring(end + 1);
        }
        return expression;
    }

    // Handle pow function
    private static String handlePow(String expression) {
        while (expression.contains("pow")) {
            int start = expression.indexOf("pow");
            int end = expression.indexOf(')', start);
            String inside = expression.substring(start + 4, end);
            String[] parts = inside.split(",");
            double base = Double.parseDouble(parts[0]);
            double exponent = Double.parseDouble(parts[1]);
            expression = expression.substring(0, start) + Math.pow(base, exponent) + expression.substring(end + 1);
        }
        return expression;
    }

    // Handle sqrt function
    private static String handleSqrt(String expression) {
        while (expression.contains("sqrt")) {
            int start = expression.indexOf("sqrt");
            int end = expression.indexOf(')', start);
            String inside = expression.substring(start + 5, end);
            double value = Double.parseDouble(inside);
            expression = expression.substring(0, start) + Math.sqrt(value) + expression.substring(end + 1);
        }
        return expression;
    }

    // Handle round function
    private static String handleRound(String expression) {
        while (expression.contains("round")) {
            int start = expression.indexOf("round");
            int end = expression.indexOf(')', start);
            String inside = expression.substring(start + 6, end);
            double value = Double.parseDouble(inside);
            expression = expression.substring(0, start) + Math.round(value) + expression.substring(end + 1);
        }
        return expression;
    }

    // Method to display the history of past calculations
    public static void showHistory() {
        if (history.isEmpty()) {
            System.out.println("No history available.");
        } else {
            System.out.println("Calculation History:");
            for (String entry : history) {
                System.out.println(entry);
            }
        }
    }
}
