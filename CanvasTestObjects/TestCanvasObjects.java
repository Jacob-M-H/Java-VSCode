package CanvasTestObjects;

import CanvasTestObjects.CanvasShapes.TestCanvasCord;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Shape;

public class TestCanvasObjects {
    HBox buttonHolder=null;
    Canvas tabCanvas=null; 
    VBox testContainer=null;
    
    public TestCanvasObjects(){
        this.testContainer=new VBox();
        this.testContainer.setPrefWidth(550);
        this.testContainer.setPrefHeight(600);
        this.buttonHolder=new HBox();
        this.buttonHolder.setPrefHeight(50);
        this.buttonHolder.setPrefHeight(100);
        this.tabCanvas=new Canvas(500, 500); 
    }
    
    public Button addButton(String buttonStr){
        Button toReturn=new Button(buttonStr);
        this.buttonHolder.getChildren().add(toReturn);
        return toReturn;
    }

    public void addShape(){
        GraphicsContext gc=this.tabCanvas.getGraphicsContext2D(); 
    }
    public HBox getButtonHolder(){
        return this.buttonHolder;
    }
    public Canvas getCanvas(){
        return this.tabCanvas;
    }
    public VBox getTestContainer(){
        return this.testContainer;
    }
    

    public TestCanvasCord[] returnShapeCords(){
        return null;
    }
}
