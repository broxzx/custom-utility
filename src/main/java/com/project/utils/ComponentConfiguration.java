package com.project.utils;

import com.profesorfalken.jsensors.JSensors;
import com.profesorfalken.jsensors.model.components.Components;

public class ComponentConfiguration {

    public static Components getComponents() {
        return JSensors.get.components();
    }

}
