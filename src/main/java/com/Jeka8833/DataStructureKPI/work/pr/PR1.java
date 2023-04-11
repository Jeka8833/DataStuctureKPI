package com.Jeka8833.DataStructureKPI.work.pr;

import com.Jeka8833.DataStructureKPI.work.Task;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.DoubleFunction;
import java.util.function.IntFunction;

public class PR1 implements Task {
    private static final Path OUT_PATH = Paths.get("DP0102");

    private static final double START_POS = 0;
    private static final double END_POS = Math.PI / 4;
    private static final IntFunction<Double> STEP = value -> value / 40d * Math.PI;
    private static final DoubleFunction<Double> FUNCTION = value -> Math.sin(Math.sin(value * value)) * value;

    private static final double MAX_ERROR =
            (Math.abs(STEP.apply(1) - STEP.apply(0))) / 2; // Only for linear step

    @Override
    public void run() {
        StringBuilder stringBuilder = new StringBuilder();
        int i = 0;
        while (true) {
            double input = START_POS + STEP.apply(i++);
            if (input - END_POS > MAX_ERROR) break;

            double output = FUNCTION.apply(input);

            stringBuilder.append(new BigDecimal(input)).append(' ').append(new BigDecimal(output)).append('\n');
        }
        try {
            Files.write(OUT_PATH, stringBuilder.toString().getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @NotNull
    @Override
    public String name() {
        return "Практикум №1 Робота з файлами в С++.";
    }
}
