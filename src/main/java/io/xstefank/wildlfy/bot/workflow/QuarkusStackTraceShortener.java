package io.xstefank.wildlfy.bot.workflow;

import io.xstefank.wildlfy.bot.report.StackTraceShortener;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Singleton;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Singleton
public class QuarkusStackTraceShortener implements StackTraceShortener {

    private static final String HTML_INTERNAL_ERROR_MARKER = "<title>Internal Server Error";
    private static final Pattern STACK_TRACE_PATTERN = Pattern.compile("Actual: <!doctype html>.*?<pre>(.*?)</pre>",
            Pattern.DOTALL);
    private static final String QUARKUS_TEST_EXTENSION = " at io.quarkus.test.junit.QuarkusTestExtension.runExtensionMethod(";

    @Override
    public String shorten(String stacktrace, int length) {
        if (StringUtils.isBlank(stacktrace)) {
            return null;
        }

        if (stacktrace.contains(HTML_INTERNAL_ERROR_MARKER)) {
            // this is an HTML error, let's get to the stacktrace
            Matcher matcher = STACK_TRACE_PATTERN.matcher(stacktrace);
            StringBuilder sb = new StringBuilder();
            if (matcher.find()) {
                matcher.appendReplacement(sb, "Actual: An Internal Server Error with stack trace:\n$1");
                stacktrace = sb.toString();
            }
        }

        int quarkusTestExtensionIndex = stacktrace.indexOf(QUARKUS_TEST_EXTENSION);
        if (quarkusTestExtensionIndex > 0) {
            stacktrace = stacktrace.substring(0, stacktrace.lastIndexOf('\n', quarkusTestExtensionIndex));
        }

        return StringUtils.abbreviate(stacktrace, length);
    }
}
