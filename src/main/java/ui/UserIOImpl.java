package ui;

import java.util.Scanner;

public class UserIOImpl implements UserIO{
    Scanner sc = new Scanner(System.in);
    @Override
    public void print(String message) {
        System.out.println(message);
    }

    @Override
    public String readString(String prompt) {
        print(prompt);
        return sc.nextLine();
    }

    @Override
    public Double readDouble(String prompt) {
        print(prompt);
        return Double.parseDouble(sc.nextLine());
    }

    @Override
    public char readChar(String prompt) {
        print(prompt);
        char result = ' ';

        while (result == ' ') {
            String input = sc.nextLine().trim();

            if (input.length() == 1) {
                result = input.charAt(0);
            } else {
                System.out.print("Please enter a single character: ");
            }
        }

        return result;
    }
}