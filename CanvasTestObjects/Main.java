package CanvasTestObjects;
import java.util.ArrayList;
 
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType; 

/*Test smaller scale features. */
public class Main extends Application{

    /*Required to be arguments to be seen in button action handlers. */
    public int drawStroke=0;
    public ArrayList<String> strokeNotes=new ArrayList<String>();
    public TestCanvasObjects testPixelControl=new TestCanvasObjects();
    public static void main(final String[] args){
        launch(args);  
    } 
    @Override
    public void start(Stage primaryStage) throws Exception {
        
        AnchorPane required=new AnchorPane(); //Required to render a scene


        //A Vbox with a HBox button container, and a Canvas. 
        VBox testContainer = testPixelControl.getTestContainer();
        testContainer.getChildren().addAll(testPixelControl.getButtonHolder(), testPixelControl.getCanvas());
        required.getChildren().add(testContainer);
        testPixelControl.getButtonHolder().setBackground(this.newBackground("#32a850", 0, 0));
        testPixelControl.getTestContainer().setBackground(this.newBackground("#cf8b6b"));

        //Add a button, 
        Button drawButton=testPixelControl.addButton("Draw");  

        //Canvas needs size
        testPixelControl.getCanvas().setWidth(500);
        testPixelControl.getCanvas().setHeight(500); 

        //Test drawing with gc option(s). 
        //strokeNotes.add("DefaultPathWithoutMoveTo");
        //strokeNotes.add("Reset");
        strokeNotes.add("LineBezierLineWithoutMove");
        strokeNotes.add("Reset");
        strokeNotes.add("First Stroke");
        strokeNotes.add("Reset");
        //strokeNotes.add("Draw line");
        //strokeNotes.add("Reset");
        //strokeNotes.add("bezierCurve");
        //strokeNotes.add("Reset");
        //strokeNotes.add("LineBezierLine");
        //strokeNotes.add("Reset"); 
        this.changeButtonName(drawButton, strokeNotes.get(drawStroke));

        drawButton.setOnAction(new EventHandler<ActionEvent>(){
            @Override 
            public void handle(ActionEvent e){
                GraphicsContext gc=testPixelControl.getCanvas().getGraphicsContext2D();
                if (strokeNotes.size()<1){
                    return;
                } 
                System.out.printf("Button Val: %s\n", strokeNotes.get(drawStroke));
                
                switch(strokeNotes.get(drawStroke)){ 
                    case "First Stroke":
                        System.out.println("Please draw something!");
                        
                        gc.arc(250,250, 100, 50, 180, 300); 
                        
                        gc.setFill(Color.GREEN);
                        gc.setStroke(Color.BLUE);
                        gc.fillArc(250,250, 100,100, 0, 180, ArcType.ROUND);
                        
                        gc.setFill(Color.BLUE);
                        gc.setStroke(Color.GREEN);
                        gc.fillArc(250,250, 100,100, 90, 180, ArcType.ROUND);
                        //Start at 90 degrees with the 'draw line' rotating counter clockwise as degres work like that. Okay

                        gc.fill();  //fill the path.
                        gc.stroke(); //outline
                        //CenterX, CenterY, RadiusX, RadiusY - ellipse
                        //StartAngle, Length <- ?
                        break;
                    case "svgPath":
                        break;
                    case "Draw line": //Fail. This doesn't seem like it should fail.
                        gc.setFill(Color.BLACK);
                        gc.setStroke(Color.RED);
                        gc.setLineWidth(5);

                        gc.beginPath();
                        gc.moveTo(50,50);
                        gc.lineTo(100, 100); 
                        gc.closePath();
                        gc.fill();  //fill the path.
                        gc.stroke(); //outline 
                        break;
                    case "LineBezierLineWithoutMove"://odd
                        gc.setFill(Color.BLACK);
                        gc.setStroke(Color.RED);
                        gc.setLineWidth(5);

                        gc.beginPath();
                        gc.moveTo(50,50); //Quesiton where is the default coordinates of moveTo
                         
                        gc.lineTo(100, 100); 
                        gc.closePath(); 
                        gc.stroke();
                        
                        gc.setStroke(Color.BLUE);
                        gc.bezierCurveTo(100, 100, 150, 150, 175, 175);
                        gc.stroke();

                        gc.setStroke(Color.RED);
                        gc.lineTo(500, 500); 

                        gc.closePath();
                        gc.stroke();
                        gc.fill();  
                        break;


                    case "bezierCurve": //works.
                        gc.setFill(Color.BLACK);
                        gc.setStroke(Color.RED);
                        gc.setLineWidth(5); 

                        gc.beginPath();
                        gc.moveTo(50,50);
                        //Bezier curve has 3 points associated. 
                        gc.bezierCurveTo(150, 20, 150, 150, 75, 150);
                        gc.stroke(); //Apply the stroke
                        gc.closePath(); //apply the path
                        gc.fill();  //fill the path.
                        gc.stroke(); //outline
                        //TODO: clip the stroke so that it is flush. OR figure out the additional lines/curves that must befilled for outline
                        break;
                    case "LineBezierLine": //Lines require STrokes!
                        gc.setFill(Color.BLACK);
                        gc.setStroke(Color.RED);
                        gc.setLineWidth(5);

                        gc.beginPath();
                        gc.moveTo(50,50);
                        gc.lineTo(100, 100); 
                        gc.closePath();

                        gc.beginPath();
                        gc.moveTo(50,50);
                        gc.bezierCurveTo(100, 100, 150, 150, 175, 175);
                        gc.closePath(); 

                        
                        gc.beginPath();
                        gc.moveTo(175,175);
                        gc.lineTo(500, 500); 
                        gc.closePath();
                        gc.fill();  
                        gc.stroke(); 
                        break;
                    case "DefaultPathWithoutMoveTo": //FAIL
                        //This does not draw a line! Needs a starting position.
                        gc.setFill(Color.BLACK);
                        gc.setStroke(Color.RED);
                        gc.setLineWidth(5);

                        gc.beginPath();
                        //Note that a single lineTo will FAIL.
                            //A second lineTo will clip based on the direction. I believe teh default clip is parallel with X axis, while the other end(s) will have a slope perpendicular to the previous line's slope.
                        gc.lineTo(100, 100); 
                        gc.lineTo(200, 200);  
                        gc.closePath();
                        gc.fill();  //fill the path.
                        gc.stroke(); //outline

                        break;
                    case "Reset":
                        double width=testPixelControl.getCanvas().getWidth(); 
                        double height=testPixelControl.getCanvas().getHeight(); 
                        gc.clearRect(0,0,width,height);
                        break;
                    default:
                        break;               
                
                }

                drawStroke=drawStroke+1;    
                if (drawStroke>strokeNotes.size()-1){
                    drawStroke=0;
                }     
                changeButtonName(drawButton, strokeNotes.get(drawStroke));

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
