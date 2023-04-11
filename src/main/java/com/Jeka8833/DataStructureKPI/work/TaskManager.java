package com.Jeka8833.DataStructureKPI.work;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.ArrayList;
import java.util.List;

public class TaskManager {

    public static final List<Class<? extends Task>> TASK_LIST = new ArrayList<>();

    public static void registerTask(@NotNull Class<? extends Task> aClass) {
        TASK_LIST.add(aClass);
    }

    @Range(from = 0, to = Integer.MAX_VALUE)
    public static int size() {
        return TASK_LIST.size();
    }

    public static void printTaskList() {
        for (int i = 0; i < TASK_LIST.size(); i++) {
            Task task = createNewConstructor(i);
            if (task == null) {
                System.out.println("It is not possible to create a task, it's bug");
                return;
            }

            System.out.println((i + 1) + ": " + task.name());
        }
    }

    public static void startTask(@Range(from = 1, to = Integer.MAX_VALUE) int index)
            throws StepDownException, ExitException {

        Task task = createNewConstructor(index - 1);
        if (task == null) {
            System.out.println("It is not possible to create a task, it's bug");
            return;
        }

        System.out.println();
        System.out.println();
        System.out.println("===========================================");
        System.out.println("Starting: " + task.name());
        System.out.println("===========================================");
        System.out.println();

        task.run();
    }

    @Nullable
    @Contract(pure = true)
    private static Task createNewConstructor(@Range(from = 0, to = Integer.MAX_VALUE) int index) {
        try {
            return TASK_LIST.get(index).newInstance();
        } catch (Exception ignored) {
        }
        return null;
    }

}
