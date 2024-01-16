package com.example.demo4;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TeamInfo {
    private IntegerProperty place;
    private StringProperty team;
    private StringProperty car;
    private StringProperty boss;
    private IntegerProperty points;

    public TeamInfo(int place, String team, String car, String boss, int points) {
        this.place = new SimpleIntegerProperty(place);
        this.team = new SimpleStringProperty(team);
        this.car = new SimpleStringProperty(car);
        this.boss = new SimpleStringProperty(boss);
        this.points = new SimpleIntegerProperty(points);
    }

    public int getPlace() {
        return place.get();
    }

    public String getTeam() {
        return team.get();
    }

    public String getCar() {
        return car.get();
    }

    public String getBoss() {
        return boss.get();
    }

    public int getPoints() {
        return points.get();
    }
}
