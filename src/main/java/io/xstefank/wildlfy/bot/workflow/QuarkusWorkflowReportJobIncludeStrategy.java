package io.xstefank.wildlfy.bot.workflow;

import io.xstefank.wildlfy.bot.report.WorkflowReport;
import io.xstefank.wildlfy.bot.report.WorkflowReportJob;
import io.xstefank.wildlfy.bot.report.WorkflowReportJobIncludeStrategy;

import javax.inject.Singleton;

@Singleton
public class QuarkusWorkflowReportJobIncludeStrategy implements WorkflowReportJobIncludeStrategy {

    @Override
    public boolean include(WorkflowReport report, WorkflowReportJob job) {
        if (job.isFailing()) {
            return true;
        }

        // in this particular case, we exclude the Windows job as it does not run the containers job
        // (no Docker support on Windows) and thus does not provide a similar coverage as the Linux
        // jobs. Having it green does not mean that things were OK globally.
        if (isJvmTests(job)) {
            if (isWindows(job)) {
                return false;
            }

            return hasJobWithSameLabelFailing(report, job);
        }

        return hasJobWithSameLabelFailing(report, job);
    }

    private static boolean isJvmTests(WorkflowReportJob job) {
        return job.getName().startsWith(QuarkusWorkflowConstants.JOB_NAME_JVM_TESTS_PREFIX);
    }

    private static boolean isWindows(WorkflowReportJob job) {
        return job.getName().contains(QuarkusWorkflowConstants.JOB_NAME_WINDOWS);
    }

    private static boolean hasJobWithSameLabelFailing(WorkflowReport report, WorkflowReportJob job) {
        return report.getJobs().stream()
                .filter(j -> j.getLabel().equals(job.getLabel()))
                .anyMatch(j -> j.isFailing());
    }
}
