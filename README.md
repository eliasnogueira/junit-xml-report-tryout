# JUnit XML Report

This repo exercises the different ways to generate a JUnit XML report.

## Reason for this repo

It also tries to understand why the `maven-surefire-plugin` shows only the rerun and failed tests in both JUnit Open
Report format and JUnit XML Legacy Report format when the `rerunFailingTestsCount` is set to >=1.

## (Simple) Architecture

There are two test classes containing three tests each. One test in each class fails.

The `junit-platform-reporting` dependency is used to generate the JUnit Open report.
It comes along with the `maven-surefire-plugin` configuration to enable it.

Also, a custom `LegacyXmlReportGeneratingListener` listener is used to generate the JUnit XML Legacy Report.
To generate the legacy report, there's a service configured in the `src/test/resources/META-INF/services`
directory.`

During the test execution (`mvn test` or subsequent lifecycle phases), the two reports are generated in the
`target/` directory.

## Scenarios

### Scenario 1: running without `rerunFailingTestsCount`

1. Set the `rerunFailingTestsCount` to 0 in the `maven-surefire-plugin` configuration.
2. Run the tests.
3. Check the JUnit XML report as both `open-test-report.xml` and `legacy-test-report.xml` file will contain all the
   tests: success, and failed.

### Scenario 2: running with `rerunFailingTestsCount` set to >=1

1. Set the `rerunFailingTestsCount` to 1 in the `maven-surefire-plugin` configuration.
2. Run the tests.
3. Check the JUnit XML as both `open-test-report.xml` and `legacy-test-report.xml` will have only the failed/rerun
   tests.

## Observations

* It does not look like a problem in JUnit Platform.
* The Surefire XML reports contain all tests
* Both JUnit reports, when run with `rerunFailingTestsCount` >=1, contain only the failed/rerun tests, not the success
  ones
* It seems that, in a way I could not understand (yet), JUnit gets only the failed tests from the Surefire XML reports
  when `rerunFailingTestsCount` >=1
    * They are located
      on [JUnit4Provider](https://github.com/apache/maven-surefire/blob/master/surefire-providers/surefire-junit4/src/main/java/org/apache/maven/surefire/junit4/JUnit4Provider.java)
      for Legacy (I think)
      and [JUnitCoreProvider](https://github.com/apache/maven-surefire/blob/master/surefire-providers/surefire-junit47/src/main/java/org/apache/maven/surefire/junitcore/JUnitCoreProvider.java)
      for JUnit 5

## Additional comment

I have another listener: `DebugListener` that prints the test name and the test result.
It is used to debug the test execution.

When running with `rerunFailingTestsCount` >=1 this is the log, which shows two TestPlan executions per class:

- one containig all tests
- another containing only the failed ones

```
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
==== NEW TEST PLAN ====
Root: JUnit Jupiter
Total identifiers: 9
[INFO] Running com.eliasnogueira.tests.FirstTest
[RUN] testOne() - SUCCESSFUL
[RUN] testTwo() - SUCCESSFUL
[RUN] testThree() - FAILED
[ERROR] Tests run: 3, Failures: 1, Errors: 0, Skipped: 0, Time elapsed: 0.031 s <<< FAILURE! -- in com.eliasnogueira.tests.FirstTest
[ERROR] com.eliasnogueira.tests.FirstTest.testThree -- Time elapsed: 0.005 s <<< FAILURE!
org.opentest4j.AssertionFailedError
        at org.junit.jupiter.api.AssertionUtils.fail(AssertionUtils.java:37)
        at org.junit.jupiter.api.Assertions.fail(Assertions.java:122)
        at com.eliasnogueira.tests.FirstTest.testThree(FirstTest.java:20)

[INFO] Running com.eliasnogueira.tests.SecondTest
[RUN] oneTest() - FAILED
[RUN] twoTest() - SUCCESSFUL
[RUN] ThreeTest() - SUCCESSFUL
[ERROR] Tests run: 3, Failures: 1, Errors: 0, Skipped: 0, Time elapsed: 0.007 s <<< FAILURE! -- in com.eliasnogueira.tests.SecondTest
[ERROR] com.eliasnogueira.tests.SecondTest.oneTest -- Time elapsed: 0.001 s <<< FAILURE!
org.opentest4j.AssertionFailedError
        at org.junit.jupiter.api.AssertionUtils.fail(AssertionUtils.java:37)
        at org.junit.jupiter.api.Assertions.fail(Assertions.java:122)
        at com.eliasnogueira.tests.SecondTest.oneTest(SecondTest.java:10)

==== NEW TEST PLAN ====
Root: JUnit Jupiter
Total identifiers: 5
[INFO] Running com.eliasnogueira.tests.SecondTest
[RUN] oneTest() - FAILED
[ERROR] Tests run: 1, Failures: 1, Errors: 0, Skipped: 0, Time elapsed: 0.002 s <<< FAILURE! -- in com.eliasnogueira.tests.SecondTest
[ERROR] com.eliasnogueira.tests.SecondTest.oneTest -- Time elapsed: 0 s <<< FAILURE!
org.opentest4j.AssertionFailedError
        at org.junit.jupiter.api.AssertionUtils.fail(AssertionUtils.java:37)
        at org.junit.jupiter.api.Assertions.fail(Assertions.java:122)
        at com.eliasnogueira.tests.SecondTest.oneTest(SecondTest.java:10)

[INFO] Running com.eliasnogueira.tests.FirstTest
[RUN] testThree() - FAILED
[ERROR] Tests run: 1, Failures: 1, Errors: 0, Skipped: 0, Time elapsed: 0.002 s <<< FAILURE! -- in com.eliasnogueira.tests.FirstTest
[ERROR] com.eliasnogueira.tests.FirstTest.testThree -- Time elapsed: 0.001 s <<< FAILURE!
org.opentest4j.AssertionFailedError
        at org.junit.jupiter.api.AssertionUtils.fail(AssertionUtils.java:37)
        at org.junit.jupiter.api.Assertions.fail(Assertions.java:122)
        at com.eliasnogueira.tests.FirstTest.testThree(FirstTest.java:20)

[INFO]
```
