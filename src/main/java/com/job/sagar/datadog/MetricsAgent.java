package com.job.sagar.datadog;

import com.timgroup.statsd.NonBlockingStatsDClient;
import com.timgroup.statsd.StatsDClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class MetricsAgent {
    private static final Logger logger = LogManager.getLogger(MetricsAgent.class);
    @Value("${datadog.explorer}")
    private String explorer;
    @Value("${datadog.hostname}")
    private String hostname;
    @Value("${datadog.port}")
    private int port;
    private StatsDClient metricClient;

    public MetricsAgent() {
    }

    @PostConstruct
    public void init() {
        metricClient = new NonBlockingStatsDClient(explorer, hostname, port, "application: jobSagar");
    }

    public void incrementOperationCountByResult(String operation, String metric) {
        metricClient.increment(operation, "result:" + metric);
    }

    public void recordResponseCodeCount(String apiName, String httpCode, String responseCode,
                                        String client) {
        metricClient.increment("Response Code Count", "API Name:" + apiName,
                "HTTP Code: " + httpCode, "Response Code:" + responseCode, "Client:" + client);
    }

    public void recordResponseCodeCount(String httpCode, String responseCode, String client) {
        metricClient.increment("Response_Code_Count", "http_code: " + httpCode, "response_code: " + responseCode,
                "client:" + client);
    }

    public void recordExecutionTimeReportCode(String reportCode, long timeTaken) {
        metricClient.recordExecutionTime("Xfer_Report_Code Execution_Time", timeTaken, "report_code:" + reportCode);
    }

    @Async("dbFailureMetricExecutor")
    public void pushDBFailureMetric(String flowType, String execption) {
        try {
            metricClient.increment("DB_FAILURE_METRIC_COUNT", "flow_type:" + flowType, "error:" + execption);
        } catch (Exception ex) {
            logger.error("Exception occurred while logging metrics for DB failure");
        }
    }
}
