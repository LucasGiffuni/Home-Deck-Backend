package com.home.app.model;

import java.util.ArrayList;
import java.util.Arrays;


public class Lights {


    @JsonProperty("lightsName")
    private String lightsName;
    
    @JsonProperty("lights")
    private ArrayList<Light> lights;

    public String getLightsName() {
        return lightsName;
    }

    public void setLightsName(String value) {
        this.lightsName = value;
    }

    public ArrayList<Light> getLights() {
        return lights;
    }

    public void setLights(ArrayList<Light> value) {
        this.lights = value;
    }

    @Override
    public String toString() {
        return "Lights [lightsName=" + lightsName + ", lights=" + lights + "]";
    }

   

    
}

