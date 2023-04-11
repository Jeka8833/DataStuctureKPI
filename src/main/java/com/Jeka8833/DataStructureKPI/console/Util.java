package com.Jeka8833.DataStructureKPI.console;

import com.Jeka8833.DataStructureKPI.work.ExitException;
import com.Jeka8833.DataStructureKPI.work.StepDownException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Scanner;

public class Util {

    private static final Scanner SCANNER = new Scanner(System.in);

    @NotNull
    @Contract(pure = true, value = "_ -> new")
    public static String read(@NotNull String print) throws ExitException, StepDownException {
        try {
            if (!print.isEmpty()) System.out.print(print);

            final String text = SCANNER.nextLine();
            if (text.equalsIgnoreCase("exit")) throw new ExitException();
            if (text.equalsIgnoreCase("break")) throw new StepDownException();

            return text;
        } catch (Exception exception) {
            return "";
        }
    }

    @NotNull
    @Contract(pure = true, value = " -> new")
    public static String read() throws ExitException, StepDownException {
        return read("");
    }

    @Contract(pure = true)
    public static int readInt(@NotNull String print, int min, int max) throws ExitException, StepDownException {
        while (true) {
            String text = read(print);
            try {
                int value = Integer.parseInt(text);
                if (value >= min && value <= max) return value;

                System.out.println("Incorrect input value, value should be in range " +
                        "[" + min + "; " + max + "], try again");
            } catch (Exception exception) {
                System.out.println("Incorrect value, try again");
            }
        }
    }

    @Contract(pure = true)
    public static int readIntRange(@NotNull String print) throws ExitException, StepDownException {
        return readInt(print, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    @Contract(pure = true)
    public static int readInt(int min, int max) throws ExitException, StepDownException {
        return readInt("", min, max);
    }

    @Contract(pure = true)
    public static int readInt() throws ExitException, StepDownException {
        return readInt("", Integer.MIN_VALUE, Integer.MAX_VALUE);
    }
}
