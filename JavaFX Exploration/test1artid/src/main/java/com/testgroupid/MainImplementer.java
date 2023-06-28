package com.testgroupid;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Parent; 
import javafx.stage.Stage;

//import java.io.File;
//import java.io.IOException;
//import java.nio.file.Path;



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

    //All caps are reserved for constants.
    int defaultWidth=420;
    int defaultHeight=420;
    boolean isResizable=true;
    String iconPath ="/Hilst Paint Icon.png";
    String stageTitle = "Hilst Paint";
    int windowSpawnX=0;
    int windowSpawnY=0;
    boolean isFullScreen = false;

    public Stage setDefaults(Stage stage){ 
        
        //Additional elements
        Image icon = new Image(iconPath); // the / means relative path. Found the item in Resources as desired.
        //defaults
        stage.setTitle(stageTitle);
        stage.getIcons().add(icon);
        stage.setWidth(this.defaultWidth);
        stage.setHeight(this.defaultHeight);
        stage.setResizable(this.isResizable);
        stage.setX(windowSpawnX);
        stage.setY(windowSpawnY);
        stage.setFullScreen(isFullScreen);
        //stage.setFullScreen(true);
        stage.setFullScreenExitHint("To Escape Fullscreen, press q");
        stage.setFullScreenExitKeyCombination(KeyCombination.valueOf("q"));
        return stage;
    }


    public Group exampleNodes(Group root){ 
        Color defaultTestColor=Color.BLUE;
        
        //Example adding text to the scene
            Text text =new Text();
            text.setText("test Text");
            text.setX(50);
            text.setY(50);
            text.setFont(Font.font("Times New Roman", FontWeight.NORMAL, FontPosture.REGULAR, 20));
            text.setFill(defaultTestColor);
            root.getChildren().add(text);

        //Example adding Line object
            Line line=new Line();
            line.setStartX(20);
            line.setStartY(60);
            line.setEndX(50);
            line.setEndY(65);
            line.setStroke(defaultTestColor);
            line.setOpacity(0.5);
            line.setStrokeWidth(5);
            root.getChildren().add(line);

        //Example adding Rectangle object
        Rectangle rectangle = new Rectangle();
        rectangle.setX(100);
        rectangle.setY(100);
        rectangle.setWidth(50);
        rectangle.setHeight(50);
        rectangle.setFill(Color.TRANSPARENT); 
        rectangle.setStrokeWidth(5);
        rectangle.setStroke(defaultTestColor);
        root.getChildren().add(rectangle);

        //Example adding Polygon/circle object
            Polygon triangle = new Polygon(); //Not expected behavior.
            triangle.getPoints().setAll(
                200.0,200.0,
                300.0, 300.0,
                200.0, 200.0
                );
            triangle.setFill(defaultTestColor);
            triangle.setStroke(defaultTestColor);
            triangle.setOpacity(1);
            triangle.setStrokeWidth(5); 
            
            Circle circle = new Circle();    
            circle.setCenterX(350);
            circle.setCenterY(350);        
            circle.setRadius(20);
            circle.setFill(defaultTestColor);
            root.getChildren().add(triangle); 
            root.getChildren().add(circle);

        //Example adding Image object
            Image image = new Image(this.iconPath);
            ImageView imageView=new ImageView(image);
            imageView.setX(10);
            imageView.setY(10);
            imageView.setOpacity(.1);
            image=null; //temp object set to null, signals garbage collector.
            root.getChildren().add(imageView);

        return root;
    }
    
    
     

    @Override //it gets upset at me if I don't have hte Stage stage argument
    public void start(Stage stage) throws Exception{ //Stage primaryStage) { //throws Exception {
       
        
        MainImplementer.selfInfo();
        //throw new UnsupportedOperationException("Unimplemented method 'start'");
        
        //Set up the Scene
        //Stage stage=new Stage();  
        

        //Root stuff
             
            //Root node based on fxml file! 
            System.out.println("Try loading test Main fxml!");
            //Parent root=FXMLLoader.load(getClass().getResource("C:/Users/thebl/Java VSCode/JavaFX Exploration/test1artid/src/main/resources/com/testgroupid/testMain.fxml"));
            try{
            //System.out.println("Get Path to fxml");
            //Path path = new File(getClass().getResource("/primary.fxml").getFile()).toPath();
            //System.out.println("Path found: "+path.toString());
            String fxml = "testMain";
            System.out.println("FXMLLoader");
            FXMLLoader fxmlLoader = new FXMLLoader(MainImplementer.class.getResource(fxml+".fxml"));
            System.out.println("FXMLLoader.load");
            Parent root = fxmlLoader.load();
            //Parent root=FXMLLoader.load(getClass().getResource("C:/Users/thebl/Java VSCode/JavaFX Exploration/test1artid/src/main/resources/com/testgroupid/primary.fxml"));
            System.out.println("Loaded!");

            }
            catch(Exception e){
                e.printStackTrace();
                System.out.println("Failed 1!");
                //KEEP: (comment out when above works.)
                 //Group root = new Group(); //Note there are different root nodes, FUTURE.
            
            }
        
           
            
            Group root = new Group(); //Note there are different root nodes, FUTURE.
            
            

        //Scene defaults
            int sceneWidth = 420; //The size of the stage takes priority, thus these values are less important to me.
            int sceneHeight = 420;
            Color defaultSceneColor =Color.BLACK;

        
        Scene scene = new Scene(root, sceneWidth, 
            sceneHeight, defaultSceneColor); //Scene requires at least 1 node, the root node.
        
        
        //this.exampleNodes(root);


        //Set stage attributes
        this.setDefaults(stage); 

        //Set and show the scene on the stage.
        stage.setScene(scene);
        stage.show(); //This should be kept at the bottom of 'start' func. 
        //All javafx applications use start() as strating point for construting javaFX application.
        
    }
     
}
