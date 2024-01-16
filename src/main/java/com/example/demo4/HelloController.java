package com.example.demo4;

import javafx.animation.TranslateTransition;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class HelloController {
    @FXML
    private TextField fp_answer;

    @FXML
    private Button fp_backBtn;

    @FXML
    private Button fp_proceedBtn;

    @FXML
    private ComboBox<?> fp_question;

    @FXML
    private AnchorPane fp_questionForm;

    @FXML
    private Button np_back;

    @FXML
    private PasswordField np_confirmPassword;

    @FXML
    private Button np_newChangePassBtn;

    @FXML
    private AnchorPane np_newPassForm;

    @FXML
    private PasswordField np_newPassword;

    @FXML
    private Hyperlink si_forgotPass;

    @FXML
    private Button si_loginBtn;

    @FXML
    private AnchorPane si_loginForm;

    @FXML
    private PasswordField si_password;

    @FXML
    private TextField si_username;

    @FXML
    private Button side_CreateBtn;

    @FXML
    private Button side_alreadyHave;

    @FXML
    private AnchorPane side_form;

    @FXML
    private TextField su_age;

    @FXML
    private TextField su_favouriteTeam;

    @FXML
    private PasswordField su_password;

    @FXML
    private Button su_signupBtn;

    @FXML
    private AnchorPane su_signupForm;

    @FXML
    private TextField su_username;

    @FXML
    private TextField fp_username;

    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;

    private Alert alert;

    public void loginBtn() {
        if (si_username.getText().isEmpty() || si_password.getText().isEmpty()) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Incorrect Username/Password");
            alert.showAndWait();
        } else {
            String selectData = "SELECT name, password FROM person WHERE name = ? and password = ?";
            connect = dataBase.connectDB("Formula1", "postgres", "Jorje0505");

            try {
                prepare = connect.prepareStatement(selectData);
                prepare.setString(1, si_username.getText());
                prepare.setString(2, si_password.getText());

                result = prepare.executeQuery();

                if (result.next()) {

                    data.username = si_username.getText();

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully login");
                    alert.showAndWait();

                    Parent root = FXMLLoader.load(getClass().getResource("MainForm.fxml"));

                    Stage stage = new Stage();
                    Scene scene = new Scene(root);

                    stage.setTitle("Formula1 Main");
                    stage.setMinWidth(1100);
                    stage.setMinHeight(300);

                    stage.setScene(scene);
                    stage.show();

                    si_loginBtn.getScene().getWindow().hide();


                } else {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Incorrect Username/Password");
                    alert.showAndWait();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void regBtn(){

        if(su_username.getText().isEmpty() || su_password.getText().isEmpty() || su_age.getText().isEmpty() || su_favouriteTeam.getText().isEmpty()){

            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all blank fields");
            alert.showAndWait();
        }
        else{

            String regData = "INSERT INTO person(name, password, team, age)"+"VALUES(?,?,?,?)";
            connect= dataBase.connectDB("Formula1", "postgres", "Jorje0505");

            try{

                String checkUsername = "select name From person where name = '" + su_username.getText() + "'";

                prepare= connect.prepareStatement(checkUsername);
                result = prepare.executeQuery();

                if(result.next()){
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText(su_username.getText()+" is already taken");
                    alert.showAndWait();
                }else{
                    prepare = connect.prepareStatement(regData);
                    prepare.setString(1, su_username.getText());
                    prepare.setString(2,su_password.getText());
                    prepare.setString(3,su_favouriteTeam.getText());
                    prepare.setInt(4, Integer.parseInt(su_age.getText()));

                    prepare.executeUpdate();

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully registered account");
                    alert.showAndWait();

                    su_username.setText("");
                    su_password.setText("");
                    su_favouriteTeam.setText("");
                    su_age.setText("");

                    TranslateTransition slider = new TranslateTransition();

                    slider.setNode(side_form);
                    slider.setToX(300);
                    slider.setDuration(Duration.seconds(.5));

                    slider.setOnFinished((ActionEvent e) -> {
                        side_alreadyHave.setVisible(false);
                        side_CreateBtn.setVisible(true);
                    });

                    slider.play();
                }



            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private String[] questionList ={"Yes", "No"};

    public void switchForgotPass(){

        fp_questionForm.setVisible(true);
        si_loginForm.setVisible(false);

    }

    public void proceedBtn(){

        if(fp_username.getText().isEmpty()){

            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all blank fields");
            alert.showAndWait();
        }
        else{

            String selcData = "SELECT name FROM person WHERE name = ?";
            connect = dataBase.connectDB("Formula1", "postgres", "Jorje0505");

            try {

                prepare = connect.prepareStatement(selcData);
                prepare.setString(1,fp_username.getText());

                result = prepare.executeQuery();

                if(result.next()){
                    np_newPassForm.setVisible(true);
                    fp_questionForm.setVisible(false);
                }else {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Incorrect username");
                    alert.showAndWait();
                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void changePassBtn(){

        if(np_newPassword.getText().isEmpty() || np_confirmPassword.getText().isEmpty()){
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all blank fields");
            alert.showAndWait();
        }else{

            if(np_newPassword.getText().equals(np_confirmPassword.getText())){

                connect = dataBase.connectDB("Formula1", "postgres", "Jorje0505");

                try {

                    String updatePass ="UPDATE person SET password = '"
                            + np_newPassword.getText() + "' " +
                            "WHERE name = '"+ fp_username.getText() +"'";

                    prepare = connect.prepareStatement(updatePass);
                    prepare.executeUpdate();

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Password changed");
                    alert.showAndWait();

                    si_loginForm.setVisible(true);
                    np_newPassForm.setVisible(false);

                    np_newPassword.setText("");
                    np_confirmPassword.setText("");
                    fp_username.setText("");

                }catch (Exception e){
                    e.printStackTrace();
                }
            }else {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Not Match");
                alert.showAndWait();
            }

        }

    }

    public void backToLogin(){
        si_loginForm.setVisible(true);
        fp_questionForm.setVisible(false);
    }

    public void backToQF(){
        fp_questionForm.setVisible(true);
        np_newPassForm.setVisible(false);
    }

    public void switchForm(ActionEvent event){

        TranslateTransition slider = new TranslateTransition();

        if(event.getSource() == side_CreateBtn){
            slider.setNode(side_form);
            slider.setToX(0);
            slider.setDuration(Duration.seconds(.5));
            
            slider.setOnFinished((ActionEvent e) -> {
                side_alreadyHave.setVisible(true);
                side_CreateBtn.setVisible(false);

                fp_questionForm.setVisible(false);
                si_loginForm.setVisible(true);
                np_newPassForm.setVisible(false);

            });

            slider.play();
        } else if (event.getSource() == side_alreadyHave) {

            slider.setNode(side_form);
            slider.setToX(300);
            slider.setDuration(Duration.seconds(.5));

            slider.setOnFinished((ActionEvent e) -> {
                side_alreadyHave.setVisible(false);
                side_CreateBtn.setVisible(true);

                fp_questionForm.setVisible(false);
                si_loginForm.setVisible(true);
                np_newPassForm.setVisible(false);

            });

            slider.play();
        }
    }
}