package com.home.app.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Light {


    private long id;
    private String name;
    private String type;
    private String gridSite;
    private String roomId;

    private long lightBrightness;
    private boolean state;

    public long getID() {
        return id;
    }

    public void setID(long value) {
        this.id = value;
    }


    public String getName() {
        return name;
    }

    public void setName(String value) {
        this.name = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String value) {
        this.type = value;
    }


    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String value) {
        this.roomId = value;
    }

    public String getGridSite() {
        return gridSite;
    }

    public void setGridSite(String value) {
        this.gridSite = value;
    }

    public long getLightBrightness() {
        return lightBrightness;
    }

    public void setLightBrightness(long value) {
        this.lightBrightness = value;
    }

    public boolean getState() {
        return state;
    }

    public void setState(boolean value) {
        this.state = value;
    }

    @Override
    public String toString() {
        return "Light [id=" + id + ", name=" + name + ", gridSite=" + gridSite + ", roomId=" + roomId
                + ", lightBrightness=" + lightBrightness + ", state=" + state + "]";
    }


}
