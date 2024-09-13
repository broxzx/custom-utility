package com.project.init;

import com.project.service.MonitoringService;

public class ApplicationInitializer {

    public static void run() {
        MonitoringService monitoringService = new MonitoringService();
        monitoringService.getComponentsData();
    }

}
