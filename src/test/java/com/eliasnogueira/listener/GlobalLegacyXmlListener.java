package com.eliasnogueira.listener;

import org.junit.platform.reporting.legacy.xml.LegacyXmlReportGeneratingListener;

import java.io.PrintWriter;
import java.nio.file.Path;

/**
 * Need to implement the LegacyXmlReportGeneratingListener and register the class as a service in order to generate the JUnit 4 report (legacy report)
 * <p>
 * For more details, see the
 * <a href="https://docs.junit.org/current/user-guide/#junit-platform-reporting-legacy-xml">
 * JUnit 5 User Guide - Legacy XML Reports
 */
public class GlobalLegacyXmlListener extends LegacyXmlReportGeneratingListener {

    public GlobalLegacyXmlListener() {
        super(
                Path.of("target/"),
                new PrintWriter(System.out)
        );
    }
}
