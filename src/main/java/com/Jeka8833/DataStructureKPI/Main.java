package com.Jeka8833.DataStructureKPI;

import com.Jeka8833.DataStructureKPI.console.Util;
import com.Jeka8833.DataStructureKPI.works.ExitException;
import com.Jeka8833.DataStructureKPI.works.PR1;
import com.Jeka8833.DataStructureKPI.works.StepDownException;
import com.Jeka8833.DataStructureKPI.works.TaskManager;

public class Main {

    private static void registerTasks() {
        TaskManager.registerTask(PR1.class);
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
