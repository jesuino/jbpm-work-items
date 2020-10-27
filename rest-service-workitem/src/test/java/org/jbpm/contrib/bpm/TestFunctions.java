package org.jbpm.contrib.bpm;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.kie.api.runtime.process.ProcessContext;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class was automatically generated by the data modeler tool.
 */

public class TestFunctions implements java.io.Serializable {

    static final long serialVersionUID = 1L;

    public static final Logger logger = Logger.getLogger(TestFunctions.class.getName());

    private static final ObjectMapper jsonMapper = new ObjectMapper();

    public String getPreBuildTemplate() {
        return ("{  "
            + "   'scm': { "
            + "      'url': '@{input.buildConfiguration.scmRepoURL}', "
            + "      'revision': '@{input.buildConfiguration.scmRevision}' "
            + "   }, "
            + "   'syncEnabled': @{input.buildConfiguration.preBuildSyncEnabled}, "
            + "   'callback': { "
            + "      'url': '@{system.callbackUrl}', "
            + "      'method': 'POST' "
            + "   } "
            + "}").replace("'","\"");
    }

    public String getBuildTemplate() {
        return ("{  "
                + "   'buildScript': '@{input.buildConfiguration.buildScript}', "
                + "   'scm': { "
                + "      'url': '@{preBuildResult.response.scm.url}', "
                + "      'revision': '@{preBuildResult.response.scm.revision}' "
                + "   }, "
                + "   'callback': { "
                + "      'url': '@{system.callbackUrl}', "
                + "      'method': 'POST' "
                + "   } "
                + "}").replace("'","\"");
    }

    public String getCompletionTemplate() {
        return ("{ "
                + "   'buildConfigurationId': '@{input.buildConfiguration.id}', "
                + "   'scm': { "
                + "      'url': '@{preBuildResult.?response.?scm.url}', "
                + "      'revision': '@{preBuildResult.?response.?scm.revision}' "
                + "   }, "
                + "   'completionStatus': '@{functions.getCompletionStatus(preBuildResult.?status, ?buildResult.?status)}' "
                + "}").replace("'","\"");
    }

    public TestFunctions() {
    }

    public static void logInfo(ProcessContext kcontext, String msg) {
        logger.info("Process: " + kcontext.getProcessInstance().getProcessName()+" id: "+kcontext.getProcessInstance().getId()+" ; "+msg);
    }

    public static ProcessCompletionStatus getCompletionStatus(
            String prebuildStatus,
            String buildStatus) {
        try {
            logger.info("PrebuildStatus: " + prebuildStatus);
            logger.info("BuildStatus: " + buildStatus);

            if (prebuildStatus.equals("FAILED")) {
                logger.info("Operation FAILED.");
                return ProcessCompletionStatus.FAILED;
            }

            if (prebuildStatus.equals("CANCELLED")) {
                logger.info("Operation CANCELLED.");
                return ProcessCompletionStatus.CANCELLED;
            }

            if (prebuildStatus.equals("TIMED_OUT")) {
                logger.info("Operation TIMED_OUT.");
                return ProcessCompletionStatus.TIMED_OUT;
            }

            if (buildStatus != null) { //build task did run
                if (buildStatus.equals("FAILED")) {
                    logger.info("Operation FAILED.");
                    return ProcessCompletionStatus.FAILED;
                }

                if (buildStatus.equals("CANCELLED")) {
                    logger.info("Operation CANCELLED.");
                    return ProcessCompletionStatus.CANCELLED;
                }

                if (buildStatus.equals("TIMED_OUT")) {
                    logger.info("Operation TIMED_OUT.");
                    return ProcessCompletionStatus.TIMED_OUT;
                }
            }
            logger.info("Process status SUCCESS.");
            return ProcessCompletionStatus.SUCCESS;
        } catch (Throwable e) {
            logger.log(Level.SEVERE, "Error processing process status.", e);
            return ProcessCompletionStatus.SYSTEM_ERROR;
        }
    }

    public enum ProcessCompletionStatus {
        SUCCESS, FAILED, CANCELLED, TIMED_OUT, SYSTEM_ERROR;
    }
}
