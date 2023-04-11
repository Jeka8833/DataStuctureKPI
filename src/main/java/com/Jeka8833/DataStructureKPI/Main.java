package com.Jeka8833.DataStructureKPI;

import com.Jeka8833.DataStructureKPI.console.Util;
import com.Jeka8833.DataStructureKPI.work.*;
import com.Jeka8833.DataStructureKPI.work.pr.PR1;
import com.Jeka8833.DataStructureKPI.work.pr.PR4;

public class Main {

    private static void registerTasks() {
        TaskManager.registerTask(PR1.class);
        TaskManager.registerTask(PR4.class);
    }

    public static void main(String[] args) {
        registerTasks();

        try {
            while (true) {
                try {
                    System.out.println();
                    System.out.println("Menu:");
                    TaskManager.printTaskList();

                    int selected = Util.readInt("Task index: ", 1, TaskManager.size());
                    TaskManager.startTask(selected);

                    System.out.println("Ended");
                } catch (StepDownException e) {
                    System.out.println("Back to menu");
                }
            }
        } catch (ExitException e) {
            System.out.println("Stopping....");
        }
    }

}
