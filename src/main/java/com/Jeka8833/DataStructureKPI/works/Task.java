package com.Jeka8833.DataStructureKPI.works;

import org.jetbrains.annotations.NotNull;

public interface Task {

    void run() throws ExitException, StepDownException;

    @NotNull
    default String name() {
        return "Undefined";
    }
}
