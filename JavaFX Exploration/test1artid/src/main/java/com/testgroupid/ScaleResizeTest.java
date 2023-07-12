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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
  
 


public class ScaleResizeTest extends Application{
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

    double scrollbarThick=15;
    
    //Scene defaults -convention, -1 means not set yet. 
        //Scene Stage discrepency. If set to the same dimensions of the stage, it is 'larger' than the stage viewport. Figured out 14 pixels wider than the viewport, 38 taller than the viewport.
        double sceneWidth; //The size of the stage takes priority, thus these values are less important to me.
        double sceneHeight;
        Color defaultSceneColor =Color.BLUE; 
        final double initialSceneWidth=420; //After discrepency, used for backBone and other's initial values.
        final double initialSceneHeight=420;
        //Note to be 'flush' with the bottom, assume there are ~2 pixels further on the Y/X axis, Likely consequense of hte Pixel Coordinate system detailedi n Node class.  
        double decorW;
        double decorH;

    //Inner Pane stuff
        double iPtoAnchorX=30;
        double iPtoAnchorY=30;
        double eZtoAnchorX=10;
        double eZtoAnchorY=10;
    double initialInnerPaneWidth=initialSceneWidth-iPtoAnchorX; //iP 30 pixels from window border initially, 
    double initialInnerPaneHeight=initialSceneHeight-iPtoAnchorY;
    double initialExpandableZoneWidth=initialSceneWidth-eZtoAnchorX; //eZ 10 pixels from window border initially,
    double initialExpandableZoneHeight=initialSceneHeight-eZtoAnchorY;
    //the constant distance between ip and ez, divided by 2 to get the total 'shadow' ip casts on ez.
    double eZbufferX=initialExpandableZoneWidth-initialInnerPaneWidth;
    double eZbufferY=initialExpandableZoneHeight-initialInnerPaneHeight;


    //Scrollable stuff
        AnchorPane backBone=new AnchorPane();
        AnchorPane innerPane = new AnchorPane(); 
        ScrollBar moveInnerH=new ScrollBar();
        ScrollBar moveInnerV =new ScrollBar(); 
        
        AnchorPane expandableZone = new AnchorPane(); 
        ScrollBar zoomBar =new ScrollBar(); 

        
        AnchorPane Report=new AnchorPane(); //For displaying information, not required in final version
        //Background stuff 
            //#region
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
            BackgroundFill expandableZoneColor =  new BackgroundFill(
                    Color.valueOf("#4E4E4E"),
                    new CornerRadii(0),
                    new Insets(0)
                    ); 
            Background expandableZoneBackground =
                new Background(expandableZoneColor); 
            //#endregion
    



    //Translucent Backgrond -ERROR, takes on color of parent, not true transparency. 
    BackgroundFill transFill =  new BackgroundFill(
                    Color.valueOf("#00FFFFFF"),
                    new CornerRadii(0),
                    new Insets(0)
                    ); 
            Background transFillBackground =
                new Background(transFill); 
 

    String stuff[] ={};
        

    private double min(double eZScaleX, double eZScaleY) {
        if (eZScaleX <= eZScaleY){
            return eZScaleX;
        }
        else {
            return eZScaleY;
        }
    } 

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
 

    public void removeReport(Group container){
        container.getChildren().remove(Report);
        Report.getChildren().clear();
    }
    public void setReport(Group container, int lever){
        //version that takes in account container size, a number over levers, and 
        //a running value for moving the text around, would be nice. 
        try{
            removeReport(container);
        }
        finally{
            System.out.println("Try lever: "+ lever);
            switch(lever){
                case 0: 
                        Label iPWReport =new Label("iPW:"+String.valueOf(initialInnerPaneWidth));
                        Label iPHReport =new Label("iPH:"+String.valueOf(initialInnerPaneHeight));
                        Label iPSXReport =new Label("iPSX:"+String.valueOf(innerPane.getScaleX()));
                        Label iPSYReport =new Label("iPSY:"+String.valueOf(innerPane.getScaleY()));  
                        Label iPXLay =new Label("IPXLay:"+String.valueOf(innerPane.getLayoutX()));
                        Label iPYLay =new Label("IPXLay:"+String.valueOf(innerPane.getLayoutY()));
                        
                        Report.getChildren().addAll(iPHReport, iPWReport,iPSXReport, iPSYReport, 
                        iPXLay, iPYLay);
                        iPWReport.setLayoutY(0);
                        iPHReport.setLayoutY(10);
                        iPSXReport.setLayoutY(20);
                        iPSYReport.setLayoutY(30);
                        iPXLay.setLayoutY(40);
                        iPYLay.setLayoutY(50);
                        break;
                
            }

            container.getChildren().add(Report);
        }
    }

        //I beleive the scene is resized with the stage. 
    
    //BUG: Pref is not enough! Not sure why
    //TODO: change these to bindings, though depending on the extent of the GUI, might be best to leave them as functions
    //TODO: change the setting of width and height to be more succinct, min/max/prefDIM isn't what I want to keep. 
    public void resizeBackBoneX(double width){
            this.backBone.setPrefWidth(width);
        }
    public void resizeBackBoneY(double height){
            this.backBone.setPrefHeight(height);
        }
    public void resizeInnerPaneX(double width){
            this.innerPane.setMinWidth(width); 
            this.innerPane.setMaxWidth(width);
            this.innerPane.setPrefWidth(width); 
            
        }
    public void resizeInnerPaneY(double height){
            this.innerPane.setMinHeight(height); 
            this.innerPane.setMaxHeight(height); 
            this.innerPane.setPrefHeight(height); 
        }
    public void resizeExpandableZoneX(double width){
            this.expandableZone.setMinWidth(width);
            this.expandableZone.setMaxWidth(width);
            this.expandableZone.setPrefWidth(width);
        }
    public void resizeExpandableZoneY(double height){
            this.expandableZone.setMinHeight(height); 
            this.expandableZone.setMaxHeight(height); 
            this.expandableZone.setPrefHeight(height); 
        }
    public void resizeMoveInnerH(double width, double yCord){ 
            this.moveInnerV.setPrefHeight(scrollbarThick);
            this.moveInnerV.setMinHeight(scrollbarThick); 
            this.moveInnerV.setMaxHeight(scrollbarThick);
            this.moveInnerH.setLayoutX(10);  //roughly how far from X axis I want to be
            this.moveInnerH.setLayoutY(yCord-scrollbarThick+1); //roughly the height of the scrollbar   
            this.moveInnerH.setMaxWidth(width-25);
            this.moveInnerH.setMinWidth(width-25);
        }
    public void resizeMoveInnerV(double height, double xCord){
            this.moveInnerV.setPrefWidth(scrollbarThick);
            this.moveInnerV.setMinWidth(scrollbarThick); 
            this.moveInnerV.setMaxWidth(scrollbarThick);
            this.moveInnerV.setLayoutX(xCord-scrollbarThick); //rouhgly how thic scrollbar is 14.25
            this.moveInnerV.setLayoutY(10);
            this.moveInnerV.setMaxHeight(height-25);
            this.moveInnerV.setMinHeight(height-25); 
            
        }

    public void setScrollable(double refW, double refH){  
        this.backBone.setVisible(true);
        this.backBone.setCursor(Cursor.HAND);
        backBone.setBackground(backBoneBackground); 
        this.innerPane.setVisible(true);      
        this.expandableZone.setVisible(true);
        expandableZone.setBackground(expandableZoneBackground);
        innerPane.setBackground(innerPaneBackground);     
        this.moveInnerV.setOrientation(Orientation.VERTICAL);
        this.moveInnerH.setOrientation(Orientation.HORIZONTAL);    

        //Layout stuff
            this.backBone.setLayoutX(0);
            this.backBone.setLayoutY(0);   
            resizeBackBoneX(refW);
            resizeBackBoneY(refH);
            resizeMoveInnerH(refW, refH);
            resizeMoveInnerV(refH,refW); 
            
            //WARNING: This might want ot set in another section. Not sure, base on ratio maybe?
            resizeInnerPaneX(initialInnerPaneWidth);
            resizeInnerPaneY(initialInnerPaneHeight);
            innerPane.setLayoutX((refW-initialInnerPaneWidth)/2);
            innerPane.setLayoutY((refH-initialInnerPaneHeight)/2); 
            System.out.println("iP PrefW: "+innerPane.getPrefWidth()
            +" iP PrefH: "+innerPane.getPrefHeight());
 
             
            resizeExpandableZoneX(initialExpandableZoneWidth);
            resizeExpandableZoneY(initialExpandableZoneHeight);
            expandableZone.setLayoutX(innerPane.getLayoutX()-(eZbufferX/2));
            expandableZone.setLayoutY(innerPane.getLayoutY()-(eZbufferY/2));
 
            //Align zoombar with H-bar, may be moved later.
            zoomBar.setMaxHeight(scrollbarThick);
            zoomBar.setLayoutX(moveInnerH.getLayoutX());
            zoomBar.setLayoutY(moveInnerH.getLayoutY()-scrollbarThick+1);
            //Center the scrollbars -off set in the listener, work required in scale after that test to properly set everything up. 
            
            //moveInnerH.setValue(0);
            //moveInnerV.setValue(0);
            zoomBar.setValue(100);


        if (false){
        System.out.println("decorH: "+this.decorH+"\n"+
                            "decorW: "+this.decorW+"\n"+
                            "sceneHeight: "+this.sceneHeight+"\n"+
                            "sceneWidth: "+this.sceneWidth+"\n"+
                            "initialSceneHeight: "+this.initialSceneHeight+"\n"+
                            "initialSceneWidth: "+this.initialSceneWidth+"\n");
        }
               
    }

    public void resizeScrollable(double X, double Y){ 
        this.sceneHeight=this.sceneHeight+Y;
        this.sceneWidth=this.sceneWidth+X;
        //this.setScrollable(this.sceneWidth, this.sceneHeight);
    } 
    public void figureDecorDiscrep(Scene scene){        
        //Note, this might need to be adjusted in the future to allow for integer perfect pixels
        //decoration discrepency.
        this.decorW=initialSceneWidth-scene.getWidth();
        this.decorH=initialSceneHeight-scene.getHeight(); 
        this.sceneHeight=initialSceneHeight-this.decorH;
        this.sceneWidth=initialSceneWidth-this.decorW;
    }

    public double round(double rMe, double rBoundry){
        //Asssert: rBoundry is positive.
        boolean example =false;
        if (example){
        System.out.println(
            "test 1: "+round(16.43,.25)+"\n"+
            "test 2: "+round(-16.43,.25)+"\n"+
            "test 3: "+round(16.43,-.25)+"\n"+
            "test 4: "+round(-16.43,-.25)+"\n"+
            "test 1: "+round(16.43,1.25)+"\n"+
            "test 2: "+round(-16.43,1.25)+"\n"+
            "test 3: "+round(16.43,-1.25)+"\n"+
            "test 4: "+round(-16.43,-1.25)+"\n"
        );}


        if (rBoundry!=0){
            double remainder;
            double kept;
            remainder = rMe%rBoundry;
            kept=rMe-remainder;
            double lowerB=0;
            double upperB=0;
            if (rBoundry<0){
                rBoundry=Math.abs(rBoundry);
            }
            
            if (rBoundry>0) { //Clean this later. unnecessarily complex. Simply take 1/2 of boundry and compare whether the remainder is less or greater than this value.
                while(upperB<remainder){
                    upperB+=rBoundry;
                }
                lowerB=upperB-rBoundry;
            }
            else {
                while(lowerB>remainder){
                    lowerB-=rBoundry;
                }
                upperB=lowerB+rBoundry;
            }
            if (Math.abs(Math.abs(upperB)-Math.abs(remainder))<Math.abs(Math.abs(lowerB)-Math.abs(remainder))){
                //round up
                kept+=upperB;
            } else{
                kept+=lowerB;
            }
            System.out.println("LB, UB:"+lowerB+" "+upperB+" Kept: "+kept+" remainder:"+remainder+"\n");

            return kept;

        } 
        else if (rBoundry==0) {
            return Math.round(rMe);//round up or down to nearest .0 value
        } 
        return Double.MAX_VALUE; //to signal bad
    }
    

    @Override //it gets upset at me if I don't have hte Stage stage argument
    public void start(Stage stage) throws Exception{ //Stage primaryStage) { //throws Exception {

        ScaleResizeTest.selfInfo(); 
        Group root = new Group();  
        Scene scene = new Scene(root, initialSceneWidth, 
            initialSceneHeight, defaultSceneColor);
        stage=this.setDefaultsStage(stage);  
        //Set and show the scene on the stage.
        stage.setScene(scene);  
        stage.show();  //IF THIS IS MOVED, THINGS DON:T GO SUPER WELL. WHY?

        //details on layout, discrepency
        figureDecorDiscrep(scene);
        stage.setWidth(decorW+initialSceneWidth);
        stage.setHeight(decorH+initialSceneWidth);
        stage.setMinHeight(decorH+initialSceneHeight);
        stage.setMinWidth(decorW+initialSceneWidth);
        this.setScrollable(initialSceneWidth, initialSceneHeight);    
         
        //Tree stucture:
        root.getChildren().addAll(backBone);  
        backBone.getChildren().addAll(
            this.expandableZone,this.innerPane, //panes
            moveInnerH,moveInnerV,zoomBar //scrollbars
            );   
         
 
        
          
         
          
         
         
        //Reports - new container option that will hold labels for experimentation. 
        setReport(root, 0); 
 
         
        resizeExpandableZoneX((initialExpandableZoneWidth+5)*2);//840
        resizeExpandableZoneY((initialExpandableZoneHeight+5)*2);//840
        resizeInnerPaneX((initialInnerPaneWidth+15)*2);//720
        resizeInnerPaneY((initialInnerPaneHeight+15)*2);//720
        //Protocol
        if (expandableZone.getWidth()!=initialSceneWidth-eZtoAnchorX ||
        expandableZone.getHeight()!=initialSceneHeight-eZtoAnchorY){ 
            //UNTESTED : Will have to load file, or set eZ/iP to larger than sceneInitials.

            //doesn't exaclty matter which. 
            //Buffer needs ot be changed. what if we have a rectangular cnavas?
            //figure out which is out of bounds, or the most extensivly out of bound.
            //(scene-buffer)/currentDimEZ, doesn't factor in rectnagle. 
            double eZScaleX=(initialSceneWidth-eZtoAnchorX)/expandableZone.getMinWidth();
            double eZScaleY=(initialSceneHeight-eZtoAnchorY)/expandableZone.getMinHeight();
            //Prioritize getting all in frame, we do not to 'warp' the image by setting the scale of X or Y independently
            double eZPriortyScale=min(eZScaleX,eZScaleY);
            expandableZone.setScaleX(eZPriortyScale);
            expandableZone.setScaleY(eZPriortyScale);

            //Same for iP, a bit redundant though.
            double iPScaleX=(initialSceneWidth-iPtoAnchorX)/innerPane.getMinWidth();
            double iPScaleY=(initialSceneHeight-iPtoAnchorY)/innerPane.getMinHeight();
            double iPPriorityScale=min(iPScaleX, iPScaleY);
            innerPane.setScaleX(iPPriorityScale);
            innerPane.setScaleY(iPPriorityScale);
            System.out.println("Priority Scale:\neZ: "+eZPriortyScale+"\n iP: "+iPPriorityScale);
            expandableZone.setLayoutX(eZtoAnchorX/2);
            expandableZone.setLayoutY(eZtoAnchorY/2);
            innerPane.setLayoutX(iPtoAnchorX/2);
            innerPane.setLayoutY(iPtoAnchorY/2);
        }

            //Perhaps a view port needs to be made?
                //backBone.setTopAnchor(innerPane, innerPane.getLayoutY());
                //backBone.setLeftAnchor(innerPane, innerPane.getLayoutX());
                //backBone.setTopAnchor(expandableZone, expandableZone.getLayoutY());
                //backBone.setLeftAnchor(expandableZone, expandableZone.getLayoutX());
        
                
        moveInnerH.setBlockIncrement(1);
        moveInnerH.setBlockIncrement(1);
        zoomBar.setBlockIncrement(1);
        moveInnerH.setUnitIncrement(1);
        moveInnerV.setUnitIncrement(1);
        zoomBar.setUnitIncrement(1);

         
        

        //Add circles
        AnchorPane ULCircle =new AnchorPane();
        ULCircle.setMaxHeight(10);
        ULCircle.setMaxWidth(10);
        ULCircle.setScaleX(1);
        ULCircle.setScaleY(1);
        Circle ULdot = new Circle();
        ULdot.setCenterX(5);
        ULdot.setCenterY(5);
        ULdot.setRadius(5);
        ULdot.setFill(Paint.valueOf("blue")); 
        ULCircle.setOpacity(0);
        ULCircle.getChildren().add(ULdot);
        AnchorPane.setTopAnchor(ULCircle, -5.0);
        AnchorPane.setLeftAnchor(ULCircle, -5.0);
        innerPane.getChildren().add(ULCircle);
        ULCircle.setOpacity(0);
        ULCircle.setOnMouseEntered(new EventHandler<Event>() {
            public void handle(Event event){
                System.out.println("In ULCircle"); 
                ULCircle.setOpacity(1);
            }
        });
        ULCircle.setOnMouseExited(new EventHandler<Event>() {
            public void handle(Event event){
                System.out.println("Out ULCircle");
                ULCircle.setOpacity(0);
            }
        }); 
        
         innerPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event){
                innerPane.setMaxHeight(innerPane.getMinHeight());
                innerPane.setMaxWidth(innerPane.getMinWidth());
                System.out.println("Max W, H " + innerPane.getMaxWidth()+" "+innerPane.getMaxWidth());
                System.out.println("getX "+event.getX()+" getY "+ event.getY()); //in object
               // System.out.println("getSceneX "+event.getSceneX()+" getSceneY "+ event.getSceneY()); //in scene
               // System.out.println("getScreenX "+event.getScreenX()+" getScreenY "+ event.getScreenY()); //in stage
                Rectangle tempRect = new Rectangle( event.getX(), event.getY(),10, 10); //cords, dim
                innerPane.getChildren().add(tempRect);
            
            }
        });
        

         //Add listeners.
        moveInnerH.valueProperty().addListener(new ChangeListener<Number>()  { 
            @Override
            public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
                System.out.println("Current Value of moveInnerH: "+moveInnerH.getValue());
                //moveInnerH=round(moveInnerH.getValue(),.05);

                //System.out.println("DecipherH: "+arg0+" "+ arg1+" "+arg2);
                innerPane.setLayoutX(innerPane.getLayoutX()+(arg2.doubleValue()-arg1.doubleValue()));
                expandableZone.setLayoutX(expandableZone.getLayoutX()+(arg2.doubleValue()-arg1.doubleValue()));
                setReport(root, 0);
            } 
            
        });
        moveInnerV.valueProperty().addListener(new ChangeListener<Number>()  { 
            @Override
            public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
                System.out.println("Current Value of moveInnerV: "+moveInnerV.getValue());
                //System.out.println("DecipherV: "+arg0+" "+ arg1+" "+arg2);
                //Node class, double property, old val, new val
                innerPane.setLayoutY(innerPane.getLayoutY()+(arg2.doubleValue()-arg1.doubleValue()));
                expandableZone.setLayoutY(expandableZone.getLayoutY()+(arg2.doubleValue()-arg1.doubleValue()));
                setReport(root, 0);
            } 
            
        });
        //this.resizeScrollableInnerPane();
        
        
        zoomBar.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
                System.out.println("Current Value of ScaleBar: "+zoomBar.getValue());
                zoomBar.setValue(round(zoomBar.getValue(), .5));
                System.out.println("Current Value of ScaleBar: "+zoomBar.getValue());

                //System.out.println("DecipherV: "+arg0+" "+ arg1+" "+arg2);
                //Node class, double property, old val, new val 
                innerPane.setScaleX(zoomBar.getValue()/100);
                innerPane.setScaleY(zoomBar.getValue()/100);
                expandableZone.setScaleX(zoomBar.getValue()/100);
                expandableZone.setScaleY(zoomBar.getValue()/100);
                setReport(root, 0); 
                //in addition to figuring it's new dimension, let us figure out a few things:
                //viewport objects?
                //DimScene - 1/2 new eZ Pane, (now we get the 'leftover' bits of backBone)
                    // From this left over, divide by 2. Figure out based on the scale how much 'over' it's gone, and readjust the 
                    //layout to negatives or positives as needed. 
            } 
        });
        

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
 
         

        //FUTURE: Resize all elements appropraitely after screen size changes.
        stage.widthProperty().addListener((obs, oldVal, newVal) -> { 
            //this.resizeComponentsX();
            //System.out.println(newVal.doubleValue()-oldVal.doubleValue()); 
            this.resizeScrollable(newVal.doubleValue()-oldVal.doubleValue(), 0);  
        });

        stage.heightProperty().addListener((obs, oldVal, newVal) -> { 
            //this.resizeComponentsY();
            //System.out.println(newVal.doubleValue()-oldVal.doubleValue()); 
            this.resizeScrollable(0,newVal.doubleValue()-oldVal.doubleValue());
        }); 
        




    }
     
}
