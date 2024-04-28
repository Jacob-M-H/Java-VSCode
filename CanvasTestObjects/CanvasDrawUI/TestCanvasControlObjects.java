package CanvasTestObjects.CanvasDrawUI;

import CanvasTestObjects.CanvasShapes.TestCanvasCord;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Shape;

//IGNORING SCALE for now.

public class TestCanvasControlObjects {
    VBox testContainer=null;
    
    HBox buttonHolder=null;
    
    Pane CanvasBindMaster=null; //everyone [everyone in the tabs or in the container] binds back to me!
    Canvas tabCanvas=null;  //I'm the canvas a tab specifies!
    Canvas tempDrawCanvas=null; //I'm the canvas UI elements are temporarily drawn to!
    Pane dragNodes=null; //I'm the pane that ignores zoom level, to keep nodes a constant size! I give the dots to tempDrawCanvas to give Path/Polygon info! I also adjust by nodes by the BindMaster zoom/layout/width/height!
    //DragNodes transform any draw action as to be put onto the canvas layer [so from a non scaled position, to a scaled layer]
        //However, the drawing itself is done on tempDrawCanvas, it is only the nodes that are put onto dragNodes, and the position recieved. 



    //Canvas position/scale/dimensions in BindMaster
    //x,y,w,h,zoom/scale <-watched by Tab canvas, TempDrawCanvas, 
    //Also watched by drag nodes, but drag nodes adjusts it's children positions by these, not itself.
    private DoubleProperty x = new SimpleDoubleProperty(0);
    private DoubleProperty y = new SimpleDoubleProperty(0);
    private DoubleProperty w = new SimpleDoubleProperty(0);
    private DoubleProperty h = new SimpleDoubleProperty(0);
    private DoubleProperty zoom = new SimpleDoubleProperty(0);

    private DoubleProperty regionWidth=new SimpleDoubleProperty(0);
    private DoubleProperty regionHeight=new SimpleDoubleProperty(0);
    //Region x/y assumed to be 0... Should likely bind though. 
    //Assuming children treat their parent's upper left corner as 0,0.

    public TestCanvasControlObjects(double width,double height, double buttonContainerPer){ //these three params later can be made into binds.

        //Initialize in order of depth and position in parent containers
        testContainer = new VBox();
        testContainer.setPrefWidth(width);
        testContainer.setPrefHeight(height);
        
        double buttonDimH=height*buttonContainerPer;
        buttonHolder=new HBox();
        buttonHolder.setPrefWidth(width);
        buttonHolder.setPrefHeight(buttonDimH);
        //RegionBinds
        regionWidth.setValue(width);
        regionHeight.setValue(height-buttonDimH);        

        CanvasBindMaster=new Pane();
        CanvasBindMaster.minWidthProperty().bind(regionWidth);
        CanvasBindMaster.maxWidthProperty().bind(regionWidth);
        CanvasBindMaster.prefWidthProperty().bind(regionWidth);
        CanvasBindMaster.maxHeightProperty().bind(regionHeight);
        CanvasBindMaster.minHeightProperty().bind(regionHeight);
        CanvasBindMaster.prefHeightProperty().bind(regionHeight);



        //Basic, assuming canvas doesn't need to be saceld at all, in this way all the layers should be sandwitched.
            //Initially should be grabbed from Canvas's after being scaled to fit
        x.setValue(CanvasBindMaster.layoutXProperty().getValue());
        y.setValue(CanvasBindMaster.layoutYProperty().getValue());
        w.setValue(CanvasBindMaster.widthProperty().getValue());
        h.setValue(CanvasBindMaster.heightProperty().getValue());
        zoom.setValue(0);


        tabCanvas=new Canvas();
        tabCanvas.layoutXProperty().bind(x);
        tabCanvas.layoutYProperty().bind(y);
        tabCanvas.widthProperty().bind(w);
        tabCanvas.heightProperty().bind(h);

        tempDrawCanvas =new Canvas();
        tempDrawCanvas.layoutXProperty().bind(x);
        tempDrawCanvas.layoutYProperty().bind(y);
        tempDrawCanvas.widthProperty().bind(w);
        tempDrawCanvas.heightProperty().bind(h); 


        //Adjusts dragable when needed.
        dragNodes=new Pane();
        dragNodes.maxHeightProperty().bind(CanvasBindMaster.heightProperty());
        dragNodes.minHeightProperty().bind(CanvasBindMaster.heightProperty());
        dragNodes.prefHeightProperty().bind(CanvasBindMaster.heightProperty()); 
        dragNodes.maxWidthProperty().bind(CanvasBindMaster.widthProperty());
        dragNodes.minWidthProperty().bind(CanvasBindMaster.widthProperty());
        dragNodes.prefWidthProperty().bind(CanvasBindMaster.widthProperty()); 


        //Layout
        testContainer.getChildren().addAll(buttonHolder, CanvasBindMaster);
        CanvasBindMaster.getChildren().addAll(tabCanvas, tempDrawCanvas, dragNodes);
    }

    public void reportTestContainer(){
        System.out.printf("CanvasBindMaster | tabCanvas | tempDrawCanvas | dragNodes\n");
        System.out.printf("X/Y\n");
        System.out.printf("%f | %f | %f | %f\n",CanvasBindMaster.layoutXProperty(),tabCanvas.layoutXProperty(),tempDrawCanvas.layoutXProperty(),dragNodes.layoutXProperty());
        System.out.printf("%f | %f | %f | %f\n",CanvasBindMaster.layoutYProperty(),tabCanvas.layoutYProperty(),tempDrawCanvas.layoutYProperty(),dragNodes.layoutYProperty());
        
        System.out.printf("Width/Height\n");System.out.printf("%f | %f | %f | %f\n",CanvasBindMaster.widthProperty(),tabCanvas.widthProperty(),tempDrawCanvas.widthProperty(),dragNodes.widthProperty());
        System.out.printf("%f | %f | %f | %f\n",CanvasBindMaster.heightProperty(),tabCanvas.heightProperty(),tempDrawCanvas.heightProperty(),dragNodes.heightProperty());
        
        System.out.printf("Scale X/y\n");
        System.out.printf("%f | %f | %f | %f\n",CanvasBindMaster.scaleXProperty(),tabCanvas.scaleXProperty(),tempDrawCanvas.scaleXProperty(),dragNodes.scaleXProperty());
        System.out.printf("%f | %f | %f | %f\n",CanvasBindMaster.scaleYProperty(),tabCanvas.scaleYProperty(),tempDrawCanvas.scaleYProperty(),dragNodes.scaleYProperty());
    }
    
    public Button addButton(String buttonStr){
        Button toReturn=new Button(buttonStr);
        this.buttonHolder.getChildren().add(toReturn);
        return toReturn;
    }
 
    public VBox getTestContainer(){
        return this.testContainer;
    }
    public HBox getButtonHolder(){
        return this.buttonHolder;
    }
    public Pane getBindMaster(){
        return this.CanvasBindMaster;
    }
    public Canvas getTabCanvas(){
        return this.tabCanvas;
    }
    public Canvas getDrawCanvas(){
        return this.tempDrawCanvas;
    }
    public Pane getDragNodesPane(){
        return this.dragNodes;
    }

    //Set bindings/get Bindings. Regions/x,y,w,h,zoom
    //Bindings shoudl predect dimensions ans assumptions.
    //Switching panes/getting panes and adding chidlren shouldn't affect functionality if ones careful. If it does, we'll make that a private, and only accessable throuhg methods for certain actions.
    
    

    public TestCanvasCord[] returnShapeCords(){
        return null;
    }
}
