/*
 * Copyright 2021 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jbpm.process.longrest.bpm;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.kie.api.runtime.process.ProcessContext;

/**
 * This class was automatically generated by the data modeler tool.
 */

public class TestFunctions implements java.io.Serializable {

    static final long serialVersionUID = 1L;

    public static final Logger logger = Logger.getLogger(TestFunctions.class.getName());

    public static boolean addHeartBeatToRequest = false;

    public String getPreBuildTemplate() {
        String template = "{  "
                + "   'scm': { "
                + "      'url': @{quote(input.buildConfiguration.scmRepoURL)}, "
                + "      'revision': @{quote(input.buildConfiguration.scmRevision)} "
                + "   }, "
                + "   'syncEnabled': @{quote(input.buildConfiguration.preBuildSyncEnabled)}, "
                + "   'callback': { "
                + "      'url': @{quote(system.callbackUrl)}, "
                + "      'method': @{quote(system.callbackMethod)} ";
        if (addHeartBeatToRequest) {
            template = template
                    + "   }, "
                    + "   'heartBeat': { "
                    + "      'url': @{quote(system.heartBeatUrl)}, "
                    + "      'method': @{quote(system.heartBeatMethod)} ";
        }
        template = template + "   } "
                + "}";
        return template.replace("'", "\"");
    }

    public String getBuildTemplate() {
        return ("{  "
                + "   'buildScript': @{quote(input.buildConfiguration.buildScript)}, "
                + "   'scm': { "
                + "      'url': @{quote(preBuildResult.response.scm.url)}, "
                + "      'revision': @{quote(preBuildResult.response.scm.revision)} "
                + "   }, "
                + "   'callback': { "
                + "      'url': @{quote(system.callbackUrl)}, "
                + "      'method': @{quote(system.callbackMethod)} "
                + "   } "
                + "}").replace("'", "\"");
    }

    public String getCompletionTemplate() {
        return ("{ "
                + "   'buildConfigurationId': @{quote(input.buildConfiguration.id)}, "
                + "   'scm': { "
                + "      'url': @{quote(preBuildResult.?response.?scm.url)}, "
                + "      'revision': @{quote(preBuildResult.?response.?scm.revision)} "
                + "   }, "
                + "   'completionStatus': '@{functions.getCompletionStatus(preBuildResult.?status, ?buildResult.?status)}', "
                + "   'labels': @{asJson(input.buildConfiguration.labels, true)} "
                + "}").replace("'", "\"");
    }

    public TestFunctions() {
    }

    public static void logInfo(ProcessContext kcontext, String msg) {
        logger.info("Process: " + kcontext.getProcessInstance().getProcessName() + " id: " + kcontext.getProcessInstance().getId() + " ; " + msg);
    }

    public static ProcessCompletionStatus getCompletionStatus(
            String prebuildStatus,
            String buildStatus) {
        try {
            logger.info("PrebuildStatus: " + prebuildStatus);
            logger.info("BuildStatus: " + buildStatus);

            if (prebuildStatus == null) {
                logger.info("Operation failed with SYSTEM_ERROR.");
                return ProcessCompletionStatus.SYSTEM_ERROR;
            }

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
        SUCCESS,
        FAILED,
        CANCELLED,
        TIMED_OUT,
        SYSTEM_ERROR;
    }
}
