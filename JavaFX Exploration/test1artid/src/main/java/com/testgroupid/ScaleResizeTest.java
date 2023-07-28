package com.testgroupid;
 

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
            for (int row=0; row<checkerMe.getMinHeight(); row++){ //expect 50 pixels
                for (int col =0; col<checkerMe.getMinWidth(); col++) {
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
            for (int row=0; row<checkerMe.getMinHeight(); row+=4){ //expect 50 pixels
                for (int col =0; col<checkerMe.getMinWidth(); col+=5) {
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

    public void setScrollable(double refW, double refH){  
        this.backBone.setVisible(true);
        this.backBone.setCursor(Cursor.HAND);
        this.backBone.setSnapToPixel(false); //Important otherwise expandableZone/iP would get weird 'null spaces' which appear to exist but are undrawable, and outline the thing.
        backBone.setBackground(backBoneBackground); 
        this.innerPane.setVisible(true);      
        this.expandableZone.setVisible(true);
       // this.expandableZone.setSnapToPixel(false); //This isn't super important just yet... Maybe it will be in the future but for now it's not. 

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


    //TODO: Just use getbounds in parent, 
    public double figureActualDim(Pane panel, String dim){
        if (dim=="Width" || dim=="wid" ||dim=="W" || dim=="w") {
            //System.out.println("Old Method (getMinWidth): "+(panel.getMinWidth()*panel.getScaleX()));
            //System.out.println("Old Method (getWidth): "+(panel.getWidth()*panel.getScaleX()));
            //System.out.println("New Method: "+ panel.getBoundsInParent().getWidth());
            return panel.getMinWidth()*panel.getScaleX();
        } else if (dim=="Height" || dim=="hei" || dim =="H" || dim=="h"){
            return panel.getMinHeight()*panel.getScaleY();
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
                        
                        //Once again, too early, returns 0. - components have 0 size until GUI shows, which for some reason isn't immeidately after stage.show...
                            //additionally the component might need to be visible.
                        //ActualEZW = expandableZone.getBoundsInParent().getWidth();
                        //ActualEZH = expandableZone.getBoundsInParent().getHeight();
                        //System.out.println("compare new : "+ ActualEZW+", "+ActualEZH);
                        ActualEZW=figureActualDim(expandableZone, "w"); 
                        ActualEZH=figureActualDim(expandableZone, "h");
                        
                        
                        //Actuals are the 'apparent' size in it's parent. -ASSERT: Parent of these panels is scaled 1. unclear what happens if not the case. 
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
        System.out.println("ezP:"+eZPriortyScale);
        //could the buffer be changed.
        
        IdealScale=-1;
        System.out.println("Check is eZPriority Scale assigned at this time? "+eZPriortyScale);
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
        System.out.println("Zoom min, max = ("+minZoom+", "+maxZoom+"), ideal based on minPixelZoom ["+minPixelZoom+"]"+IdealScale);
        zoomBar.setMax(maxZoom);
        zoomBar.setMin(minZoom);
        zoomBar.setValue(minZoom); //default zoomed out the best we can.
        //ASSERT: Min Window size will always accomodate minPixelZoom?
        //ASSERT: Square windows allowed only right now, otherwise minPixelZoom must account for the ratio.
        //NOTE: Instead of initial scenewidht and height, might have to use some min or maxing potentially. Later tests required.
        
        System.out.println("Initial Zoombar: "+ zoomBar.getValue());
        System.out.println("Zoom bar min, max: "+zoomBar.getMin()+", "+zoomBar.getMax());
        System.out.println("ExpandableZone Scale: "+expandableZone.getScaleX());
        zoomBar.setUnitIncrement(.1);
        System.out.println("ZoomBar step: "+zoomBar.getUnitIncrement());
        //TODO: Figure out a proper zoomBar increment. Should be based on the dimensions of the frame, minPixelZoom, and the Actualdimensions of the expandablePane. 

          
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

    

    @Override //it gets upset at me if I don't have hte Stage stage argument
    public void start(Stage stage) throws Exception{ //Stage primaryStage) { //throws Exception {
        
        moveInnerH.setBlockIncrement(1);
        moveInnerH.setBlockIncrement(1);
        zoomBar.setBlockIncrement(1);
        moveInnerH.setUnitIncrement(1);
        moveInnerV.setUnitIncrement(1);
        zoomBar.setUnitIncrement(1);


        ScaleResizeTest.selfInfo(); 
        Group root = new Group();    
        Scene scene = new Scene(root, initialSceneWidth, 
            initialSceneHeight, defaultSceneColor);
        stage=this.setDefaultsStage(stage);  
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
        this.setScrollable(initialSceneWidth, initialSceneHeight);    
         
        //Tree stucture:
        root.getChildren().addAll(backBone);   
        backBone.getChildren().addAll(
            this.expandableZone,this.innerPane, //panes
            moveInnerH,moveInnerV,zoomBar //scrollbars
            );   
         
         
        //Reports - new container option that will hold labels for experimentation. 
        setReport(root, 1); 
 
         


        //#region 
            ///This sets' up the default Layout X and Y for EZ, and default scale. 

        //Resize the zones. Should be compatible with any number of positive values.  
        //resizeExpandableZoneX((initialExpandableZoneWidth)*2);//820
        //resizeExpandableZoneY((initialExpandableZoneHeight)*2);//820  

        //resizeExpandableZoneX((initialExpandableZoneWidth));//410
        //resizeExpandableZoneY((initialExpandableZoneHeight));//410

        //resizeExpandableZoneX((initialExpandableZoneWidth/2));//205
        //resizeExpandableZoneY((initialExpandableZoneHeight/2));//205
        resizeExpandableZoneX(410);//205
        resizeExpandableZoneY(410);//205   
        
        innerPane.setVisible(false); //temporary while setting up eZ. 
          
        figureInitialScale();   
        //#endregion  

         
        //used for recentering -zoom, H/V Bar.
            initScaleEZX=expandableZone.getLayoutX();
            initScaleEZY=expandableZone.getLayoutY();
            addCheckers(expandableZone); //to track centering
            addCheckers2(expandableZone);

        zoomBar.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
 
                System.out.println("ZoomBar Triggered"); 
                 
                double OldScale=expandableZone.getScaleX(); //doesn't matter X or Y.
                double NewScale=zoomBar.getValue();


                expandableZone.setScaleX(zoomBar.getValue()); 
                expandableZone.setScaleY(zoomBar.getValue());   
                if (NewScale == zoomBar.getMin()){ 
                    reboundMoveInnerH(0); 
                    reboundMoveInnerV(0);
                } else { 
                    double RatioScale=NewScale/OldScale;  
                    reboundMoveInnerH(RatioScale); 
                    reboundMoveInnerV(RatioScale);
                }

                expandableZone.setLayoutX(initScaleEZX+(-1*moveInnerH.getValue())); 
                expandableZone.setLayoutY(initScaleEZX+(-1*moveInnerV.getValue())); 
                System.out.println("ZoomBar Over");
            } 
        });

    moveInnerH.valueProperty().addListener(new ChangeListener<Number>()  { 
        //KEEP: Note that if we wish to allow the scroll bar to 'teleport' on click, we will need to make a flag to take the change value (if it's not already arg2) without taking ratios and other things into account.
        @Override
        public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
            System.out.println("moveInnerH Listener Triggered");
                //moveInnerH.setValue(round(moveInnerH.getValue(), 1));  //triggers twice which is annoying -figure out a better way of doing this? 
            
            expandableZone.setLayoutX(initScaleEZX + (-1*moveInnerH.getValue())); 
            System.out.println("moveInnerH Listener Over - value "+moveInnerH.getValue());
        } 
        
    });
    moveInnerV.valueProperty().addListener(new ChangeListener<Number>()  { 
            @Override
            public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
                System.out.println("moveInnerV Listener Triggered"); 
                expandableZone.setLayoutY(initScaleEZX + (-1*moveInnerV.getValue()));  
                System.out.println("moveInnerV Listener Over - value "+moveInnerV.getValue());
            } 
            
        });













        
        expandableZone.boundsInLocalProperty().addListener(new ChangeListener<Bounds>() { //From oracle page on 0 layouts.
            @Override 
            public void changed(ObservableValue<? extends Bounds> obs, Bounds oldBounds, Bounds newBounds) { 
                System.out.println("get W H working? With layout bound listener?");
                System.out.println("Old Bounds W, H"+"("+oldBounds.getWidth()+", "+oldBounds.getHeight()+")"); 
                System.out.println("New Bounds W, H"+"("+newBounds.getWidth()+", "+newBounds.getHeight()+")");  
            }
        });

        expandableZone.boundsInParentProperty().addListener(new ChangeListener<Bounds>() { //From oracle page on 0 layouts.
            @Override 
            public void changed(ObservableValue<? extends Bounds> obs, Bounds oldBounds, Bounds newBounds) { 
                //recenter <-will keep calling itself over and over! 
                //expandableZone.setLayoutX(initScaleEZX);
                //expandableZone.setLayoutY(initScaleEZY); //recenter... 
                
            }
        });



/* Not sure if this is needed still 
        
        //Now attempt to deal with H and V bar stuff
        //From if smaller than stage.
            ActualEZW=(expandableZone.getScaleX()*expandableZone.getMinWidth()); 
            ActualEZX =initialSceneWidth-eZtoAnchorX-expandableZone.getMinWidth();  
            double defaultY=expandableZone.getLayoutY();
            double defaultX=expandableZone.getLayoutX();
            //expandableZone.setLayoutY((ActualEZY+eZtoAnchorY)/2); 
        //On scale change
        double diffActualX=initialSceneWidth-eZtoAnchorX //'ideal box'
                            -expandableZone.getMinWidth()*expandableZone.getScaleX(); //'minus how big it actually is'
        double hBarMin=-Math.abs(diffActualX); //I could make it snap to pixels, but for now let's leave it smooth
        double hBarMax = Math.abs(diffActualX);
        //Get H/VBar positions. Should default ot '0'
        if (hBarMin==hBarMax) {//hide hBar
            ;
        }
        if (!moveInnerH.isVisible()){ //if this wasn't visible before, make visible set value 0
            moveInnerH.setVisible(true);
            moveInnerH.setValue(0);
        }
        moveInnerH.setMin(hBarMin);
        moveInnerH.setMax(hBarMax); 
        
        moveInnerH.setVisible(false);
        
        //Zoom protocols (if bigger than desired box, smaller tah ndesired box, scenewidth scene height != buffer?)
        //H/V bars show/hide based on zoom
        //H/V bars set value and max min based on zoom and where we have zoomed. Note we will have to add a getH bar value and V bar value for when we are eediting it's layoutX/Y
 */









        System.out.println("L"+expandableZone.getBoundsInLocal());
        System.out.println("P"+expandableZone.getBoundsInParent());
        expandableZone.setScaleX(.3);
        expandableZone.setScaleY(.3);
        expandableZone.setLayoutX(expandableZone.getLayoutX()-.5*(expandableZone.getBoundsInLocal().getWidth()-expandableZone.getBoundsInParent().getWidth()));//KEEP, I think this will center it no matter the scale to it's 'original' layout position
        System.out.println("L"+expandableZone.getBoundsInLocal());
        System.out.println("P"+expandableZone.getBoundsInParent());










        setReport(root, 1); 
        //Add circles
        //#region
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
        //#endregion

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
         
       
        //this.resizeScrollableInnerPane();
        
        
         

        innerPane.setOnMouseEntered(new EventHandler<Event>() { 
            @Override
            public void handle(Event event){
                //System.out.println("InnerPane " + event.getEventType());
            }
        });
        innerPane.setOnMouseExited(new EventHandler<Event>() { 
            @Override
            public void handle(Event event){
                //System.out.println("InnerPane " + event.getEventType());
            }
        });
        backBone.setOnMouseEntered(new EventHandler<Event>() { 
            @Override
            public void handle(Event event){
                //System.out.println("backPane " +event.getEventType());
            }
        });
        backBone.setOnMouseExited(new EventHandler<Event>() { 
            @Override
            public void handle(Event event){
                //System.out.println("backPane "+event.getEventType());
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
