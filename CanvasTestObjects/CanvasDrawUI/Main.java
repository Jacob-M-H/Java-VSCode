package CanvasTestObjects.CanvasDrawUI;

import CanvasTestObjects.TestCanvasObjects;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
/*Test smaller scale features. */
public class Main extends Application{
    private boolean turnTrans=false; //for a test. Button flag must be provided as a member.

    /*Required to be arguments to be seen in button action handlers. */
    
    public TestCanvasControlObjects testPixelControl=new TestCanvasControlObjects(500,500,.1);
    public static void main(final String[] args){
        launch(args);  
    } 
    @Override
    public void start(Stage primaryStage) throws Exception {
        
        AnchorPane required=new AnchorPane(); //Required to render a scene


        //A Vbox with a HBox button container, and a Canvas. 
        VBox testContainer = testPixelControl.getTestContainer(); 
        required.getChildren().add(testContainer);
        testPixelControl.getButtonHolder().setBackground(this.newBackground("#32a850", 0, 0));
        testPixelControl.getTestContainer().setBackground(this.newBackground("#cf8b6b",0,0));//lowest level,
        testPixelControl.getBindMaster().setBackground(this.newBackground("#32a850")); //Next lowest
        testPixelControl.getDragNodesPane().setBackground(this.newBackground("#9384ab"));//highest level.
    

        //TODO: dragCircle will need seperate properties that will adjust all non dragged points. Points being dragged will have to be adjusted as frame of reference changes. For now ignoring.
        //TODO: corner radii and insets need to be watched for both transCanvas and dragCircle panes, custom property needs to be defined.
        Button drawButton = testPixelControl.addButton("Draw something!");
        TextField points=new TextField("0");
        testPixelControl.getButtonHolder().getChildren().add(points);
        

 
        drawButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {  
                Canvas transCanvas=testPixelControl.getDrawCanvas();
                transCanvas.getGraphicsContext2D().setStroke(Color.BLACK);
                transCanvas.getGraphicsContext2D().beginPath();
                transCanvas.getGraphicsContext2D().lineTo(50, 50);
                transCanvas.getGraphicsContext2D().stroke();
                transCanvas.getGraphicsContext2D().closePath();
                if (turnTrans==false){
                    System.out.println("Draw!");
                    turnTrans=true;
                }
                else{
                    System.out.println("Trans!");
                    transCanvas.opacityProperty().setValue(0); //This may mess with drawing.

                    testPixelControl.getDragNodesPane().opacityProperty().setValue(0);
                }
            }
        }); 




        Scene appScene=new Scene(required, 550, 600); 
        primaryStage.setScene(appScene); 
        primaryStage.show();

    }
    public Background newBackground(String hex, double cornerRadii, double inset){
        BackgroundFill backgroundFill = new BackgroundFill(Color.valueOf(hex), new CornerRadii(cornerRadii), new Insets(inset));
        return new Background(backgroundFill);
    }
    public Background newBackground(String hex){
        BackgroundFill backgroundFill = new BackgroundFill(Color.valueOf(hex), new CornerRadii(10), new Insets(10));
        return new Background(backgroundFill);
    }
    public void changeButtonName(Button btn, String label){
        btn.setText(label);
    }
}