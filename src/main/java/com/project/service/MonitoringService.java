package com.project.service;

import com.profesorfalken.jsensors.model.components.Components;
import com.profesorfalken.jsensors.model.sensors.Sensors;
import com.profesorfalken.jsensors.model.sensors.Temperature;
import com.project.utils.ComponentConfiguration;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MonitoringService {

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public void getComponentsData() {
        Thread thread = new Thread(() -> {
            System.out.printf("Current Thread: %s%n", Thread.currentThread().getName());
            Components components = ComponentConfiguration.getComponents();

            processCpu(components);
            processGpu(components);
        });


        scheduler.scheduleAtFixedRate(thread, 0, 30, TimeUnit.SECONDS);
    }

    private void processCpu(Components components) {
        components.cpus.forEach(currentCpu -> {
            System.out.printf("Getting sensors for cpu %s%n", currentCpu.name);

            displayData(currentCpu.sensors);
        });
    }

    private void processGpu(Components components) {
        components.gpus.forEach(currentGpu -> {
            System.out.printf("Getting sensors for gpu %s%n", currentGpu.name);

            displayData(currentGpu.sensors);
        });
    }

    private void displayData(Sensors sensors) {
        displayTemperature(sensors.temperatures);
        System.out.println("-".repeat(50));
    }

    private void displayTemperature(List<Temperature> temperatures) {
        temperatures
                .forEach(temperature -> {
                    System.out.println(temperature.name + " temp: " + temperature.value);
                });
    }
}
