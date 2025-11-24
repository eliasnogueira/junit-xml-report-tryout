package com.eliasnogueira.listener;

import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.launcher.TestPlan;

public class DebugListener implements TestExecutionListener {

    @Override
    public void testPlanExecutionStarted(TestPlan testPlan) {
        System.out.println("==== NEW TEST PLAN ====");
        testPlan.getRoots().forEach(root ->
                System.out.println("Root: " + root.getDisplayName())
        );
        System.out.println("Total identifiers: " + testPlan.countTestIdentifiers(test -> true));
    }

    @Override
    public void executionFinished(TestIdentifier testIdentifier, TestExecutionResult testExecutionResult) {
        if (testIdentifier.isTest()) {
            System.out.printf("[RUN] %s - %s%n",
                    testIdentifier.getDisplayName(),
                    testExecutionResult.getStatus());
        }
    }
}
