package com.example.demo4;

public class Teams {
    public Integer getId() {
        return id;
    }

    public String getTeamName() {
        return teamName;
    }

    public String getTeamPrincipal() {
        return teamPrincipal;
    }

    public String getHeadquarters() {
        return headquarters;
    }

    public Integer getPrimarySponsorId() {
        return primarySponsorId;
    }
    public Integer getPoints() {
        return points;
    }

    public Integer getPosition() {
        return position;
    }
    private Integer id;
    private String teamName;
    private String teamPrincipal;
    private String headquarters;
    private Integer primarySponsorId;

    public String getCar() {
        return car;
    }

    private String car;

    private Integer points;
    private Integer position;


    public Teams(Integer id, String teamName, String teamPrincipal, String headquarters, Integer primarySponsorId, Integer points, Integer position, String car) {
        this.id = id;
        this.teamName = teamName;
        this.teamPrincipal = teamPrincipal;
        this.headquarters = headquarters;
        this.primarySponsorId = primarySponsorId;
        this.points = points;
        this.position =position;
        this.car = car;
    }
}
