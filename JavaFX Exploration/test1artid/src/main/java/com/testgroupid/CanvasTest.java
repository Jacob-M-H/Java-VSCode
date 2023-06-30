package com.testgroupid;
 

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image; 
import javafx.scene.input.KeyCombination;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class CanvasTest extends Application{
     public static void selfInfo(){
        System.out.println("Goal 1 - Create canvas in a window.\nGoal 2 - Resize the canvas in the window.\nGoal 3 - Drag self around the canvas when left clicking and dragging\nGoal 4 - Drag window view over the canvas using scrollbars/middle click isntead of left click if possible toggle\nGoal 5 - Resize the view of the canvas (like zoom out) without 'actually' decreasing the size. For example creating a circle on the screen on click should estimate the prefered position and blip an appropraite size in the canvas.\nGoal 6 - Convention for 'drawing' on the canvas.\nGoal 7 - Click and drag items already placed on the canvas.\nGoal 8 - Track the order of items, in the future layer. Canvas base layer should be white, but the layer itself should be transparent when not selected, and if a lower layer is selected (furthest to the left in a tabView), everything ot the right should be completely transparent drawing and all.\nGoal 9 - Clean code, critique.");
    }
    public static void main(String[] args){
        launch(args); //from Application Parent, sends string to launch method, then start method automatically called.
    }

    //All caps are reserved for constants.
    int defaultWidth=420;
    int defaultHeight=420;
    boolean isResizable=true;
    String iconPath ="/Hilst Paint Icon.png";
    String stageTitle = "Canvas Goal 1";
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

    @Override //it gets upset at me if I don't have hte Stage stage argument
    public void start(Stage stage) throws Exception{ //Stage primaryStage) { //throws Exception {
       
        
        CanvasTest.selfInfo();
        //throw new UnsupportedOperationException("Unimplemented method 'start'");
        
        //Set up the Scene
        //Stage stage=new Stage();  
        

        //Root stuff
        Group root =new Group();
        Canvas writableArea=new Canvas();
        ScrollPane sp=new ScrollPane(writableArea);
        root.getChildren().add(sp); 
        sp.setFitToWidth(true);
        writableArea.setWidth(defaultWidth*2);
        Rectangle rect = new Rectangle(5, 5, 5, 5);
        sp.setContent(writableArea); 
    
        
        /*The few important difference between Pane and Group is that :
        Pane can have its own size, where as a Group will take on the collective 
            bounds of its children and is not directly resizable.
        Pane can be used when you want to position its nodes at 
            absolute position. */
            //https://docs.oracle.com/javase/8/javafx/api/javafx/scene/Parent.html
        
        //I shall keep Parent as the root, since it seems to have the subchildren of the major possible root nodes usually.
        
            

        //Scene defaults
            int sceneWidth = 420; //The size of the stage takes priority, thus these values are less important to me.
            int sceneHeight = 420;
            Color defaultSceneColor =Color.BLACK;

        
        Scene scene = new Scene(root, sceneWidth, 
            sceneHeight, defaultSceneColor); //Scene requires at least 1 node, the root node.
        
        
        
        //Set stage attributes
        this.setDefaults(stage); 

        //Set and show the scene on the stage.
        stage.setScene(scene);
        stage.show(); //This should be kept at the bottom of 'start' func. 
        //All javafx applications use start() as strating point for construting javaFX application.
        
    }
     
}
