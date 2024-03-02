package dbprak20.util;

import java.util.Scanner;

public class Utility {
    /**
     * Get right input from user from min to max with message
     *
     * @param min min value
     * @param max max value
     * @param text message
     * @return long as input
     */
    public static long validateInput(long min, long max, String text) {
        boolean exit = false;
        long out = 0;
        if(text.isEmpty())
            text = "Please type a number";

        Scanner scanner = new Scanner(System.in);
        do {
            System.out.println(text);
            boolean isNumber = true;
            String s = scanner.next().trim();
            char[] charArr = s.toCharArray();

            //Check if it's a number
            for(char ch: charArr) {
                if(!Character.isDigit(ch)) {
                    isNumber = false;
                    break;
                }
            }
            if(isNumber) {
                out = Long.parseLong(String.copyValueOf(charArr));

                if(out >=min && out <= max)
                    exit = true;
                else
                    System.out.println("Not valid number. The valid number is between " + min +" and " + max);
            } else {
                System.out.println("This is not a number");
            }
        } while(!exit);

        return out;
    }
}
