package com.example.myapplication;

public class Observation {

    private int id;
    private String observationText;
    private String timeOfObservation;
    private String additionalComments;
    private int hikeId;

    // Constructor
    public Observation(int id, String observationText, String timeOfObservation, String additionalComments, int hikeId) {
        this.id = id;
        this.observationText = observationText;
        this.timeOfObservation = timeOfObservation;
        this.additionalComments = additionalComments;
        this.hikeId = hikeId;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getObservationText() {
        return observationText;
    }

    public void setObservationText(String observationText) {
        this.observationText = observationText;
    }

    public String getTimeOfObservation() {
        return timeOfObservation;
    }

    public void setTimeOfObservation(String timeOfObservation) {
        this.timeOfObservation = timeOfObservation;
    }

    public String getAdditionalComments() {
        return additionalComments;
    }

    public void setAdditionalComments(String additionalComments) {
        this.additionalComments = additionalComments;
    }

    public int getHikeId() {
        return hikeId;
    }

    public void setHikeId(int hikeId) {
        this.hikeId = hikeId;
    }
}
