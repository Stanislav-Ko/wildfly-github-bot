package io.xstefank.wildlfy.bot.report;

import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import org.kohsuke.github.GHCheckRun;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class WorkflowReportFormatter {

    public String getCheckRunReportSummary(WorkflowReport report, WorkflowContext workflowContext, boolean artifactsAvailable,
                                           WorkflowReportJobIncludeStrategy workflowReportJobIncludeStrategy) {
        return Templates.checkRunReportSummary(report, workflowContext, artifactsAvailable, workflowReportJobIncludeStrategy)
                .render();
    }

    public String getCheckRunReport(WorkflowReport report, boolean includeStackTraces) {
        return Templates.checkRunReport(report, includeStackTraces).render();
    }

    public String getReportComment(WorkflowReport report, boolean artifactsAvailable, GHCheckRun checkRun,
                                   String messageIdActive, boolean includeStackTraces,
                                   WorkflowReportJobIncludeStrategy workflowReportJobIncludeStrategy) {
        return Templates.commentReport(report, artifactsAvailable, checkRun, messageIdActive, includeStackTraces,
                workflowReportJobIncludeStrategy).render();
    }

    @CheckedTemplate
    private static class Templates {

        public static native TemplateInstance checkRunReportSummary(WorkflowReport report, WorkflowContext workflowContext,
                                                                    boolean artifactsAvailable, WorkflowReportJobIncludeStrategy workflowReportJobIncludeStrategy);

        public static native TemplateInstance checkRunReport(WorkflowReport report, boolean includeStackTraces);

        public static native TemplateInstance commentReport(WorkflowReport report, boolean artifactsAvailable,
                                                            GHCheckRun checkRun, String messageIdActive, boolean includeStackTraces,
                                                            WorkflowReportJobIncludeStrategy workflowReportJobIncludeStrategy);
    }
}
