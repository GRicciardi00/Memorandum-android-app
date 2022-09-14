package com.example.memoapp;



public class Memo {

    private String title;
    private String description;
    private String day;
    private String hour;
    private String status;
    private String location;
    private String latlon;
    private boolean notification;

    public Memo(String title, String description, String day, String hour, String location,String latlon) {
        this.title = title;
        this.description = description;
        this.day = day;
        this.hour = hour;
        this.status = "active";
        this.latlon = latlon;
        this.location = location;
        this.notification = false; //it becomes true only after setting the relative geofence in the main activity
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDay() {
        return day;
    }

    public String getHour() {
        return hour;
    }

    public String getStatus(){
        return status;
    }

    public void setStatus(String stat){
        status = stat;
    }

    public String getLatlon() {
        return latlon;
    }

    public String getLocation() {
        return location;
    }

    public Boolean getNotification() {return notification;}

    public void setNotification(Boolean val) {notification=val;}




}
