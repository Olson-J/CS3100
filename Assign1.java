/** 
 * Assignment 1 for CS3100
 * @author Julie Olson
 * a02363064
 */

import java.math.BigInteger;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class Assign1 {
    /**
     * This class contains code to produce a Fibonacci value, factorial, or 'e'
     * value based on user input from the command line. When given invalid
     * input, the program will print a help message.
     */
    public static void main(String[] args) {
        /**
         * This method checks that the user input contains at least two 
         * arguments. If so, it will call the checkArgs method to validate
         * those arguments. If not, it will print a help message.
         */
        if (args.length >= 2 && args.length % 2 == 0){
            checkArgs(args, 0);
        }
        else {
            printHelp();
        }
    }

    static void checkArgs(String[] args, int zero) {
        /**
         * this method will check the given arguments for valid 
         * computation requests and numerical input. If more than once 
         * computation was requested, address the first computation 
         * and then re-process the remaining arguments.
         */
        switch (args[zero]) {
            // check that the first argument is a valid command
            case "-fib":
                try {
                    // check that the second argument is within the acceptable range
                    int n = Integer.parseInt(args[zero + 1]);
                    if (n >= 0 && n <= 40) {
                        findFib(n);
                    }
                    else {
                        System.out.println("Valid range for Fibonacci is [0, 40]");
                    }
                } 
                catch (NumberFormatException e) {
                    printHelp();
                }
                break;
            case "-fac":
                try {
                    // check that the second argument is within the acceptable range
                    int n = Integer.parseInt(args[zero + 1]);
                    if (n >= 0 && n <= 2147483647) {
                        findFac(n);
                    }
                    else {
                        System.out.println("Valid range for factorial is [0, 2147483647]");
                    }
                } 
                catch (NumberFormatException e) {
                    printHelp();
                }
                break;
            case "-e":
                try {
                    // check that the second argument is within the acceptable range
                    int n = Integer.parseInt(args[ zero + 1]);
                    if (n >= 1 && n <= 2147483647) {
                        findE(n);
                    }
                    else {
                        System.out.println("Valid range for 'e' is [1, 2147483647]");
                    }
                } 
                catch (NumberFormatException e) {
                    printHelp();
                }
                break;
            default:
                System.out.println("Unknown command line argument: " + args[zero]);
                break;
        }

        // check for additional arguments
        if (args.length > 2 && args.length > zero + 2) {
            checkArgs(args, zero + 2);
        }
    }

    static void findFib(int x) {
        /**
         * This method computes the Fibonacci value of the given input
         * using the sequence beginning with f0 = 1, f1 = 1.
         */
        if (x == 0 || x == 1) {
            System.out.println("Fibonacci of " + x + " is " + 1);
            return;
        }

        int fib = 1;
        int prevFib = 1;

        // compute the Fibonacci value by adding the previous two values
        for (int i = 2; i <= x; i++) {
            int temp = fib;
            fib += prevFib;
            prevFib = temp;
        }
        System.out.println("Fibonacci of " + x + " is " + fib);
    }

    static void findFac(int x) {
        /**
         * This method computes the factorial of the given input
         * using BigInteger values for precision.
         */
        BigInteger fac = BigInteger.valueOf(1);
        for (int i = 1; i <= x; i++) {
            fac = fac.multiply(BigInteger.valueOf(i));
        }
        System.out.println("Factorial of " + x + " is " + fac);
    }

    static void findE(int x) {
        /**
         * This method computes the value of 'e' using the given input
         * as the number of iterations [of a Taylor series] to use.
         * follows the formula: e^x = 1 + x/1! + x^2/2! + x^3/3! + ... + x^n/n!
         */

        BigDecimal e = BigDecimal.ONE;
        BigDecimal factorial = BigDecimal.ONE;

        for (int i = 1; i < x; i++) {
            factorial = factorial.multiply(BigDecimal.valueOf(i));
            BigDecimal term = BigDecimal.ONE.divide(factorial, 16, RoundingMode.HALF_UP);
            e = e.add(term);
        }
        System.out.println("Value of e using " + x + " iterations is " + e);
    }

    static void printHelp() {
        /**
         * This method prints out a help message that outlines the possible 
         * valid computations and their valid ranges.
         */
        System.out.println("--- Assign 1 Help ---");
        System.out.println("   -fib [n] : Compute the Fibonacci of [n]; valid range [0, 40]");
        System.out.println("   -fac [n] : Compute the factorial of [n]; valid range [0, 2147483647]");
        System.out.println("   -e [n] : Compute the value of 'e' using [n] interations; valid range [1, 2147483647]");
    }
}