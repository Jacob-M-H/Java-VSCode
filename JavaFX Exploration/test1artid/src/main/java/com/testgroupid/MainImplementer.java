package com.testgroupid;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Parent; 
import javafx.stage.Stage;

import java.io.IOException;



public class MainImplementer extends Application{
    public static void selfInfo(){
        System.out.println("Hello! This file is to instantiate and test features of JavaFX to be used in JH Paint, a clone/improvement to MSPaint.");
        //By using Maven and JavaFX, this project should be able to be ported and wrapped up quickly when finished, and be functional on multiple platforms.

        //FXML, XML basaed markup language for GUI.
        //Scene builder - generates FXML markup which can port to an IDE
        //Web view - web pages embedded with javaFX apps, web view uses webkithtml to embde web pages
        //built in UI controls - JAVAfx built in componenets not dependnet on OS. UI componenets enough to develop app.
        //CSS like styling. improves style of app. 
        //Swing interoperabiltiy, can be embedded with swing code using swing node class. 
        //Canvas API - drawing direclty in an area of JAVAFX scene
        //integrated graphcis library -classes to deal with 2D and 3D graphics.
        //Graphics baedo n rendered pipeline(prisim) hardware accelerated graphis.
        //Media pipeline supports playback of web multimedia on low latency.
        //Self contained app deployment. 


    }

    public static void main(String[] args){
        launch(args); //from Application Parent, sends string to launch method, then start method automatically called.
    }

    @Override //it gets upset at me if I don't have hte Stage stage argument
    public void start(Stage stage) throws Exception{ //Stage primaryStage) { //throws Exception {
       
        MainImplementer.selfInfo();
        //throw new UnsupportedOperationException("Unimplemented method 'start'");
        
        //Stage stage=new Stage();  
        Group root = new Group(); //Note there are different root nodes, FUTURE.
        Scene scene = new Scene(root); //Scene requires at least 1 node, the root node.


        stage.setScene(scene);
        stage.show(); //This should be kept at the bottom of 'start' func. 
        //All javafx applications use start() as strating point for construting javaFX application.
        
    }
     
}
