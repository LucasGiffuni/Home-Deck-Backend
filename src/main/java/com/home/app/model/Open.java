package com.home.app.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Open {
    private long id;
    private String name;
    private String roomId;
    private String gridSite;

    private boolean state;

    public long getId() {
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

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String value) {
        this.roomId = value;
    }

    public boolean getState() {
        return state;
    }

    public void setState(boolean value) {
        this.state = value;
    }

    public String getGridSite() {
        return gridSite;
    }

    public void setGridSite(String value) {
        this.gridSite = value;
    }
}
