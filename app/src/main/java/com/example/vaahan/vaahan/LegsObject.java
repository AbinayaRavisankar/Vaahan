package com.example.vaahan.vaahan;

import java.util.List;

public class LegsObject {

    private List<StepsObject> steps;
    private DistanceObject distance;
    private DurationObject duration;
    private String end_address, start_address;

    public LegsObject(DurationObject duration, DistanceObject distance, List<StepsObject> steps) {
        this.duration = duration;
        this.distance = distance;
        this.steps = steps;
    }

    public List<StepsObject> getSteps() {
        return steps;
    }

    public DistanceObject getDistance() {
        return distance;
    }

    public DurationObject getDuration() {
        return duration;
    }

    public void setStartAddress(String start_address) {
        this.start_address = start_address;
    }

    public void setEndAddress(String end_address) {
        this.end_address = end_address;
    }

    public String getEndAddress() {
        return end_address;
    }

    public String getStartAddress() {
        return start_address;
    }
}
