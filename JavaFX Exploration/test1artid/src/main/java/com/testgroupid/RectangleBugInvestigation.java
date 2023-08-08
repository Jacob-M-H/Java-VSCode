package com.testgroupid;
 

import java.util.NoSuchElementException;
import java.util.Scanner;

import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
//import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
  
 


public class RectangleBugInvestigation extends Application{
     public static void selfInfo(){
        System.out.println("Goal 1 - Create canvas in a window.\nGoal 2 - Resize the canvas in the window.\nGoal 3 - Drag self around the canvas when left clicking and dragging\nGoal 4 - Drag window view over the canvas using scrollbars/middle click isntead of left click if possible toggle\nGoal 5 - Resize the view of the canvas (like zoom out) without 'actually' decreasing the size. For example creating a circle on the screen on click should estimate the prefered position and blip an appropraite size in the canvas.\nGoal 6 - Convention for 'drawing' on the canvas.\nGoal 7 - Click and drag items already placed on the canvas.\nGoal 8 - Track the order of items, in the future layer. Canvas base layer should be white, but the layer itself should be transparent when not selected, and if a lower layer is selected (furthest to the left in a tabView), everything ot the right should be completely transparent drawing and all.\nGoal 9 - Clean code, critique.");
    }
    public static void main(String[] args){
        launch(args); //from Application Parent, sends string to launch method, then start method automatically called.
    }

    //#region
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
        double initScaleEZX=-1;
        double initScaleEZY=-1;
        double prevRatioHBar=0.5;

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
    
        //#regiondouble 
            //Note eZ's info should help derive iP's
                //Actual pixels taken on screen, rather than objects dimeniosn
                double ActualEZW=-1; 
                double ActualEZH=-1;
                //Desired 'actual' coordinate in Parent Anchorpane
                double ActualEZY =-1;
                double ActualEZX=-1;
                //Scales' used for comparison when determining default scale, temporary as far as I'm concerned.
                double eZScaleX=-1;
                double eZScaleY=-1;
                //Default scale, based on dimensions of eZ. Determines both eZ and iP. Or should.
                double eZPriortyScale=-1; //this is the important one.
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


    //ZoomScale stuff
        double IdealScale=-1; //holds the 'ideal' scale, either fits all in screen, or figures out if less than the minPixelZoom as it's entire thing, what scale to set it to.
        double minPixelZoom =10; //Minimum number of pixels to display on a maximum zoom.
        double helper =-1; //helper is to be a container for figuring out dimension stuff, if we only need one of the two.
        double minZoom=-1;
        double maxZoom=-1;

    //#endregion

    private double min(double eZScaleX, double eZScaleY) {
        if (eZScaleX <= eZScaleY){
            return eZScaleX;
        }
        else {
            return eZScaleY;
        }
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
                //System.out.println("LB, UB:"+lowerB+" "+upperB+" Kept: "+kept+" remainder:"+remainder+"\n");

                return kept;

            } 
            else if (rBoundry==0) {
                return Math.round(rMe);//round up or down to nearest .0 value
            } 
            return Double.MAX_VALUE; //to signal bad
        }
        public void addCheckers(AnchorPane checkerMe){ 
            //make 1x1 rectangle nodes,   
            Rectangle temp;  
            for (int row=0; row<checkerMe.getPrefHeight(); row++){ //expect 50 pixels
                for (int col =0; col<checkerMe.getPrefWidth(); col++) {
                    if (row%2 == col%2) {  
                        temp = new Rectangle(col*1, row*1, 1, 1);
                        temp.setFill(Color.FIREBRICK);
                        checkerMe.getChildren().add(temp);  
                    }   
                }    
            }   
        }
        public void addCheckers2(AnchorPane checkerMe){ 
            //make 1x1 rectangle nodes,   
            Rectangle temp;  
            for (int row=0; row<checkerMe.getPrefHeight(); row+=4){ //expect 50 pixels
                for (int col =0; col<checkerMe.getPrefWidth(); col+=5) {
                    if (row%3 ==  col%2  ) {  
                        temp = new Rectangle(col*1, row*1, 1, 1);
                        temp.setFill(Color.BLUE);
                        checkerMe.getChildren().add(temp);  
                    }   
                }    
            } 
        }
        public void removeCheckers(Pane uncheckerMe){
            uncheckerMe.getChildren().clear(); //Naive function, just naming convention suggests use after add checkers.
            //Future, extend rectangel class t oa 'checkers' class, so that I can more easily filter them out of the chidlren.
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
        Label collection[];
        int start = 0; 
        
        try{
            removeReport(container);
        }
        finally{
            System.out.println("Try lever: "+ lever);
            switch(lever){
                case 0: 
                        Label iPWReport =new Label("iPW:"+String.valueOf(innerPane.getMinWidth()));
                        Label iPHReport =new Label("iPH:"+String.valueOf(innerPane.getMinHeight()));
                        Label iPSXReport =new Label("iPSX:"+String.valueOf(innerPane.getScaleX()));
                        Label iPSYReport =new Label("iPSY:"+String.valueOf(innerPane.getScaleY()));  
                        Label iPXLay =new Label("IPXLay:"+String.valueOf(innerPane.getLayoutX()));
                        Label iPYLay =new Label("IPYLay:"+String.valueOf(innerPane.getLayoutY()));
                         
                        collection = new Label[] {iPHReport, iPWReport,iPSXReport, iPSYReport, 
                        iPXLay, iPYLay};

                        for (Label x : collection){
                            Report.getChildren().add(x);
                            x.setTextFill(Paint.valueOf("Orange"));
                            x.setLayoutY(start);
                            start+=10;
                            } 
                        break;
                    
                case 1: 
                        Label eZWReport =new Label("eZW:"+String.valueOf(expandableZone.getMinWidth()));
                        Label eZHReport =new Label("eZH:"+String.valueOf(expandableZone.getMinHeight()));
                        Label eZSXReport =new Label("eZSX:"+String.valueOf(expandableZone.getScaleX()));
                        Label eZSYReport =new Label("eZSY:"+String.valueOf(expandableZone.getScaleY()));  
                        Label eZXLay =new Label("eZXLay:"+String.valueOf(expandableZone.getLayoutX()));
                        Label eZYLay =new Label("eZYLay:"+String.valueOf(expandableZone.getLayoutY()));
                
                        double ActualEZW=(expandableZone.getScaleX()*expandableZone.getMinWidth());
                        double ActualEZX = (initialSceneWidth - ActualEZW)/2;  //420-840/2=-210?
                        Label eZActualW=new Label("eZAW: "+String.valueOf(ActualEZW));
                        Label eZActualX=new Label("eZAX: "+String.valueOf(ActualEZX));
                         
                        
                        collection = new Label[] {eZHReport, eZWReport,eZSXReport, eZSYReport, 
                        eZXLay, eZYLay,
                        eZActualW, eZActualX};

                        for (Label x : collection){
                            Report.getChildren().add(x);
                            x.setTextFill(Paint.valueOf("Orange"));
                            x.setLayoutY(start);
                            start+=10;
                            } 
                        break;
                
            } 

            container.getChildren().add(Report);
        }
    }
//TODO: Just use getbounds in parent, 
    public double figureActualDim(Pane panel, String dim){
        if (dim=="Width" || dim=="wid" ||dim=="W" || dim=="w") {
            //System.out.println("Old Method (getMinWidth): "+(panel.getMinWidth()*panel.getScaleX()));
            //System.out.println("Old Method (getWidth): "+(panel.getWidth()*panel.getScaleX()));
            //System.out.println("New Method: "+ panel.getBoundsInParent().getWidth());
            return panel.getPrefWidth()*panel.getScaleX();
        } else if (dim=="Height" || dim=="hei" || dim =="H" || dim=="h"){
            return panel.getPrefHeight()*panel.getScaleY();
        }
        System.out.println("WARNING: Misused function figureActualDim Panel:"+ panel.toString() + " "+dim);
        return -1; //an impossible value, as we cannot have negative scale or dimensions.
    }
    public double figureActualCord(Pane panel, String cord) {
        if (cord=="X" || cord=="x") {

        } else if (cord=="Y" || cord =="y"){

        }
        System.out.println("WARNING: Misused function figureActualCord Panel:"+panel.toString()+" "+cord);
        return Double.MAX_VALUE; //To signal somethings gone wrong
    }
 
        //I beleive the scene is resized with the stage.  
    //BUG: Pref is not enough! Not sure why
    //TODO: change these to bindings, though depending on the extent of the GUI, might be best to leave them as functions
    //TODO: change the setting of width and height to be more succinct, min/max/prefDIM isn't what I want to keep. 
    public void resizeBackBoneX(double width){
            this.backBone.setPrefWidth(width); 
            this.backBone.setMinHeight(width); //<=became quite choppy, should research more on the difference between these.
            this.backBone.setMaxHeight(width);
        }
    public void resizeBackBoneY(double height){
            this.backBone.setPrefHeight(height); 
            this.backBone.setMinHeight(height);
            this.backBone.setMaxHeight(height); 
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
            expandableZone.setMinWidth(width);
            expandableZone.setMaxWidth(width);
            expandableZone.setPrefWidth(width);
        }
    public void resizeExpandableZoneY(double height){
            expandableZone.setMinHeight(height); 
            expandableZone.setMaxHeight(height); 
            expandableZone.setPrefHeight(height); 
        }
    public void resizeMoveInnerH(double width, double yCord){ 
            this.moveInnerH.setPrefHeight(scrollbarThick);
            this.moveInnerH.setMinHeight(scrollbarThick); 
            this.moveInnerH.setMaxHeight(scrollbarThick);
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

    
     
    public void reboundMoveInnerH(double RatioScale){
        System.out.println("reboundMoveInnerH Triggered");
                double newHBarMax =.5*expandableZone.getBoundsInParent().getWidth() - (.5*expandableZone.getWidth()*zoomBar.getMin()); //subtract the initial width due to initial scale. initial zoom is minZoom.
                double newHBarMin = -newHBarMax; //value is defaulted 0, for now we're just using this to move the layout a certain amount
                double newHBarVal=0; //just a default to hold.

                //More sensible container than zoom listener
                if (RatioScale==0){
                    moveInnerH.setValue(0);
                    moveInnerH.setMax(0);
                    moveInnerH.setMin(0);
                    moveInnerH.setVisible(false);
                } else{
                    moveInnerH.setVisible(true);
                }

                if ((moveInnerH.getValue()<=moveInnerH.getMax()-.5*eZbufferX ) && (moveInnerH.getValue()>=moveInnerH.getMin()+.5*eZbufferX )){ //NO PINK, buffer hidden. 
                    newHBarVal=moveInnerH.getValue()*(RatioScale); //ratio between old and new scales.
                }
                else { //LEFT PINK, buffer in play.  
                    if (moveInnerH.getValue()<0) {  
                        //newHBarVal = newHBarMin - (moveInnerH.getValue()-moveInnerH.getMin()) ; //newMin - oldAmount over,
                        if (RatioScale<=1){ //zoom out 
                           //Take the old amount over, multiply by the scale ratio, add to the the min (wihtout buffer accounted)
                            newHBarVal=newHBarMin+((moveInnerH.getValue()-moveInnerH.getMin()-.5*eZbufferX)*RatioScale);   
                        } else{ //zoom in 
                            //The idea is to get as close to the center of the shape as I can slowly.  Ratio scale is greater than 1, so invert it and we get the amount to 'shrink' it by.
                            newHBarVal=newHBarMin+((moveInnerH.getValue()-moveInnerH.getMin()-.5*eZbufferX)*(Math.pow(RatioScale, -1))); 
                        } 
                    }
                    else if (moveInnerH.getValue()>0) { //RIGHT PINK (If toggle is set to -1 (Sider moves with the panel)) 
                        //newHBarVal=newHBarMax + (moveInnerH.getMax() - moveInnerH.getValue());
                        if (RatioScale<=1) { //zoom out  
                            newHBarVal=newHBarMax + ((moveInnerH.getValue()-moveInnerH.getMax()+.5*eZbufferX)*RatioScale);  
                        } else { //zoom in 
                            newHBarVal=newHBarMax + ((moveInnerH.getValue()-moveInnerH.getMax()+.5*eZbufferX)*(Math.pow(RatioScale,-1)));  
                        }

                    }
                    else { 
                        newHBarVal=0; //shouldn't happen but might?
                    }
                }
                
                newHBarMax=newHBarMax+.5*eZbufferX; //acount for buffer, easier to think of it figuring distances.
                newHBarMin=-newHBarMax;
                moveInnerH.setValue(0); //just sowe don't have any expansion or strangeness, completley unncessary I believe.
                moveInnerH.setMax(newHBarMax);
                moveInnerH.setMin(newHBarMin);
                moveInnerH.setValue(newHBarVal); 
        System.out.println("reboundMoveInnerH Over");
    }
    public void reboundMoveInnerV(double RatioScale){
        System.out.println("reboundMoveInnerV Triggered");
                double newVBarMax =.5*expandableZone.getBoundsInParent().getHeight() - (.5*expandableZone.getHeight()*zoomBar.getMin()); //subtract the initial width due to initial scale. initial zoom is minZoom.
                double newVBarMin = -newVBarMax; //value is defaulted 0, for now we're just using this to move the layout a certain amount
                double newVBarVal=0; //just a default to hold.

                //More sensible container than zoom listener
                if (RatioScale==0){
                    moveInnerV.setValue(0);
                    moveInnerV.setMax(0);
                    moveInnerV.setMin(0);
                    moveInnerV.setVisible(false);
                } else{
                    moveInnerV.setVisible(true);
                }

                if ((moveInnerV.getValue()<=moveInnerV.getMax()-.5*eZbufferX ) && (moveInnerV.getValue()>=moveInnerV.getMin()+.5*eZbufferX )){ //NO PINK, buffer hidden. 
                    newVBarVal=moveInnerV.getValue()*(RatioScale); //ratio between old and new scales.
                }
                else { //UP PINK, buffer in play.  
                    if (moveInnerV.getValue()<0) {  
                        //newVBarVal = newVBarMin - (moveInnerV.getValue()-moveInnerV.getMin()) ; //newMin - oldAmount over,
                        if (RatioScale<=1){ //zoom out 
                           //Take the old amount over, multiply by the scale ratio, add to the the min (wihtout buffer accounted)
                            newVBarVal=newVBarMin+((moveInnerV.getValue()-moveInnerV.getMin()-.5*eZbufferX)*RatioScale);   
                        } else{ //zoom in 
                            //The idea is to get as close to the center of the shape as I can slowly.  Ratio scale is greater than 1, so invert it and we get the amount to 'shrink' it by.
                            newVBarVal=newVBarMin+((moveInnerV.getValue()-moveInnerV.getMin()-.5*eZbufferX)*(Math.pow(RatioScale, -1))); 
                        } 
                    }
                    else if (moveInnerV.getValue()>0) { //DOWN PINK (If toggle is set to -1 (Sider moves with the panel)) 
                        //newVBarVal=newVBarMax + (moveInnerV.getMax() - moveInnerV.getValue());
                        if (RatioScale<=1) { //zoom out  
                            newVBarVal=newVBarMax + ((moveInnerV.getValue()-moveInnerV.getMax()+.5*eZbufferX)*RatioScale);  
                        } else { //zoom in 
                            newVBarVal=newVBarMax + ((moveInnerV.getValue()-moveInnerV.getMax()+.5*eZbufferX)*(Math.pow(RatioScale,-1)));  
                        }

                    }
                    else { 
                        newVBarVal=0; //shouldn't happen but might?
                    }
                }
                
                newVBarMax=newVBarMax+.5*eZbufferX; //acount for buffer, easier to think of it figuring distances.
                newVBarMin=-newVBarMax;
                moveInnerV.setValue(0); //just sowe don't have any expansion or strangeness, completley unncessary I believe.
                moveInnerV.setMax(newVBarMax);
                moveInnerV.setMin(newVBarMin);
                moveInnerV.setValue(newVBarVal); 
        System.out.println("reboundmoveInnerV Over");
    }
  
public void setScrollable(double refW, double refH){  
        this.backBone.setVisible(true);
        this.backBone.setCursor(Cursor.HAND);
        this.backBone.setSnapToPixel(false); //NOTE Important otherwise expandableZone/iP would get weird 'null spaces' which appear to exist but are undrawable, and outline the thing.
        backBone.setBackground(backBoneBackground); 
        this.innerPane.setVisible(true);      
        this.expandableZone.setVisible(true);
       //NOTE this.expandableZone.setSnapToPixel(false); //This isn't super important just yet... Maybe it will be in the future but for now it's not. 

        expandableZone.setBackground(expandableZoneBackground);
        innerPane.setBackground(innerPaneBackground);     
        this.moveInnerV.setOrientation(Orientation.VERTICAL);
        this.moveInnerH.setOrientation(Orientation.HORIZONTAL);    

        //Layout stuff -backbone subject to change with the ifguring of scales
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
             
               
    }

     
    public void figureInitialScale(){
        //TODO: NOTE getWidth and getHeight cannot be used on eZ, returns 0. Thus in the future instead of 'min' we should reference a double value 'initialEZW', or something like that.
        //InitialScale has entire picture in frame, thus H/V bar's are hidden, and their values are set to 0, both min, max, and value.
        
        //H/VBars set by scale.
            moveInnerH.setValue(0);
            moveInnerH.setMax(0);
            moveInnerH.setMin(0);
            moveInnerH.setVisible(false);
            moveInnerV.setValue(0);
            moveInnerV.setMax(0);
            moveInnerV.setMin(0);
            moveInnerV.setVisible(false);
        //Protocol
            //Figure out zoom min and max
                //These are set as such default, so check back when cleaning up or allowing user specifications.
                IdealScale=-1; 
                minPixelZoom =10; //Minimum number of pixels to display on a maximum zoom.

                //This is the telescopic ratio for the pixels. Thus the the ACTUAL minPixelZoom we want is based around having 100 pixels per pixel
                    helper =Math.min(backBone.getWidth(), backBone.getHeight()); // CHECK: Make sure these are returning nonzeros to start.
                    //System.out.println("CHECK 0 !="+helper);
                    //Smaller diemsnion means less maginificaito nrequired. However we also need to get the dimension of Expandable zone and figure out hte ratio between the two
                    helper =Math.min(backBone.getWidth()/expandableZone.getWidth(), backBone.getHeight()/expandableZone.getHeight());
                    //System.out.println("CHECK 0 !="+helper);


                helper =-1; //helper is to be a container for figuring out dimension stuff, if we only need one of the two.
                minZoom=-1;
                maxZoom=-1;
            //Helper requires least amount of scale to get up to or down to 10 pixels. 
                helper=Math.min(expandableZone.getMinWidth(),expandableZone.getMinHeight());
                
       
            //'scale up' or 'scale down'
                //If either W/H are not perfect
                if (expandableZone.getMinWidth()!=initialSceneWidth-eZtoAnchorX ||
                expandableZone.getMinHeight()!=initialSceneHeight-eZtoAnchorY){  //Edit: eZ compared to 'ideal box' - May have to 'scale up'
                    
                    //First figure out scale  
                        eZScaleX=(initialSceneWidth-eZtoAnchorX)/expandableZone.getMinWidth();
                        eZScaleY=(initialSceneHeight-eZtoAnchorY)/expandableZone.getMinHeight(); 
                        //Prioritize getting all in frame 
                        eZPriortyScale=Math.min(eZScaleX,eZScaleY);  
                        //TODO: Check if eZPrioritScale is 1, break after setting required things?
                        expandableZone.setScaleX(eZPriortyScale);
                        expandableZone.setScaleY(eZPriortyScale); 
                        
                         
                        ActualEZW=figureActualDim(expandableZone, "w"); 
                        ActualEZH=figureActualDim(expandableZone, "h");
                         //-ASSERT: Parent of these panels is scaled 1. unclear what happens if not the case. 
                        //eZPriority scale is the scale required to fit the entire image in scene by default.

                //Main DEAL: Find the layout X and Y that makes the zoom centered on the window. 
                    if (expandableZone.getMinWidth()<=initialSceneWidth && expandableZone.getMinHeight() <=initialSceneHeight) {
                    //if Smaller than backBone 
                        //account for buffer, then get difference from 'ideal box' of ScreenDim-buffer,  and the actual shape. 
                            //Now figure actual width and height based on scale: 
                            ActualEZY =initialSceneHeight-eZtoAnchorY-expandableZone.getMinHeight(); //Different from if it was larger than the screen!
                            ActualEZX =initialSceneWidth-eZtoAnchorX-expandableZone.getMinWidth();  
                            expandableZone.setLayoutY((ActualEZY+eZtoAnchorY)/2);
                            expandableZone.setLayoutX((ActualEZX+eZtoAnchorX)/2); 
                    }
                    else {
                    //if Larger than backbone (1 dim at least)  
                        //Now figure actual width and height based on scale:  
                        ActualEZX=(expandableZone.getMinWidth()-ActualEZW); //The space taken up discrpency, 'excess' space left by the zoom out, or receeded space.
                        ActualEZY=(expandableZone.getMinHeight()-ActualEZH); //The space taken up discrpency, 'excess' space left by the zoom out, or receeded space. 
                        expandableZone.setLayoutY((eZtoAnchorY -ActualEZY)/2);//ActualEZ[Cord] is how far 'forward' the value is from where it needs to be. thus everything else I think follows.
                        expandableZone.setLayoutX((eZtoAnchorX -ActualEZX)/2);  
                    }
                } 
                //Both H/W are perfect for the scene and buffer.
                else{//EDIT: Else scale=1?, no else statement was provided. 
                    eZPriortyScale=1; //The fit is perfect, thus the scale should be 1.
                    expandableZone.setScaleX(1);
                    expandableZone.setScaleY(1);
                    //set other helper variables.
                    ActualEZX=eZtoAnchorX/2;
                    ActualEZY=eZtoAnchorY/2; 
                    expandableZone.setLayoutX(ActualEZX);
                    expandableZone.setLayoutY(ActualEZY);  
                }
        
            //Min and Max zoom figure out. 
                //Note: Max zoom shouldn't account for buffer, as area should taken w/ 10 pixels if allowed.
                //eZpriroity is basically min zoom. but possibly also max zoom.
        //Close version: maxZoom=minPixelZoom*Math.min(initialSceneHeight, initialSceneWidth)/Math.max(expandableZone.getMinWidth(), expandableZone.getMinHeight()); //display at max zoom at least 10 pixels. 
    //maxZoom=eZPriortyScale*minPixelZoom; //Basically the scaled up version of the eZ, and initialDim cancel out, so we get ?=minzoomPixels.
        //This is equivalent ot what I had before, but let's try this:
        maxZoom=initialSceneWidth/minPixelZoom;//*eZPriortyScale; //could it be 1/2 
        //EDIT^ take min or max dimension? Simplest calcualtion seems to tke from the origin. 
        //could the buffer be changed.
        
        IdealScale=-1; 
        minZoom=eZPriortyScale;//scales should be the same.
        if (helper/minPixelZoom <1) { //Maxzoom requires providing 10 pixels we don't have. 
        //Ideal Scale if we had our minPixelZoom fully.
        IdealScale =Math.max(initialSceneWidth/minPixelZoom,initialSceneHeight/minPixelZoom);
        //now this is the scale to set the dimensions smaller than our minPixelZoom

            maxZoom=IdealScale;  //Just show what pixels you do have.
            minZoom=IdealScale;  //Can't zoom out, everything is alerady on display.
            expandableZone.setScaleX(IdealScale);
            expandableZone.setScaleY(IdealScale);
        } 
        zoomBar.setMax(maxZoom);
        zoomBar.setMin(minZoom);
        zoomBar.setValue(minZoom); //default zoomed out the best we can.
        //ASSERT: Min Window size will always accomodate minPixelZoom?
        //ASSERT: Square windows allowed only right now, otherwise minPixelZoom must account for the ratio.
        //NOTE: Instead of initial scenewidht and height, might have to use some min or maxing potentially. Later tests required.
        
         
        zoomBar.setUnitIncrement(.1); 
        //TODO: Figure out a proper zoomBar increment. Should be based on the dimensions of the frame, minPixelZoom, and the Actualdimensions of the expandablePane. 


            System.out.println("BackBone: "+backBone.getBoundsInLocal()+"\n" + 
                            "Scene: "+backBone.getParent().getBoundsInLocal().getWidth()+
                                 ", "+backBone.getParent().getBoundsInLocal().getHeight() +"\n" + 
                            "BackBone deets: "+ backBone.getLayoutX()+", "+backBone.getLayoutY() +", "+backBone.getWidth()+", " +backBone.getHeight());
          
    }







    //These values are will be used in reference to centering and scaling each of hte panes. 
            //we assume a centered shape is desired
            double idealEzBuffer = 20;
            double idealIpBuffer =30;  //ASSERT Ip bffer always larger than Ez buffer.
            double ActualIPX =-1;
            double ActualIPY =-1;
            double initScaleIPX=-1; //the coordinate of the min scaled innerPane. 
            double initScaleIPY=-1;
            double XIPBuffer=-1; //In the event one of these is not the ideal, these are set to the actual.
            double YIPBuffer=-1;
    public void figInitialScale2(){
        System.out.println("Scale2 Triggered:");
        //TODO: NOTE getWidth and getHeight cannot be used on eZ, returns 0. Thus in the future instead of 'min' we should reference a double value 'initialEZW', or something like that.
        //InitialScale has entire picture in frame, thus H/V bar's are hidden, and their values are set to 0, both min, max, and value.
        
        //#region
            //H/VBars set by scale.
                moveInnerH.setValue(0);
                moveInnerH.setMax(0);
                moveInnerH.setMin(0);
                moveInnerH.setVisible(false);
                moveInnerV.setValue(0);
                moveInnerV.setMax(0);
                moveInnerV.setMin(0);
                moveInnerV.setVisible(false);
            //Protocol
                //Figure out zoom min and max
                    //These are set as such default, so check back when cleaning up or allowing user specifications.
                    IdealScale=-1; 
                    minPixelZoom =10; //Minimum number of pixels to display on a maximum zoom.


            //NOTE: PrefWidth must be used/minWidth due to how JavaFX Works whe nfirst making the scene. - original set scales used minDims. I'll tyr using PrefDims. 
            
                    //This is the telescopic ratio for the pixels. Thus the the ACTUAL minPixelZoom we want is based around having 100 pixels per pixel
                        helper =Math.min(backBone.getPrefWidth(), backBone.getPrefHeight()); // CHECK: Make sure these are returning nonzeros to start.
                            //System.out.println("CHECK 0 !="+helper);
                        //Smaller diemsnion means less maginificaito nrequired. However we also need to get the dimension of Expandable zone and figure out hte ratio between the two
                        helper =Math.min(backBone.getPrefWidth()/expandableZone.getPrefWidth(), backBone.getPrefHeight()/expandableZone.getPrefHeight());
                            //System.out.println("CHECK 0 !="+helper); 
                        helper =-1; //helper is to be a container for figuring out dimension stuff, if we only need one of the two.
                        minZoom=-1;
                        maxZoom=-1; 
                    //ASSERT - we know the expected minPixelZoom, i.e. the minimum number of pixels to show.
             
        //#endregion 

            //If either W/H are not perfect
            if (innerPane.getPrefWidth()!=backBone.getPrefWidth()-idealIpBuffer ||
            innerPane.getPrefHeight()!=backBone.getPrefHeight()-idealIpBuffer){  //Edit: eZ compared to 'ideal box' - May have to 'scale up'
                
                //First figure out scale   
                    eZScaleX=(backBone.getPrefWidth()-idealIpBuffer)/innerPane.getPrefWidth();
                    eZScaleY=(backBone.getPrefHeight()-idealIpBuffer)/innerPane.getPrefHeight(); 
                    //Prioritize getting all in frame 
                    eZPriortyScale=Math.min(eZScaleX,eZScaleY);  
                    //TODO: Check if eZPrioritScale is 1, break after setting required things?
                    innerPane.setScaleX(eZPriortyScale);
                    innerPane.setScaleY(eZPriortyScale);    
                    ActualEZW=figureActualDim(innerPane, "w"); 
                    ActualEZH=figureActualDim(innerPane, "h");
                        //-ASSERT: Parent of these panels is scaled 1. unclear what happens if not the case. 
                    //eZPriority scale is the scale required to fit the entire image in scene by default. 
            //Main DEAL: Find the layout X and Y that makes the zoom centered on the window.    
                //This avoids needing to figure out zoom in or out default! 
                //This works without having to choose minimal or whatever I was thinking before!
                ActualIPX = (((innerPane.getPrefWidth()*innerPane.getScaleX()) - innerPane.getPrefWidth())/2) +  
                            ((backBone.getPrefWidth()-(innerPane.getPrefWidth()*innerPane.getScaleX() ))/2); 
                ActualIPY = (((innerPane.getPrefHeight()*innerPane.getScaleY()) - innerPane.getPrefHeight())/2) +  
                            ((backBone.getPrefHeight()-(innerPane.getPrefHeight()*innerPane.getScaleY() ))/2); 
                innerPane.setLayoutY(ActualIPY); 
                innerPane.setLayoutX(ActualIPX);  
            } //Both H/W are perfect for the scene and buffer.
            else{ //TODO: Test perfect fit!
                eZPriortyScale=1; //The fit is perfect, thus the scale should be 1.
                innerPane.setScaleX(1);
                innerPane.setScaleY(1);
                //set other helper variables.
                ActualIPX=idealIpBuffer/2;
                ActualIPY=idealIpBuffer/2; 
                innerPane.setLayoutX(ActualIPX);
                innerPane.setLayoutY(ActualIPY);  
            }
                
            IdealScale=-1; 
            minZoom=eZPriortyScale;//scales should be the same.
            maxZoom=Math.min(backBone.getPrefWidth(),backBone.getPrefHeight())/minPixelZoom;  
                
            if (innerPane.getPrefWidth()<=minPixelZoom && innerPane.getPrefHeight()<=minPixelZoom) { //Zoom
                //Also, in the case that H/V Bar's are not enabled, the max zoom and min zoom might not be sensible anyways. 
                System.out.println("eZ<minPixelSquare");
                zoomBar.setMax(minZoom); //default zoomed in all the way
                zoomBar.setMin(maxZoom);  //zoom out should set it to it's regular 
                //TODO: Flip orientation (so the default slider is always on the appropraite side)
                //TODO: MoveV/H hidden, reset.
                //TODO: make a listener when expandable/innerpane chagnes to flip the zoomabrs oreintation with this call? And then just always set min ot min and max to max?
                zoomBar.setVisible(true);
                
            } else {
                if (maxZoom>1 && minZoom<maxZoom) { //Normal zoom behavior 
                    zoomBar.setMax(maxZoom); 
                    zoomBar.setMin(minZoom);
                    zoomBar.setVisible(true); 
                } else { 
                    zoomBar.setVisible(false); //shouldn't occur
                }
            }    

            //TODO: Set value to min,
            //TODO: probably don't need expandablezoomDim<minPixelZoom square wise, handled below with minZoom's calculation check.

        //Testing here is the values obtained. Max zoom should usually stay aroudn 42 even with odd dimensions, minzoom should be somewhat close to an approximation
            //zoombar set min and max, It should never need to be reversed, I think, unless it's smaller than our min pixels allowed. 
            //TODO: Base max zoom off of ExpandableZones minimal dimensions, primarily it's smallest dimensions, and minPixelZoom (so 10 pixels in a 410x420 means 410/10 max zoom, consistent for however backbone gets resized.)
            //TODO: Assert backbone never get's scaled.  
            if (minZoom>maxZoom) {//minPixelZoom>Ez squarewise
                minZoom=1;
                innerPane.setScaleX(maxZoom);
                innerPane.setScaleY(maxZoom);
                ActualIPX = (((innerPane.getPrefWidth()*innerPane.getScaleX()) - innerPane.getPrefWidth())/2) +  
                            ((backBone.getPrefWidth()-(innerPane.getPrefWidth()*innerPane.getScaleX() ))/2); 
                ActualIPY = (((innerPane.getPrefHeight()*innerPane.getScaleY()) - innerPane.getPrefHeight())/2) +  
                            ((backBone.getPrefHeight()-(innerPane.getPrefHeight()*innerPane.getScaleY() ))/2); 
                innerPane.setLayoutY(ActualIPY); 
                innerPane.setLayoutX(ActualIPX); 
                zoomBar.setMax(maxZoom);
                zoomBar.setMin(minZoom);
                //TODO: Default set value to max
                System.out.println("adjsuted for eZ<MinPixelZoom - max/min zooms: "+maxZoom+", "+minZoom);
                System.out.println("adjsuted for eZ<MinPixelZoom - bar min,max: "+zoomBar.getMin()+", "+zoomBar.getMax());  
            }
            //NOTE: This might need to be set to the 'current zoom' given the smaller than MinPixel of IP. Need to test when ZoomBar is working. 

            initScaleIPX=ActualIPX;//innerPane.getLayoutX(); <-returns -1?!
            initScaleIPY=ActualIPY;//innerPane.getLayoutY(); 
            //TODO: Change this to a bool flag for performance. 
                //So if zoomBar is set to the same value as it was previously, it doesn't run it's listener for set value. Thus we will force it by asserting ZoomBar is never '0'.
                    //Thus we set to 0 then set to minZoom.
            zoomBar.setValue(0);
            zoomBar.setValue(minZoom); //NOTE: Be sure to trigger this after initScale's are set!
            //ExpandablePane resizes everytime zoom changes, or the dimension changes of IP. thus ASSERT: EZ is remade on either 'IP dim changes' OR 'Zoom change', never both atthe same time. 
                 //XIPBuffer = backBone.getPrefWidth()-innerPane.getPrefWidth()*innerPane.getScaleX(); //unused. Perhaps later if needed.
                    //YIPBuffer = backBone.getPrefHeight()-innerPane.getPrefHeight()*innerPane.getScaleY(); 
    } 

    

    public void figureExpandableZoneBasedOnIP(){
        //Assume ActualIPX, ActualIPY set
        //called when IP dim changes.
        //Want EZbuffer-IPBuffer for distance away from IP desired.
        //We'll set the layout to be the same, however  
        //DO NOT SCALE EZ, avoid as it will have UI components for dragging expand IP. 
        
        initScaleEZX=(innerPane.getLayoutX()-((innerPane.getScaleX()*innerPane.getPrefWidth()-innerPane.getPrefWidth())/2))-((idealIpBuffer-idealEzBuffer)/2) - (-1*moveInnerH.getValue());
        initScaleEZY=( innerPane.getLayoutY() - ( ( innerPane.getScaleY()*innerPane.getPrefHeight() - innerPane.getPrefHeight() )/2 ) ) - ((idealIpBuffer-idealEzBuffer)/2) -(-1*moveInnerV.getValue());
        expandableZone.setLayoutX( initScaleEZX+ (-1*moveInnerH.getValue()) );
        expandableZone.setLayoutY(initScaleEZY +(-1*moveInnerV.getValue())  ); 

        resizeExpandableZoneX((innerPane.getPrefWidth()*innerPane.getScaleX())+((idealIpBuffer-idealEzBuffer))); 
        resizeExpandableZoneY((innerPane.getPrefHeight()*innerPane.getScaleY())+((idealIpBuffer-idealEzBuffer)));  
        System.out.println("innerPane true width : "+(innerPane.getPrefWidth()*innerPane.getScaleX()));
        System.out.println("ExpandablePane expectation width "+((innerPane.getPrefWidth()*innerPane.getScaleX())+((idealIpBuffer-idealEzBuffer))) );
    } 




    
                double compensation=(idealIpBuffer-idealEzBuffer);//.5*(idealIpBuffer-idealEzBuffer);//tired of having to change 8 spots.
public void reboundMoveInnerH2(double RatioScale){
        System.out.println("reboundMoveInnerH2 Triggered"); 
                //double compensation=.5*(idealIpBuffer-idealEzBuffer);//tired of having to change 8 spots.
                //double newHBarMax =.5*innerPane.getPrefWidth()*innerPane.getScaleX() - (.5*innerPane.getPrefWidth()*zoomBar.getMin());// removed: + (.5*(idealIpBuffer-idealEzBuffer)); to keep in mind with the original function. //subtract the initial width due to initial scale. initial zoom is minZoom.
                double newHBarMax =.5*innerPane.getPrefWidth()*innerPane.getScaleX() - ( .5*backBone.getWidth() - compensation );// removed: + (.5*(idealIpBuffer-idealEzBuffer)); to keep in mind with the original function. //subtract the initial width due to initial scale. initial zoom is minZoom.
                double newHBarMin = -newHBarMax; //value is defaulted 0, for now we're just using this to move the layout a certain amount
                double newHBarVal=0; //just a default to hold.

                System.out.println("Max H bar, Min H bar ("+newHBarMax+", "+newHBarMin+")");

                //More sensible container than zoom listener
                if (RatioScale==0){
                    moveInnerH.setValue(0);
                    moveInnerH.setMax(0);
                    moveInnerH.setMin(0);
                    moveInnerH.setVisible(false);
                } else{
                    moveInnerH.setVisible(true);
                }
                 

                System.out.println("Ez+Buffer diff="+expandableZone.getPrefWidth()+"+"+(idealIpBuffer-idealEzBuffer)+", backBone ="+backBone.getPrefWidth());
                if (expandableZone.getPrefWidth() + (idealIpBuffer-idealEzBuffer) >= backBone.getPrefWidth()){ //only show the HBar if the pink is no longer an issue. Essentially if it fits in the ideal buffer range then we can show it.
                    System.out.print("Either exceeds BB, or within 20 pixels of BB [] ");    
                    //in parent substitutions - old position which will decide how it figures out its new HBar position adjustment.
                    double iPMinX =ActualIPX - 0.5*(innerPane.getPrefWidth()*innerPane.getScaleX()-innerPane.getPrefWidth()) + (-1*moveInnerH.getValue()); //NOTE IF it is zoomed up instead of down for initial, does this change the polarity correclty? [might end up with wonky numbers otherwise]
                    System.out.println("ActualIpX : "+ActualIPX+", PrefWidth : "+innerPane.getPrefWidth()+", scale "+innerPane.getScaleX());
                    double iPMaxX=iPMinX+innerPane.getPrefWidth()*innerPane.getScaleX();
                        System.out.print("iPMinX : "+iPMinX+", backBone PrefWidth "+backBone.getPrefWidth()+" iPMax: "+iPMaxX+" [] ");
                        //When max zoomed out, what are we dealing with.  

                    //No pink
                    if (iPMinX<=0 && iPMaxX>=backBone.getPrefWidth()){ 
                        System.out.print("No pink shown [] ");
                        newHBarVal=moveInnerH.getValue()*(RatioScale); //ratio between old and new scales.
                    }
                    //Pink 
                    else { //LEFT PINK, buffer in play.  
                        System.out.println("Pink shown [] ");
                        if (moveInnerH.getValue()<0) {  
                            System.out.print("Left pink shown [] ");
                            if (RatioScale<=1){ //zoom out 
                            //Take the old amount over, multiply by the scale ratio, add to the the min (wihtout buffer accounted)
                                System.out.print("zoom out [] ");
                                newHBarVal=newHBarMin+((moveInnerH.getValue()-moveInnerH.getMin()-compensation)*RatioScale);   
                                System.out.println("newHBarVal = "+newHBarVal+", old value: "+ moveInnerH.getValue()+", moveInnerGetMin: "+moveInnerH.getMin()+", IdealEZbuffer: "+idealEzBuffer+", ratio scale: "+RatioScale);
                            } else{ //zoom in 
                                System.out.print("zoom in [] ");
                                //The idea is to get as close to the center of the shape as I can slowly.  Ratio scale is greater than 1, so invert it and we get the amount to 'shrink' it by.
                                newHBarVal=newHBarMin+((moveInnerH.getValue()-moveInnerH.getMin()-compensation)*(Math.pow(RatioScale, -1))); 
                            } 
                        }
                        else if (moveInnerH.getValue()>0) { //RIGHT PINK (If toggle is set to -1 (Sider moves with the panel)) 
                            System.out.print("Right pink shown [] ");
                            
                            if (RatioScale<=1) { //zoom out  
                                System.out.print("zoom out [] ");
                                newHBarVal=newHBarMax + ((moveInnerH.getValue()-moveInnerH.getMax()+compensation)*RatioScale);  
                            } else { //zoom in 
                                System.out.print("zoom in [] ");
                                newHBarVal=newHBarMax + ((moveInnerH.getValue()-moveInnerH.getMax()+compensation)*(Math.pow(RatioScale,-1)));  
                            }

                        }
                        else { 
                            System.out.print("static [] ");
                            newHBarVal=0; //shouldn't happen but might?
                        }
                    } 
                    newHBarMax=newHBarMax+compensation; //acount for buffer, easier to think of it figuring distances.
                    newHBarMin=-newHBarMax;
                    moveInnerH.setValue(0); //just sowe don't have any expansion or strangeness, completley unncessary I believe.
                    moveInnerH.setMax(newHBarMax);
                    moveInnerH.setMin(newHBarMin);
                    moveInnerH.setValue(newHBarVal); 

                } 
                else { 

                    moveInnerH.setValue(0);
                    moveInnerH.setMax(0);
                    moveInnerH.setMin(0);
                    moveInnerH.setVisible(false);
                }

 
             
        System.out.println("reboundMoveInnerH2 Over");
    }
public void reboundMoveInnerV2(double RatioScale){
        System.out.println("reboundMoveInnerV2 Triggered"); 
                //double newHBarMax =.5*innerPane.getPrefWidth()*innerPane.getScaleX() - (.5*innerPane.getPrefWidth()*zoomBar.getMin());// removed: + (.5*(idealIpBuffer-idealEzBuffer)); to keep in mind with the original function. //subtract the initial width due to initial scale. initial zoom is minZoom.
                double newVBarMax =.5*innerPane.getPrefHeight()*innerPane.getScaleY() - ( .5*backBone.getHeight() - compensation ); //subtract the initial width due to initial scale. initial zoom is minZoom.
                double newVBarMin = -newVBarMax; //value is defaulted 0, for now we're just using this to move the layout a certain amount
                double newVBarVal=0; //just a default to hold.

                System.out.println("Max V bar, Min V bar ("+newVBarMax+", "+newVBarMin+")");

                //More sensible container than zoom listener
                if (RatioScale==0){
                    moveInnerV.setValue(0);
                    moveInnerV.setMax(0);
                    moveInnerV.setMin(0);
                    moveInnerV.setVisible(false);
                } else{
                    moveInnerV.setVisible(true);
                }
                 

                System.out.println("Ez+Buffer diff="+expandableZone.getPrefHeight()+"+"+(idealIpBuffer-idealEzBuffer)+", backBone ="+backBone.getPrefHeight());
                if (expandableZone.getPrefHeight() + (idealIpBuffer-idealEzBuffer) >= backBone.getPrefHeight()){ //only show the HBar if the pink is no longer an issue. Essentially if it fits in the ideal buffer range then we can show it.
                    System.out.print("Either exceeds BB, or within 20 pixels of BB [] ");    
                    //in parent substitutions - old position which will decide how it figures out its new HBar position adjustment.
                    double iPMinY =ActualIPY - 0.5*(innerPane.getPrefHeight()*innerPane.getScaleY()-innerPane.getPrefHeight()) + (-1*moveInnerV.getValue()); //NOTE IF it is zoomed up instead of down for initial, does this change the polarity correclty? [might end up with wonky numbers otherwise]
                    System.out.println("ActualIpY : "+ActualIPY+", PrefHeight : "+innerPane.getPrefHeight()+", scale "+innerPane.getScaleY());
                    double iPMaxY=iPMinY+innerPane.getPrefHeight()*innerPane.getScaleX();
                        System.out.print("iPMinY : "+iPMinY+", backBone PrefHeight "+backBone.getPrefHeight()+" iPMax: "+ iPMaxY + " [] ");
                        //When max zoomed out, what are we dealing with.  

                    //No pink
                    if (iPMinY<=0 && iPMaxY>=backBone.getPrefHeight()){ 
                        System.out.print("No pink shown [] ");
                        newVBarVal=moveInnerV.getValue()*(RatioScale); //ratio between old and new scales.
                    }
                    //Pink 
                    else { //UP PINK, buffer in play.  
                        System.out.println("Pink shown [] ");
                        if (moveInnerV.getValue()<0) {  
                            System.out.print("Left pink shown [] ");
                            if (RatioScale<=1){ //zoom out 
                            //Take the old amount over, multiply by the scale ratio, add to the the min (wihtout buffer accounted)
                                System.out.print("zoom out [] ");
                                newVBarVal=newVBarMin+((moveInnerV.getValue()-moveInnerV.getMin()-compensation)*RatioScale);   
                                System.out.println("newVBarVal = "+newVBarVal+", old value: "+ moveInnerV.getValue()+", moveInnerGetMin: "+moveInnerV.getMin()+", IdealEZbuffer: "+idealEzBuffer+", ratio scale: "+RatioScale);
                            } else{ //zoom in 
                                System.out.print("zoom in [] ");
                                //The idea is to get as close to the center of the shape as I can slowly.  Ratio scale is greater than 1, so invert it and we get the amount to 'shrink' it by.
                                newVBarVal=newVBarMin+((moveInnerV.getValue()-moveInnerV.getMin()-compensation)*(Math.pow(RatioScale, -1))); 
                            } 
                        }
                        else if (moveInnerV.getValue()>0) { //DOWN PINK (If toggle is set to -1 (Sider moves with the panel)) 
                            System.out.print("Right pink shown [] ");
                            
                            if (RatioScale<=1) { //zoom out  
                                System.out.print("zoom out [] ");
                                newVBarVal=newVBarMax + ((moveInnerV.getValue()-moveInnerV.getMax()+compensation)*RatioScale);  
                            } else { //zoom in 
                                System.out.print("zoom in [] ");
                                newVBarVal=newVBarMax + ((moveInnerV.getValue()-moveInnerV.getMax()+compensation)*(Math.pow(RatioScale,-1)));  
                            }

                        }
                        else { 
                            System.out.print("static [] ");
                            newVBarVal=0; //shouldn't happen but might?
                        }
                    } 
                    newVBarMax=newVBarMax+compensation; //acount for buffer, easier to think of it figuring distances.
                    newVBarMin=-newVBarMax;
                    moveInnerV.setValue(0); //just sowe don't have any expansion or strangeness, completley unncessary I believe.
                    moveInnerV.setMax(newVBarMax);
                    moveInnerV.setMin(newVBarMin);
                    moveInnerV.setValue(newVBarVal); 

                } 
                else { 

                    moveInnerV.setValue(0);
                    moveInnerV.setMax(0);
                    moveInnerV.setMin(0);
                    moveInnerV.setVisible(false);
                }

 
             
        System.out.println("reboundMoveInnerV2 Over");
    }






    int testNum=0;
    Scanner scanner = new Scanner(System.in); 
    Button runTest =new Button("test "); 
    public void testScale2(double EzW, double EzH, Stage stage,double BbW,double BbH){

            System.out.println("EZ: ("+EzW+", "+EzH+")");   
            runTest.setLayoutX(10);
            runTest.setLayoutY(10);
 
            System.out.println("BackBone Details Before Tests: "+backBone.getBoundsInParent()); 
            System.out.println("(w,h) - EZ: ("+EzW+", "+EzH+")");
            System.out.println("TEST START: "+testNum); 
            
            backBone.setMaxWidth(BbW); backBone.setMaxHeight(BbH);
            backBone.setMinWidth(BbW); backBone.setMinHeight(BbH);
            backBone.setPrefWidth(BbW); backBone.setPrefHeight(BbH);
            backBone.setLayoutX(0);
            backBone.setLayoutY(0);
            
            innerPane.setScaleX(1); innerPane.setScaleY(1);
            innerPane.setLayoutX(0); innerPane.setLayoutY(0); 
            innerPane.setMaxWidth(EzW); innerPane.setMaxHeight(EzH);
            innerPane.setMinWidth(EzW); innerPane.setMinHeight(EzH);
            innerPane.setPrefWidth(EzW); innerPane.setPrefHeight(EzH);
                    System.out.println("ip layout par: "+innerPane.getBoundsInParent());
                    System.out.println("ip layout loc: "+innerPane.getBoundsInLocal());
                    System.out.println("ip scaled dim: "+innerPane.getPrefWidth()*innerPane.getScaleX()); 
            
            figInitialScale2(); 
            
            System.out.println("BackBone Details After Tests: "+backBone.getBoundsInParent()); 
            


        
        System.out.println("TEST OVER");
    }

    
    

    Stage primStage =new Stage(); 
    Group root = new Group(); 
    
    
    @Override //it gets upset at me if I don't have hte Stage stage argument
    public void start(Stage stage) throws Exception{ //Stage primaryStage) { //throws Exception {
        
        //Boring set up.
        //#region
        moveInnerH.setBlockIncrement(1);
        moveInnerH.setBlockIncrement(1);
        zoomBar.setBlockIncrement(1);
        moveInnerH.setUnitIncrement(1);
        moveInnerV.setUnitIncrement(1);
        zoomBar.setUnitIncrement(1);


        ScaleResizeTest.selfInfo();  
        Scene scene = new Scene(root, initialSceneWidth, 
            initialSceneHeight, defaultSceneColor);
        stage=this.setDefaultsStage(primStage);  //changed from start's stage to a global primStage, for test purposes in RectBug.

        System.out.println("icons loaded."); //NOTE If it doesn't load, often times it just needs a fresh save. Not sure if the compiler actively checks or if VS Code is doing something funky.
        //Set and show the scene on the stage.
        stage.setScene(scene);  
        stage.show();  //IF THIS IS MOVED, THINGS DON:T GO SUPER WELL. WHY?

        stage.setAlwaysOnTop(true);// to have console as well as screen.

        //details on layout, discrepency
        figureDecorDiscrep(scene);
        stage.setWidth(decorW+initialSceneWidth);
        stage.setHeight(decorH+initialSceneWidth);
        stage.setMinHeight(decorH+initialSceneHeight);
        stage.setMinWidth(decorW+initialSceneWidth); 
         
        //Tree stucture:
        root.getChildren().addAll(backBone);   
        backBone.getChildren().addAll(
            this.expandableZone,this.innerPane, //panes
            moveInnerH,moveInnerV,zoomBar //scrollbars
            );   
        //#endregion 
        
        //In the future this should just set up defaults dimensions, positoin, and visiblity of the various scrollbars.
        this.setScrollable(initialSceneWidth, initialSceneHeight);   
         
        
          
         
        
        //This should be figuring out the zoomBar min/max, EZ, Backbone, and IP
        figureInitialScale();   

        //used for recentering -zoom, H/V Bar.
            initScaleEZX=expandableZone.getLayoutX();
            initScaleEZY=expandableZone.getLayoutY();
            //addCheckers(expandableZone); //to track centering
            //addCheckers2(expandableZone);
            //removeCheckers(expandableZone);


         //Begin, test backbone odd dimensions with odd expandablepane dimensions.   
        backBone.getChildren().add(runTest);
        runTest.setOnAction(new EventHandler<ActionEvent>() {
            @Override 
            public void handle(ActionEvent e) { 
                System.out.print("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");//effectively flushes the console.
                
                 
                removeCheckers(innerPane); 
                switch (testNum) {
                    case 0:
                        testScale2(410,410, primStage, initialSceneWidth, initialSceneHeight);
                        backBone.getChildren().add(new Rectangle(idealEzBuffer/2, idealEzBuffer/2, 20, 20));//to be sure the layout is correct. not necessary.
                        break;
                    case 1:
                        testScale2(205,205, primStage, initialSceneWidth, initialSceneHeight); 
                        break;
                    case 2:
                        testScale2(820, 820, primStage, initialSceneWidth, initialSceneHeight);
                        break;
                    case 3:
                        testScale2(410,410, primStage, initialSceneWidth, initialSceneHeight);
                        break; 
                    case 4:
                        testScale2(410,410, primStage, 840, 420); 
                        break; 
                    case 5:
                        testScale2(410,820, primStage, 420, 420); 
                        break; 
                    case 6: //center vertically
                        testScale2(410,410, primStage, 840, 420);  
                        break; 
                    case 7: //center horizontially
                        testScale2(410,820, primStage, 420, 420);   
                        break;


                    case 8: //Center horizontally,buffer from top and bottom.
                        testScale2(820,410, primStage, 420, 420); 
                        System.out.println("Case 1");
                        break;   
                    case 9:
                        testScale2(410,820, primStage, 420, 420);  
                        System.out.println("Case 2");
                        break;
                    case 10: //6 and 7 are tests of not forced layout of X. T
                        testScale2(410,410, primStage, 840, 420); 
                        System.out.println("Case 3");
                        break;  
                    case 11: //Center horizontally,buffer from top and bottom.
                        testScale2(820,410, primStage, 520, 420); 
                        System.out.println("nonsquare Case 1");
                        break;   
                    case 12:
                        testScale2(410,820, primStage, 520, 420);  
                        System.out.println("nonsquare Case 2");
                        break;
                    case 13:
                        testScale2(510,410, primStage, 840, 420); 
                        System.out.println("nonsquare Case 3");
                        break;  
                    case 14:  
                        testScale2(11,11, primStage, 420, 420); 
                        System.out.println("Very small EZ > minPixelZoom");
                        break; 
                    case 15:  
                        testScale2(9,9, primStage, 420, 420); 
                        System.out.println("Very small EZ < minPixelZoom");
                        break; 
                    case 16:  
                        testScale2(195,195, primStage, 420, 420);  
                        System.out.println("Actual cord IP ("+ActualIPX+", "+ActualIPY+"), scale: "+innerPane.getScaleX());
                        break; 
                    default:
                        removeCheckers(innerPane);            
                        System.out.println("End of tests");    
                        scanner.close();
                        break;
                }  
                addCheckers(innerPane); //to track centering
                addCheckers2(innerPane);
                testNum+=1;
            }
        });
        
        


        zoomBar.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) { 
                System.out.println("ZoomBar Triggered");  
                double OldScale=innerPane.getScaleX(); //doesn't matter X or Y.
                double NewScale=zoomBar.getValue();
 

                if (NewScale != 0 ){ //ASSERT: ZoomBar is never 0 unless it's being reset to run a valid value. Should be replaced by a boolean later. 
                        innerPane.setScaleX(zoomBar.getValue()); 
                        innerPane.setScaleY(zoomBar.getValue());  
                        System.out.println("Pervious EZinitX, Y = ("+initScaleEZX+", "+initScaleEZY+")");
                        figureExpandableZoneBasedOnIP();   //Whenever IP Scale is changed, must call this IMMEDIATELY After to keep Ez in synch.

                    if (NewScale == zoomBar.getMin()){ 
                        reboundMoveInnerH2(0); //Assert, EZ and IP scale are set beforehand.
                        reboundMoveInnerV2(0); 
                    } else { 
                        double RatioScale=NewScale/OldScale;  
                        reboundMoveInnerH2(RatioScale); 
                        reboundMoveInnerV2(RatioScale);  
                    }
                    
                    System.out.println("HBarValue: "+moveInnerH.getValue());
                    System.out.println("[zoombar] Init Scaled IP X and Y = ("+initScaleIPX+", "+initScaleIPY+")");  
                    System.out.println("New EZinitX, Y = ("+initScaleEZX+", "+initScaleEZY+")");
                    innerPane.setLayoutX(initScaleIPX+(-1*moveInnerH.getValue())); 
                    innerPane.setLayoutY(initScaleIPY+(-1*moveInnerV.getValue())); 
                    expandableZone.setLayoutX(initScaleEZX+(-1*moveInnerH.getValue())); 
                    expandableZone.setLayoutY(initScaleEZY+(-1*moveInnerV.getValue()));  //Perhaps move these after figure based on IP. 
                    System.out.println("ExpandablePane X and Y = ("+expandableZone.getLayoutX()+", "+expandableZone.getLayoutY()+")");
                    System.out.println("ExpandablePane width height = ("+expandableZone.getPrefWidth()+", "+expandableZone.getPrefHeight()+")");
                    System.out.println("ZoomBar Over");
                } 
            } 
        });
          
        moveInnerH.valueProperty().addListener(new ChangeListener<Number>()  { 
            //KEEP: Note that if we wish to allow the scroll bar to 'teleport' on click, we will need to make a flag to take the change value (if it's not already arg2) without taking ratios and other things into account.
            @Override
            public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
                System.out.println("moveInnerH Listener Triggered");
                    //moveInnerH.setValue(round(moveInnerH.getValue(), 1));  //triggers twice which is annoying -figure out a better way of doing this? 
                
                innerPane.setLayoutX(initScaleIPX + (-1*moveInnerH.getValue())); 
                expandableZone.setLayoutX(initScaleEZX + (-1*moveInnerH.getValue())); 
                System.out.println("moveInnerH Listener Over - value "+moveInnerH.getValue());
            } 
            
        });
        moveInnerV.valueProperty().addListener(new ChangeListener<Number>()  { 
                @Override
                public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
                    System.out.println("moveInnerV Listener Triggered"); 
                    innerPane.setLayoutY(initScaleIPY + (-1*moveInnerV.getValue())); 
                    expandableZone.setLayoutY(initScaleEZY + (-1*moveInnerV.getValue())); 
                    System.out.println("moveInnerV Listener Over - value "+moveInnerV.getValue());
                } 
                
            });

 
        
      
         

         
        System.out.println("Is root resizable? "+root.isResizable());
    }
     
}
