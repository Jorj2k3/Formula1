package com.example.demo4;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class DriverRanking {
    private IntegerProperty place;
    private StringProperty driver;
    private StringProperty team;
    private IntegerProperty points;

    public DriverRanking(int place, String driver, String team, int points) {
        this.place = new SimpleIntegerProperty(place);
        this.driver = new SimpleStringProperty(driver);
        this.team = new SimpleStringProperty(team);
        this.points = new SimpleIntegerProperty(points);
    }

    public int getPlace() {
        return place.get();
    }

    public String getDriver() {
        return driver.get();
    }

    public String getTeam() {
        return team.get();
    }

    public int getPoints() {
        return points.get();
    }
}
