package com.testgroupid;

public class HelloWorld {
    public static void main(String args[]){
        System.out.println("Hello World");
    }
}
/* 
package com.testgroupid;
 

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
//import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
//import javafx.scene.canvas.Canvas;
//import javafx.scene.control.Button;
//import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image; 
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
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
    double defaultWidth=420;
    double defaultHeight=420;
    boolean isResizable=true;
    String iconPath ="/Hilst Paint Icon.png";
    String stageTitle = "Canvas Goal 1";
    int windowSpawnX=0;
    int windowSpawnY=0;
    boolean isFullScreen = false; 
    
    //Scene defaults -convention, -1 means not set yet. 
        //Scene Stage discrepency. If set to the same dimensions of the stage, it is 'larger' than the stage viewport. Figured out 14 pixels wider than the viewport, 38 taller than the viewport.
        double sceneWidth; //The size of the stage takes priority, thus these values are less important to me.
        double sceneHeight;
        Color defaultSceneColor =Color.BLUE; 
        final double initialSceneWidth=420;
        final double initialSceneHeight=420;
        //Note to be 'flush' with the bottom, assume there are ~2 pixels further on the Y/X axis, Likely consequense of hte Pixel Coordinate system detailedi n Node class.  
        double decorW;
        double decorH;
    //Scrollable stuff
        AnchorPane backBone=new AnchorPane();
        AnchorPane innerPane = new AnchorPane(); 
        ScrollBar moveInnerH=new ScrollBar();
        ScrollBar moveInnerV =new ScrollBar(); 
        //Inner and Backbone color stuff
            BackgroundFill backBoneColor =
                new BackgroundFill(
                    Color.valueOf("#ff0fff"),
                    new CornerRadii(0),
                    new Insets(0)
                    ); 
            Background backBoneBackground =
                new Background(backBoneColor); 
            BackgroundFill innerPaneColor =  new BackgroundFill(
                    Color.valueOf("#0EE3ED"),
                    new CornerRadii(0),
                    new Insets(0)
                    ); 
            Background innerPaneBackground =
                new Background(innerPaneColor); 
 

    String stuff[] ={};
        
    public Stage setDefaultsStage(Stage stage){ 
        
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
        stage.setMinHeight(defaultHeight);
        stage.setMinWidth(defaultWidth);
        //stage.setFullScreen(true);
        stage.setFullScreenExitHint("To Escape Fullscreen, press q");
        stage.setFullScreenExitKeyCombination(KeyCombination.valueOf("q"));
        return stage;
    }
 

    private void observer(Rectangle obs){
        String list="";
        list="getAccessibleHelp="+obs.getAccessibleHelp()+"\n";
        list=list+"getAccessibleRoleDescription="+obs.getAccessibleRoleDescription()+"\n"
        +"getAccessibleText="+obs.getAccessibleText()+"\n"
        +"getArcHeight="+obs.getArcHeight()+"\n"
        +"getArcWidth="+obs.getArcWidth()+"\n"
        +"getBaselineOffset="+obs.getBaselineOffset()+"\n"
        +"getBoundsInLocal="+obs.getBoundsInLocal()+"\n"
        +"getBoundsInParent="+obs.getBoundsInParent()+"\n"
        +"getClip="+obs.getClip()+"\n"
        +"getContentBias="+obs.getContentBias()+"\n"
        +"getCursor="+obs.getCursor()+"\n"
        +"getEventDispatcher="+obs.getEventDispatcher()+"\n"
        +"getFill="+obs.getFill()+"\n"
        +"getHeight="+obs.getHeight()+"\n"
        +"getId="+obs.getId()+"\n"
        +"getInputMethodRequests="+obs.getInputMethodRequests()+"\n"
        +"getLayoutBounds="+obs.getLayoutBounds()+"\n"
        +"getLayoutX="+obs.getLayoutX()+"\n"
        +"getLayoutY="+obs.getLayoutY()+"\n"
        +"getLocalToParentTransform="+obs.getLocalToParentTransform()+"\n"
        +"getLocalToSceneTransform="+obs.getLocalToSceneTransform()+"\n"
        +"getOnContextMenuRequested="+obs.getOnContextMenuRequested()+"\n"
        +"getOnDragDetected="+obs.getOnDragDetected()+"\n"
        +"getOnInputMethodTextChanged="+obs.getOnInputMethodTextChanged()+"\n"
        +"getOnMouseClicked="+obs.getOnMouseClicked()+"\n"
        +"getOnMouseEntered="+obs.getOnMouseEntered()+"\n"
        +"getOnMouseExited="+obs.getOnMouseExited()+"\n"
        +"getOnRotate="+obs.getOnRotate()+"\n"
        +"getOnScroll="+obs.getOnScroll()+"\n"
        +"getOpacity="+obs.getOpacity()+"\n"
        +"getProperties="+obs.getProperties()+"\n"
        +"getRotate="+obs.getRotate()+"\n"
        +"getRotationAxis="+obs.getRotationAxis()+"\n"
        +"getScaleX="+obs.getScaleX()+"\n"
        +"getScaleY="+obs.getScaleY()+"\n"
        +"getScaleZ="+obs.getScaleZ()+"\n"
        +"getStroke="+obs.getStroke()+"\n"
        +"getStyle="+obs.getStyle()+"\n"
        +"getTransforms="+obs.getTransforms()+"\n"
        +"getTranslateX="+obs.getTranslateX()+"\n"
        +"getTranslateY="+obs.getTranslateY()+"\n"
        +"getTranslateZ="+obs.getTranslateZ()+"\n"
        +"getTypeSelector="+obs.getTypeSelector()+"\n"
        +"getUserData="+obs.getUserData()+"\n"
        +"getViewOrder="+obs.getViewOrder()+"\n"
        +"getWidth="+obs.getWidth()+"\n";

        System.out.println(list); 

    }


        //I beleive the scene is resized with the stage. 
    public void resizeComponentsX(){
        System.out.println("Resize X wise componenets\n");
    }
    public void resizeComponentsY(){
        System.out.println("Resize Y wise componenets\n");
    } 


    public void setScrollable(){ 
        this.backBone.setVisible(true);
        this.backBone.setCursor(Cursor.HAND);
        this.innerPane.setVisible(true);
        this.moveInnerV.setOrientation(Orientation.VERTICAL);
        this.moveInnerH.setOrientation(Orientation.HORIZONTAL);
        //Layout stuff
            this.backBone.setLayoutX(0);
            this.backBone.setLayoutY(0);
            this.backBone.setPrefWidth(this.sceneWidth);
            this.backBone.setPrefHeight(this.sceneHeight);
        
            this.moveInnerH.setLayoutX(10); 
            this.moveInnerH.setLayoutY(this.sceneHeight-14.25);    
            this.moveInnerH.setMaxWidth(this.sceneWidth-25);
            this.moveInnerH.setMinWidth(this.sceneWidth-25);
 
            this.moveInnerV.setLayoutX(this.sceneWidth-14.25); 
            this.moveInnerV.setLayoutY(10);
            this.moveInnerV.setMaxHeight(this.sceneHeight-25);
            this.moveInnerV.setMinHeight(this.sceneHeight-25);
              
            this.innerPane.setMinWidth(50);
            this.innerPane.setMinHeight(50); 
    }

    public void resizeScrollable(double X, double Y){ 
        this.sceneHeight=this.sceneHeight+Y;
        this.sceneWidth=this.sceneWidth+X;
        this.setScrollable();
    } 

    @Override //it gets upset at me if I don't have hte Stage stage argument
    public void start(Stage stage) throws Exception{ //Stage primaryStage) { //throws Exception {
       
        
        CanvasTest.selfInfo();
        
        /* 
        //throw new UnsupportedOperationException("Unimplemented method 'start'");
        
        //Set up the Scene
        //Stage stage=new Stage();  
        

        
        //Canvas writableArea=new Canvas();
        //ScrollPane sp=new ScrollPane(writableArea);
        //root.getChildren().add(sp); 
        //sp.setFitToWidth(true);
        //writableArea.setWidth(defaultWidth*2);
       // Rectangle rect = new Rectangle(5, 5, 5, 5);
        //sp.setContent(writableArea); 
          The few important difference between Pane and Group is that :
        Pane can have its own size, where as a Group will take on the collective 
            bounds of its children and is not directly resizable.
        Pane can be used when you want to position its nodes at 
            absolute position.  
            //https://docs.oracle.com/javase/8/javafx/api/javafx/scene/Parent.html
        
            //I shall keep Parent as the root, since it seems to have the subchildren of the major possible root nodes usually.
        */

      /*    
        Group root = new Group();  
        Scene scene = new Scene(root, initialSceneWidth, 
            initialSceneHeight, defaultSceneColor); 
        //Set stage attributes
        this.setDefaultsStage(stage); 
        this.backBone.setBackground(backBoneBackground); 
        this.innerPane.setBackground(innerPaneBackground);    
  
        //decoration discrepency.
        this.decorW=initialSceneWidth-scene.getWidth();
        this.decorH=initialSceneHeight-scene.getHeight(); 
        this.sceneHeight=initialSceneHeight-this.decorH;
        this.sceneWidth=initialSceneWidth-this.decorW;

        //Set up the 'Scrollable Space', backBone, moveInnerH/V, InnerPane.
        this.setScrollable();
         
         
        //Tree stucture:
        root.getChildren().addAll(backBone,moveInnerH,moveInnerV);  
        backBone.getChildren().add(innerPane);   
         
*/
/* 
    System.out.println(this.innerPane.getWidth());
        System.out.println(innerPane.getWidth()); 
        System.out.println(innerPane.getLayoutX());
        System.out.println(innerPane.getLayoutY());

        System.out.println(innerPane.getMaxHeight());
        System.out.println(innerPane.getMaxWidth());
        System.out.println(innerPane.widthProperty().doubleValue());
        System.out.println(innerPane.heightProperty().doubleValue()); 
        System.out.println(innerPane.boundsInLocalProperty()); 
        System.out.println(innerPane.boundsInParentProperty());
    */
    
/* 
        ScrollBar innerPaneScaleBar =new ScrollBar();
        innerPaneScaleBar.setLayoutX(moveInnerH.getLayoutX());
        innerPaneScaleBar.setLayoutY(moveInnerH.getLayoutY()-11);
        root.getChildren().add(innerPaneScaleBar);
        AnchorPane expandableZone = new AnchorPane(); 
        backBone.getChildren().add(0, expandableZone); 

         
        //Work in progress. I don't like how I only set the min's, and I'm not sure what the trickle down
            //effects of that choice is. Clean up later after scale investigation I think.
        double innerPaneWidth=50;
        double innerPaneHeight=50;
        innerPane.setLayoutX((sceneWidth-innerPaneWidth)/2);
        innerPane.setLayoutY((sceneHeight-innerPaneHeight)/2); 
        //Expandable stuff
            BackgroundFill expandableZoneColor =  new BackgroundFill(
                    Color.valueOf("#4E4E4E"),
                    new CornerRadii(0),
                    new Insets(0)
                    ); 
            Background expandableZoneBackground =
                new Background(expandableZoneColor); 
        expandableZone.setBackground(expandableZoneBackground);
        
        double expandableZoneWidth=innerPaneWidth+20;
        double expandableZoneHeight=innerPaneHeight+20;
        double eZbufferX=expandableZoneWidth-innerPaneWidth;
        double eZbufferY=expandableZoneHeight-innerPaneHeight;
        expandableZone.setMinWidth(expandableZoneWidth);
        expandableZone.setMinHeight(expandableZoneHeight); 
        expandableZone.setLayoutX(innerPane.getLayoutX()-(eZbufferX/2));
        expandableZone.setLayoutY(innerPane.getLayoutY()-(eZbufferY/2));
         
        //Reports
        Label iPWReport =new Label("iPW:"+String.valueOf(innerPaneWidth));
        Label iPHReport =new Label("iPH:"+String.valueOf(innerPaneHeight));
        Label iPSXReport =new Label("iPSX:"+String.valueOf(innerPane.getScaleX()));
        Label iPSYReport =new Label("iPSY:"+String.valueOf(innerPane.getScaleY()));
        AnchorPane Report=new AnchorPane();
        root.getChildren().add(Report);
        Report.getChildren().addAll(iPHReport, iPWReport,iPSXReport, iPSYReport);
        iPWReport.setLayoutY(0);
        iPHReport.setLayoutY(10);
        iPSXReport.setLayoutY(20);
        iPSYReport.setLayoutY(30);

        //set scrollbar's to midpoint (Since the image is in the middle)
        moveInnerH.setValue(50);
        moveInnerV.setValue(50);

        //Add listeners.
        moveInnerH.valueProperty().addListener(new ChangeListener<Number>()  { 
            @Override
            public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
                System.out.println("Current Value of moveInnerH: "+moveInnerH.getValue());
                System.out.println("DecipherH: "+arg0+" "+ arg1+" "+arg2);
                innerPane.setLayoutX(innerPane.getLayoutX()-50+moveInnerH.getValue());
                innerPane.setLayoutX(expandableZone.getLayoutX()-50+moveInnerH.getValue());
            } 
            
        });
        moveInnerV.valueProperty().addListener(new ChangeListener<Number>()  { 
            @Override
            public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
                System.out.println("Current Value of moveInnerV: "+moveInnerV.getValue());
                System.out.println("DecipherV: "+arg0+" "+ arg1+" "+arg2);
                innerPane.setLayoutY(innerPane.getLayoutY()-50+moveInnerV.getValue());
                innerPane.setLayoutY(expandableZone.getLayoutY()-50+moveInnerV.getValue());
            } 
            
        });
        //this.resizeScrollableInnerPane();
        innerPane.setOnMouseEntered(new EventHandler<Event>() { 
            @Override
            public void handle(Event event){
                System.out.println("InnerPane " + event.getEventType());
            }
        });
        innerPane.setOnMouseExited(new EventHandler<Event>() { 
            @Override
            public void handle(Event event){
                System.out.println("InnerPane " + event.getEventType());
            }
        });
        backBone.setOnMouseEntered(new EventHandler<Event>() { 
            @Override
            public void handle(Event event){
                System.out.println("backPane " +event.getEventType());
            }
        });
        backBone.setOnMouseExited(new EventHandler<Event>() { 
            @Override
            public void handle(Event event){
                System.out.println("backPane "+event.getEventType());
            }
        });
  

//        innerPane.setScaleX(decorH);

        //FUTURE: Resize all elements appropraitely after screen size changes.
        stage.widthProperty().addListener((obs, oldVal, newVal) -> { 
            this.resizeComponentsX();
            //System.out.println(newVal.doubleValue()-oldVal.doubleValue()); 
            this.resizeScrollable(newVal.doubleValue()-oldVal.doubleValue(), 0);

            
        });
        stage.heightProperty().addListener((obs, oldVal, newVal) -> { 
            this.resizeComponentsY();
            //System.out.println(newVal.doubleValue()-oldVal.doubleValue()); 
            this.resizeScrollable(0,newVal.doubleValue()-oldVal.doubleValue());
        }); 
        


        //Set and show the scene on the stage.
        stage.setScene(scene);  
        stage.show();  
    }
     
}
*/