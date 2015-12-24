package com.company;

import java.util.*;

/* Utility class to generate comparator functions, aiding in functional-style programming. */
class ComparatorFactory {

    // Ensure non-instantiation
    private ComparatorFactory(){}

    /* Factory method to generate a character comparator. The complexity of the algorithm
       is O(order.length()) as the algorithm runs order.indexOf on both characters to be
       compared, and indexOf is linear time on the order.length() as it traverses the String
       linearly to find the character. Because the alphabet is fixed at 'a'-'z', the order
       string has a O(1) <= 26 order constant size.
     */
    public static Comparator<Character> generateCharComparator(final String order) {
        return new Comparator<Character>() {
            public int compare(Character a, Character b){
                /* If I were to implement this manually, I would do a single run across the
                   order String until I find either a or b, but I favor using built-in methods (like indexOf)
                   in my practice when efficiency is not a concern (as in this case, where the
                   alphabet is a small size).
                 */
                int indexA = order.indexOf(a);
                int indexB = order.indexOf(b);
                if (indexA < 0 || indexB < 0) {
                    throw new IllegalArgumentException("Character not in lexicographic alphabet string");
                }
                return indexA - indexB;
            }
        };
    }

    /* Factory method to compare two strings. This is a linear time algorithm on the minimum of the two String sizes
       as the algorithm simply walks along each String one at a time in parallel, halting whenever it finds a differing
       character or when one of the Strings ends.
     */
    public static Comparator<String> generateStringComparator(final Comparator<Character> comparator) {
        return new Comparator<String>() {
            public int compare(String a, String b) {
                for (int i=0; i < a.length() && i < b.length(); i++) {
                    int charDiff = comparator.compare(a.charAt(i), b.charAt(i));
                    // charDiff != 0 means the character differs at this position. The first time it happens
                    // determines automatically which of the two strings is larger, so return here.
                    if (charDiff != 0) return charDiff;
                }
                // One string is a prefix of the other, so the larger string is simply the larger one in length.
                // Return the length difference.
                return a.length() - b.length();
            }
        };
    }
}

public class Main {


    public static void main(String[] args) {
        String order = "cba";
        String[] strArray = new String[] {"acb", "abc", "bca"};
        runFunctionAndPrint(strArray, order);
        order = "abc";
        runFunctionAndPrint(strArray, order);
        order = "a";
        strArray = new String[] {"aaa", "a", "aaaaa", "aa" };
        runFunctionAndPrint(strArray, order);
        order = "hateg";
        strArray = new String[] {"hehha", "tgah", "aa", "h", "gh", "gg", "gga", "eagt", "hhhhhhhhh" };
        runFunctionAndPrint(strArray, order);
    }

    /* Simple test function. Normally I would move the test cases inside the main method into JUnit test cases. */
    private static void runFunctionAndPrint(String[] strArray, String order) {
        List<String> strings = new ArrayList<String>();
        for (String str : strArray) {
            strings.add(str);
        }
        orderStrings(strings, order);
        System.out.println(strings);
    }

    /* The main algorithm. the complexity here is O(P * Nlog(N)), where N is the number of Strings in the input
       and P is the expected length of the strings. That is because the Collections.sort algorithm used here is NlogN
       based on the size of the input, but in our case the comparison is not O(1) but instead takes O(P) time where
       P is the order of the length of the strings.
     */
    private static void orderStrings(List<String> strings, String order) {
        Comparator<Character> charCompare = ComparatorFactory.generateCharComparator(order);
        Comparator<String> stringCompare = ComparatorFactory.generateStringComparator(charCompare);
        Collections.sort(strings, stringCompare);
    }


}
