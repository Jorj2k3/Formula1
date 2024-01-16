package com.example.demo4;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainFormController implements Initializable {

    @FXML
    private Button Teams_btn;

    @FXML
    private TableColumn<DriverRanking, String> driver_r;

    @FXML
    private Button drivers_btn;

    @FXML
    private Label fav_team;

    @FXML
    private Button main_btn;

    @FXML
    private TableColumn<DriverRanking, Integer> place_r;

    @FXML
    private TableColumn<DriverRanking, Integer> point_r;

    @FXML
    private Button races_btn;

    @FXML
    private AnchorPane rankForm;

    @FXML
    private Button ranking_btn;

    @FXML
    private TableView<DriverRanking> ranking_table;

    @FXML
    private Button signOut_btn;

    @FXML
    private Label team_place;

    @FXML
    private Label team_points;

    @FXML
    private TableColumn<DriverRanking, String> team_r;

    @FXML
    private AreaChart<String, Integer> team_table;

    @FXML
    private Label lab_HQ;

    @FXML
    private Label lab_boss;

    @FXML
    private Label lab_car;

    @FXML
    private Label lab_spons;

    @FXML
    private Label username;
    @FXML
    private AnchorPane mainF;
    @FXML
    private AnchorPane rankf;
    @FXML
    private TableColumn<TeamInfo, String> Boss_r1;
    @FXML
    private TableColumn<TeamInfo, String> car_r1;
    @FXML
    private TableColumn<TeamInfo, Integer> place_r1;

    @FXML
    private TableColumn<TeamInfo, Integer> ploints_r1;
    @FXML
    private Label team_place1;
    @FXML
    private Label team_points1;
    @FXML
    private TableColumn<TeamInfo, String> team_r1;
    @FXML
    private TableView<TeamInfo> teams_table1;
    @FXML
    private AnchorPane teamsf;
    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;

    private Alert alert;

    public void signOut(){

        try{
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Want to Sign Out");
            Optional<ButtonType> option = alert.showAndWait();

            if(option.get().equals(ButtonType.OK)){

                signOut_btn.getScene().getWindow().hide();

                Parent root = FXMLLoader.load(getClass().getResource("hello-view.fxml"));

                Stage stage = new Stage();
                Scene scene = new Scene(root);

                stage.setTitle("Formula 1");

                stage.setScene(scene);
                stage.show();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void Main_Rank(){
        rankf.setVisible(true);
        mainF.setVisible(false);
        teamsf.setVisible(false);
    }
    public void Rank_Main(){
        mainF.setVisible(true);
        rankf.setVisible(false);
        teamsf.setVisible(false);

    }
    public void Main_team(){
        mainF.setVisible(false);
        teamsf.setVisible(true);
        rankf.setVisible(false);
    }


    public void displayUsername() {
        String user = data.username;
       // System.out.println("Username from data class: " + user);

        user = user.substring(0, 1).toUpperCase() + user.substring(1);
        username.setText(user);
    }

    public void displayTeam() {
        String user = data.username;
        String selectData = "SELECT team FROM person WHERE name = ?";

        try {
            connect = dataBase.connectDB("Formula1", "postgres", "Jorje0505");
            prepare = connect.prepareStatement(selectData);
            prepare.setString(1, user);
            result = prepare.executeQuery();

            if (result.next()) {
                String teamName = result.getString("team");
                fav_team.setText(teamName);

                // Display Team Position and Points
                String positionPointsQuery = "SELECT RANK() OVER (ORDER BY SUM(R.Points) DESC) AS TeamPosition, " +
                        "SUM(R.Points) AS TotalPoints, T.Headquarters, T.TeamPrincipal, T.PrimarySponsorId, C.Model AS CarModel " +
                        "FROM Team T " +
                        "JOIN Car C ON T.TeamID = C.TeamID " +
                        "JOIN Results R ON C.CarID = R.CarID " +
                        "WHERE LOWER(T.TeamName) = LOWER(?) " +
                        "GROUP BY T.TeamID, T.TeamName, T.Headquarters, T.TeamPrincipal, T.PrimarySponsorId, C.Model";
                prepare = connect.prepareStatement(positionPointsQuery);
                prepare.setString(1, teamName);
                result = prepare.executeQuery();

                if (result.next()) {
                    int teamPosition = result.getInt("TeamPosition");
                    int totalPoints = result.getInt("TotalPoints");
                    String headquarters = result.getString("Headquarters");
                    String teamPrincipal = result.getString("TeamPrincipal");
                    int primarySponsorId = result.getInt("PrimarySponsorId");
                    String carModel = result.getString("CarModel");

                    // Display the information in labels
                    team_place.setText(String.valueOf(teamPosition));
                    team_points.setText(String.valueOf(totalPoints));
                    lab_HQ.setText(headquarters);
                    lab_boss.setText(teamPrincipal);
                    lab_car.setText(carModel);
                    lab_spons.setText(String.valueOf(primarySponsorId));

                    // Display on AreaChart
                    displayTeamPointsChart(teamName);
                } else {
                    team_place.setText("Team Position not found");
                    team_points.setText("Total Points not found");
                    lab_HQ.setText("Headquarters not found");
                    lab_boss.setText("Team Principal not found");
                    lab_car.setText("Car Model not found");
                    lab_spons.setText("Primary Sponsor ID not found");
                }
            } else {
                fav_team.setText("Favorite Team not found");
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error: " + e.getMessage());
        }
    }

    private void displayTeamPointsChart(String teamName) {
        try {
            String teamPointsQuery = "SELECT R.RaceID, R.RaceName, R.Date, R.Time, SUM(RES.Points) AS TeamPoints " +
                    "FROM Team T " +
                    "JOIN Car C ON T.TeamID = C.TeamID " +
                    "JOIN Results RES ON C.CarID = RES.CarID " +
                    "JOIN Race R ON RES.RaceID = R.RaceID " +
                    "WHERE LOWER(T.TeamName) = LOWER(?) " +
                    "GROUP BY R.RaceID, R.RaceName, R.Date, R.Time " +
                    "ORDER BY R.Date, R.Time";

            prepare = connect.prepareStatement(teamPointsQuery);
            prepare.setString(1, teamName);
            result = prepare.executeQuery();

            XYChart.Series<String, Integer> series = new XYChart.Series<>();
            series.setName(teamName); // Set the name of the series (teamName)

            while (result.next()) {
                String raceName = result.getString("RaceName"); // Change to the correct column name
                int teamPoints = result.getInt("TeamPoints"); // Change to the correct column name
                String raceDateTime = result.getString("Date") + " " + result.getString("Time");

                // Assuming team_table is an AreaChart<String, Integer>
                series.getData().add(new XYChart.Data<>(raceDateTime, teamPoints));
            }

            team_table.getData().add(series);

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error fetching team points for chart: " + e.getMessage());
        }
    }

    private void displayDriverRanking() {
        try {
            String driverRankingQuery = "SELECT RANK() OVER (ORDER BY SUM(RES.Points) DESC) AS DriverPosition, " +
                    "D.FirstName || ' ' || D.LastName AS DriverName, " +
                    "T.TeamName, " +
                    "SUM(RES.Points) AS TotalPoints " +
                    "FROM Driver D " +
                    "JOIN Results RES ON D.DriverID = RES.DriverID " +
                    "JOIN Car C ON RES.CarID = C.CarID " +
                    "JOIN Team T ON C.TeamID = T.TeamID " +
                    "GROUP BY D.DriverID, T.TeamID " +
                    "ORDER BY TotalPoints DESC";

            prepare = connect.prepareStatement(driverRankingQuery);
            result = prepare.executeQuery();

            ObservableList<DriverRanking> driverRankings = FXCollections.observableArrayList();

            int place = 1;
            while (result.next()) {
                String driverName = result.getString("DriverName");
                String teamName = result.getString("TeamName");
                int totalPoints = result.getInt("TotalPoints");

                driverRankings.add(new DriverRanking(place, driverName, teamName, totalPoints));
                place++;
            }

            ranking_table.setItems(driverRankings);

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error fetching driver rankings: " + e.getMessage());
        }
    }

    private void displayTeamRanking() {
        try {
            String teamRankingQuery = "SELECT RANK() OVER (ORDER BY SUM(RES.Points) DESC) AS TeamPosition, " +
                    "T.TeamName, " +
                    "C.Model AS Car, " +
                    "B.BossName AS Boss, " +
                    "SUM(RES.Points) AS TotalPoints " +
                    "FROM Team T " +
                    "JOIN Car C ON T.TeamID = C.TeamID " +
                    "JOIN Results RES ON C.CarID = RES.CarID " +
                    "JOIN Boss B ON T.TeamID = B.TeamID " +
                    "GROUP BY T.TeamID, C.Model, B.BossName " +
                    "ORDER BY TotalPoints DESC";

            prepare = connect.prepareStatement(teamRankingQuery);
            result = prepare.executeQuery();

            ObservableList<TeamInfo> teamRankings = FXCollections.observableArrayList();

            int place = 1;
            while (result.next()) {
                String teamName = result.getString("TeamName");
                String car = result.getString("Car");
                String boss = result.getString("Boss");
                int totalPoints = result.getInt("TotalPoints");

                teamRankings.add(new TeamInfo(place, teamName, car, boss, totalPoints));
                place++;
            }

            teams_table1.setItems(teamRankings);

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error fetching team rankings: " + e.getMessage());
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        displayUsername();
        displayTeam();

        place_r.setCellValueFactory(new PropertyValueFactory<>("place"));
        driver_r.setCellValueFactory(new PropertyValueFactory<>("driver"));
        team_r.setCellValueFactory(new PropertyValueFactory<>("team"));
        point_r.setCellValueFactory(new PropertyValueFactory<>("points"));

        displayDriverRanking();

        place_r1.setCellValueFactory(new PropertyValueFactory<>("place"));
        team_r1.setCellValueFactory(new PropertyValueFactory<>("team"));
        car_r1.setCellValueFactory(new PropertyValueFactory<>("car"));
        Boss_r1.setCellValueFactory(new PropertyValueFactory<>("boss"));
        ploints_r1.setCellValueFactory(new PropertyValueFactory<>("points"));

        displayTeamRanking();

    }
}
