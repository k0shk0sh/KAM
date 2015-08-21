package com.fast.access.kam.global.model;

/**
 * Created by Kosh on 8/21/2015. copyrights are reserved
 */
public class EventsModel {

    public enum EventType {
        DELETE, NEW
    }

    private String packageName;
    private EventType eventType;

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }
}
