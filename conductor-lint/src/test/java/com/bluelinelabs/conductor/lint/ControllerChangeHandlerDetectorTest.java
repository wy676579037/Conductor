package com.bluelinelabs.conductor.lint;

import org.intellij.lang.annotations.Language;
import org.junit.Test;

import static com.android.tools.lint.checks.infrastructure.TestFiles.java;
import static com.android.tools.lint.checks.infrastructure.TestLintTask.lint;

public class ControllerChangeHandlerDetectorTest {

    private static final String CONSTRUCTOR =
            "src/test/SampleHandler.java:2: Error: This ControllerChangeHandler needs to have a public default constructor (test.SampleHandler) [ValidControllerChangeHandler]\n"
            + "public class SampleHandler extends com.bluelinelabs.conductor.ControllerChangeHandler {\n"
            + "^\n"
            + "1 errors, 0 warnings\n";
    private static final String PRIVATE_CLASS_ERROR =
            "src/test/SampleHandler.java:2: Error: This ControllerChangeHandler class should be public (test.SampleHandler) [ValidControllerChangeHandler]\n"
                    + "private class SampleHandler extends com.bluelinelabs.conductor.ControllerChangeHandler {\n"
                    + "^\n"
                    + "1 errors, 0 warnings\n";

    @Test
    public void testWithNoConstructor() {
        @Language("JAVA") String source = ""
                + "package test;\n"
                + "public class SampleHandler extends com.bluelinelabs.conductor.ControllerChangeHandler {\n"
                + "}";

        lint()
                .files(java(source))
                .issues(ControllerIssueDetector.ISSUE, ControllerChangeHandlerIssueDetector.ISSUE)
                .run()
                .expectClean();
    }

    @Test
    public void testWithEmptyConstructor() {
        @Language("JAVA") String source = ""
                + "package test;\n"
                + "public class SampleHandler extends com.bluelinelabs.conductor.ControllerChangeHandler {\n"
                + "    public SampleHandler() { }\n"
                + "}";

        lint()
                .files(java(source))
                .issues(ControllerIssueDetector.ISSUE, ControllerChangeHandlerIssueDetector.ISSUE)
                .run()
                .expectClean();
    }

    @Test
    public void testWithInvalidConstructor() {
        @Language("JAVA") String source = ""
                + "package test;\n"
                + "public class SampleHandler extends com.bluelinelabs.conductor.ControllerChangeHandler {\n"
                + "    public SampleHandler(int number) { }\n"
                + "}";

        lint()
                .files(java(source))
                .issues(ControllerIssueDetector.ISSUE, ControllerChangeHandlerIssueDetector.ISSUE)
                .run()
                .expect(CONSTRUCTOR);
    }

    @Test
    public void testWithEmptyAndInvalidConstructor() {
        @Language("JAVA") String source = ""
                + "package test;\n"
                + "public class SampleHandler extends com.bluelinelabs.conductor.ControllerChangeHandler {\n"
                + "    public SampleHandler() { }\n"
                + "    public SampleHandler(int number) { }\n"
                + "}";

        lint()
                .files(java(source))
                .issues(ControllerIssueDetector.ISSUE, ControllerChangeHandlerIssueDetector.ISSUE)
                .run()
                .expectClean();
    }

    @Test
    public void testWithPrivateConstructor() {
        @Language("JAVA") String source = ""
                + "package test;\n"
                + "public class SampleHandler extends com.bluelinelabs.conductor.ControllerChangeHandler {\n"
                + "    private SampleHandler() { }\n"
                + "}";

        lint()
                .files(java(source))
                .issues(ControllerIssueDetector.ISSUE, ControllerChangeHandlerIssueDetector.ISSUE)
                .run()
                .expect(CONSTRUCTOR);
    }

    @Test
    public void testWithPrivateClass() {
        @Language("JAVA") String source = ""
                + "package test;\n"
                + "private class SampleHandler extends com.bluelinelabs.conductor.ControllerChangeHandler {\n"
                + "    public SampleHandler() { }\n"
                + "}";

        lint()
                .files(java(source))
                .issues(ControllerIssueDetector.ISSUE, ControllerChangeHandlerIssueDetector.ISSUE)
                .run()
                .expect(PRIVATE_CLASS_ERROR);
    }

}
