package com.project.service;

import com.profesorfalken.jsensors.model.components.Components;
import com.profesorfalken.jsensors.model.sensors.Sensors;
import com.profesorfalken.jsensors.model.sensors.Temperature;
import com.project.utils.ComponentConfiguration;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
public class MonitoringService {

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public void getComponentsData() {
        scheduler.scheduleAtFixedRate(() -> {
            log.info("Current Thread: %s".formatted(Thread.currentThread().getName()));
            Components components = ComponentConfiguration.getComponents();

            if (components == null) {
                throw new RuntimeException("Couldn't find components");
            }

            processCpu(components);
            processGpu(components);
        }, 0, 5, TimeUnit.MINUTES);
    }

    private void processCpu(Components components) {
        components.cpus.forEach(currentCpu -> {
            log.info("Getting sensors for cpu %s".formatted(currentCpu.name));

            displayData(currentCpu.sensors);
        });
    }

    private void processGpu(Components components) {
        components.gpus.forEach(currentGpu -> {
            log.info("Getting sensors for gpu %s".formatted(currentGpu.name));

            displayData(currentGpu.sensors);
        });
    }

    private void displayData(Sensors sensors) {
        displayTemperature(sensors.temperatures);
        log.info("-".repeat(50));
    }

    private void displayTemperature(List<Temperature> temperatures) {
        temperatures
                .forEach(temperature -> {
                    Double tempValue = temperature.value;
                    String tempName = temperature.name;

                    System.out.println(tempName + " temp: " + tempValue);
                    checkTemperature(tempValue);
                });
    }

    private void checkTemperature(double temperature) {
        if (temperature >= 70) {
            String shutdownCommand;
            String operatingSystem = System.getProperty("os.name");

            if ("Linux".equals(operatingSystem) || "Mac OS X".equals(operatingSystem)) {
                shutdownCommand = "shutdown -h now";
            } else if (operatingSystem.contains("Windows")) {
                shutdownCommand = "shutdown.exe -s -t 0";
            } else {
                throw new RuntimeException("Unsupported operating system.");
            }

            try {
                Runtime.getRuntime().exec(shutdownCommand);
            } catch (Exception exception) {
                log.error(exception.getMessage());
            }

            System.exit(0);
        }
    }
}
