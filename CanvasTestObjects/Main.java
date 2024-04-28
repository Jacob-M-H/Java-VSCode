package CanvasTestObjects;
import java.util.ArrayList;

import javax.xml.transform.Source;

import javafx.animation.ScaleTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.ColorInput;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Transform; 

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
        /*
            //Test drawing with gc option(s). 
                strokeNotes.add("DefaultPathWithoutMoveTo");
                strokeNotes.add("Reset");
                strokeNotes.add("LineBezierLineWithoutMove");
                strokeNotes.add("Reset");
                strokeNotes.add("First Stroke");
                strokeNotes.add("Reset");
                strokeNotes.add("Draw line");
                strokeNotes.add("Reset");
                strokeNotes.add("bezierCurve");
                strokeNotes.add("Reset");
                strokeNotes.add("LineBezierLine");
                strokeNotes.add("Reset"); 
                strokeNotes.add("testOrderFillCloseStroke");//HUGE Difference!
                strokeNotes.add("Reset"); 
                strokeNotes.add("testArcFillStrokeVSArcFillArcFillStroke"); //HUGE difference
                strokeNotes.add("Reset"); 
                strokeNotes.add("testArcTypes");
                strokeNotes.add("Reset"); 
                strokeNotes.add("test5RectWith5StrokeLine");
                strokeNotes.add("Reset"); 
            //additional tests.

                //Font tests
                strokeNotes.add("fontTests");
                strokeNotes.add("Reset"); 
                strokeNotes.add("fontTests2");
                strokeNotes.add("Reset"); 
                strokeNotes.add("fontTests3");
                strokeNotes.add("Reset"); 
                
                //Rotate, Draw, negRotate
                strokeNotes.add("Rotate");
                strokeNotes.add("fontTests"); 
                strokeNotes.add("DeRotateNeg");
                strokeNotes.add("fontTests2");
                strokeNotes.add("Reset"); 
                //Rotate, Draw, posRotate 
                strokeNotes.add("Rotate");
                strokeNotes.add("fontTests"); 
                strokeNotes.add("DeRotatePos");
                strokeNotes.add("fontTests2");
                strokeNotes.add("Reset");
                
                //Draw, Rotate, negRotate
                strokeNotes.add("fontTests");
                strokeNotes.add("Rotate"); 
                strokeNotes.add("DeRotateNeg");
                strokeNotes.add("fontTests2");
                strokeNotes.add("Reset"); 
                //Draw, Rotate, posRotate 
                strokeNotes.add("fontTests");
                strokeNotes.add("Rotate"); 
                strokeNotes.add("DeRotatePos");
                strokeNotes.add("fontTests2");
                strokeNotes.add("Reset");
                

                //Save reset
                strokeNotes.add("fontTests");
                strokeNotes.add("Save"); 
                strokeNotes.add("Reset"); 
                strokeNotes.add("Restore"); 
                strokeNotes.add("Reset"); 
        */
          
        //Venn Diagram for blends and effects
        strokeNotes.add("Oval");
        strokeNotes.add("Reset"); 
        strokeNotes.add("Venn Diagram");
        strokeNotes.add("Reset"); 

        //Venn Diagram alpha
        strokeNotes.add("Global Alpha Test1");
        strokeNotes.add("Reset");
        strokeNotes.add("Venn Diagram");
        strokeNotes.add("Reset");
        strokeNotes.add("Alpha=50");
        strokeNotes.add("Venn Diagram");
        strokeNotes.add("Reset");
        
        //Does blend retro actively affect drawing?
        strokeNotes.add("Venn Diagram");
        strokeNotes.add("BlendMode=Green");
        strokeNotes.add("Reset"); //RESET DOESNT CLEAR?!
        
        //BlendModes in order
        strokeNotes.add("Beging Venn Diagram in each BlendMode");
        
        strokeNotes.add("BlendMode=Red");
        strokeNotes.add("Venn Diagram"); 
        strokeNotes.add("Reset");
        strokeNotes.add("BlendMode=Blue");
        strokeNotes.add("Venn Diagram"); 
        strokeNotes.add("Reset");
        strokeNotes.add("BlendMode=Green");
        strokeNotes.add("Venn Diagram"); 
        strokeNotes.add("Reset");

        strokeNotes.add("BlendMode=Add");
        strokeNotes.add("Venn Diagram"); 
        strokeNotes.add("Reset");
        strokeNotes.add("BlendMode=Multiply");
        strokeNotes.add("Venn Diagram"); 
        strokeNotes.add("Reset");
        strokeNotes.add("BlendMode=Difference");
        strokeNotes.add("Venn Diagram"); 
        strokeNotes.add("Reset");

        strokeNotes.add("BlendMode=Hard Light"); //everything under green=black
        strokeNotes.add("Venn Diagram"); 
        strokeNotes.add("Reset");
        //At some point the layering flips?! Why? 
        strokeNotes.add("BlendMode=Soft Light"); //everything below green interacts
        strokeNotes.add("Venn Diagram"); 
        strokeNotes.add("Reset");

        strokeNotes.add("BlendMode=Darken");
        strokeNotes.add("Venn Diagram"); 
        strokeNotes.add("Reset");
        strokeNotes.add("BlendMode=Lighten");
        strokeNotes.add("Venn Diagram"); 
        strokeNotes.add("Reset");

        strokeNotes.add("BlendMode=Source Atop");//?
        strokeNotes.add("Venn Diagram"); 
        strokeNotes.add("Reset");
        strokeNotes.add("BlendMode=Source Over");//?
        strokeNotes.add("Venn Diagram"); 
        strokeNotes.add("Reset");

        strokeNotes.add("BlendMode=Color Burn");
        strokeNotes.add("Venn Diagram"); 
        strokeNotes.add("Reset");
        strokeNotes.add("BlendMode=Color Dodge");
        strokeNotes.add("Venn Diagram"); 
        strokeNotes.add("Reset");


        strokeNotes.add("BlendMode=Exclusion");
        strokeNotes.add("Venn Diagram"); 
        strokeNotes.add("Reset");

        strokeNotes.add("BlendMode=Overlay");
        strokeNotes.add("Venn Diagram"); 
        strokeNotes.add("Reset");

        strokeNotes.add("BlendMode=Screen");
        strokeNotes.add("Venn Diagram"); 
        strokeNotes.add("Reset"); 


        this.changeButtonName(drawButton, strokeNotes.get(drawStroke)); 
        


        GraphicsContext gc=testPixelControl.getCanvas().getGraphicsContext2D();
        gc.setLineWidth(5);
        Font oldFont=gc.getFont();
        BlendMode oldBlendMode=gc.getGlobalBlendMode();
        double oldAlpha=gc.getGlobalAlpha(); 
        drawButton.setOnAction(new EventHandler<ActionEvent>(){
            @Override 
            public void handle(ActionEvent e){ 
                if (strokeNotes.size()<1){
                    return;
                } 
                System.out.printf("Button Val: %s\n", strokeNotes.get(drawStroke));
                 
                switch(strokeNotes.get(drawStroke)){ 
                    case "First Stroke":   
                        gc.beginPath();
                        gc.arc(400,400, 50, 25, 180, 300); 
                        gc.setFill(Color.GREEN);
                        gc.setStroke(Color.BLUE);
                        gc.stroke();
                        gc.fill(); 
                        gc.closePath();  

                        gc.setFill(Color.YELLOW);
                        gc.setStroke(Color.VIOLET);
                        gc.beginPath();
                        gc.fillArc(250,250, 100,100, 0, 180, ArcType.ROUND);
                        gc.strokeArc(250,250, 100,100, 0, 180, ArcType.ROUND);
                        gc.closePath();
                        gc.stroke(); //outline  
 
                        gc.setFill(Color.BLUE);
                        gc.setStroke(Color.RED);
                        gc.fillArc(250,250, 100,100, 90, 180, ArcType.ROUND);
                        gc.stroke(); //outline
                        gc.closePath();
                        //Start at 90 degrees with the 'draw line' rotating counter clockwise as degres work like that. Okay 

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

                        gc.beginPath();
                        gc.moveTo(50,50); //Quesiton where is the default coordinates of moveTo
                        gc.lineTo(100, 100); 
                        gc.closePath();
                        gc.stroke();
                         
                        gc.beginPath();
                        gc.setStroke(Color.BLUE);
                        gc.bezierCurveTo(100, 100, 150, 90, 175, 175); //NOTE stroke before close path to get just the curve.
                        gc.stroke();
                        gc.closePath(); 

                        gc.beginPath();
                        gc.moveTo(175, 175); //NOTE must always use a moveTo after a close path.
                        gc.setStroke(Color.RED);
                        gc.lineTo(500, 500); 
                        gc.closePath();
                        gc.stroke();
                        

                        break; 

                    case "bezierCurve": //works.
                        gc.setFill(Color.BLACK);
                        gc.setStroke(Color.RED);
                        gc.setLineWidth(5); 

                        gc.beginPath();
                        gc.moveTo(50,50);
                        //Bezier curve has 3 points associated. 
                        gc.bezierCurveTo(150, 20, 150, 150, 75, 150); 
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
                    
                    case "testOrderFillCloseStroke": //The difference between them is clear. 
                        //If fill before Stroke, stroke will be equal on both sides of the line boundry
                        //If stroke before fill, Fill will remove 1/2 of the stroke [like a stamp]

                        //If closePath before stroke, the Cord is not filled in, but rather just the arc itself.
                            //Why is this behavior resultant? 

                        //SFC
                        gc.beginPath();
                        gc.arc(50,50, 25, 25, 180, 300); 
                        gc.setFill(Color.GREEN);
                        gc.setStroke(Color.BLUE);
                        gc.stroke();
                        gc.fill(); 
                        gc.closePath();  
                        
                        //SCF 
                        gc.beginPath();
                        gc.arc(90,90, 25, 25, 180, 300); 
                        gc.setFill(Color.PURPLE);
                        gc.setStroke(Color.BLUE);   
                        gc.stroke();
                        gc.closePath();  
                        gc.fill();    

                        //FSC
                        gc.beginPath();
                        gc.arc(130,130, 25, 25, 180, 300); 
                        gc.setFill(Color.YELLOW);
                        gc.setStroke(Color.BLUE); 
                        gc.fill(); 
                        gc.stroke();
                        gc.closePath();  

                        //FCS 
                        gc.beginPath();
                        gc.arc(170,170, 25, 25, 180, 300); 
                        gc.setFill(Color.RED);
                        gc.setStroke(Color.BLUE); 
                        gc.fill();  
                        gc.closePath();  
                        gc.stroke(); 
                        
                        //CFS 
                        gc.beginPath();
                        gc.arc(210,210, 25, 25, 180, 300); 
                        gc.setFill(Color.ORANGE);
                        gc.setStroke(Color.BLUE); 
                        gc.closePath(); 
                        gc.fill();    
                        gc.stroke();

                        //CSF 
                        gc.beginPath();
                        gc.arc(250,250, 25, 25, 180, 300); 
                        gc.setFill(Color.GRAY);
                        gc.setStroke(Color.BLUE); 
                        gc.closePath();     
                        gc.stroke();
                        gc.fill(); 

                        break;

                    case "testArcFillStrokeVSArcFillArcFillStroke": 
                        //SFC
                        gc.beginPath();
                        gc.arc(50,50, 25, 25, 180, 300);  //Note apparently not affected by save() and restor()... Current path attribute used for any path methods saved in path attribute...?
                        gc.setFill(Color.GREEN);
                        gc.setStroke(Color.BLUE);
                        gc.stroke();
                        gc.fill(); 
                        gc.closePath();  
                        //MAJOR difference: Arc uses Radi, strokeArc uses Diameter. Other than that, they achieve similar affects, besides
                            //the Arc type
                        gc.beginPath(); 
                        gc.strokeArc(50,100, 25, 25, 180, 300, ArcType.ROUND); 
                        gc.fillArc(100,50, 25, 25, 180, 300,ArcType.ROUND);  
                        gc.strokeArc(100,100, 25, 25, 180, 300, ArcType.ROUND);  
                        gc.fillArc(100,100, 25, 25, 180, 300,ArcType.ROUND);  
                        gc.closePath();  
                        break;                        
                    case "testArcTypes": 
                        
                        gc.beginPath();  
                        gc.fillArc(50,50, 50, 50, 180, 300, ArcType.ROUND); 
                        gc.strokeArc(50,50, 50, 50, 180, 300, ArcType.ROUND);
                        //Test as it doesn't seem to be drawing... 
                        gc.closePath();  
                        
                        gc.beginPath();  
                        gc.fillArc(130,130, 50, 50, 180, 300, ArcType.CHORD); 
                        gc.strokeArc(130,130, 50, 50, 180, 300, ArcType.CHORD);  
                        gc.closePath();  
                        
                        gc.beginPath();  
                        gc.fillArc(210,210, 50, 50, 180, 300, ArcType.OPEN); 
                        gc.strokeArc(210,210, 50, 50, 180, 300, ArcType.OPEN);  
                        gc.closePath();  
                        break;

                    case "test5RectWith5StrokeLine": //Proves that stroke is indeed adding lineWidth to both sides of the infinitely precise line segment. Note however the top two seem to be 'blended' a bit, and are not pure colors either....?
                        //Note when drawing a line, the distance from point to point is important, and the width of the line, provides the two dimensions required. Thus the end of lines are not 'capped' by the pixel coloring/line width. Only to the left and right of a given line are widened.

                        //Next to one another    
                        gc.beginPath();
                        gc.setFill(Color.GREEN);
                        gc.rect(20,20, 10,10); //add path element to current path for rect. Cords trasnformed by current rasnformed as added, unaffected by subsequent changes. Not affected by save() and restore() operations(?)
                        gc.fill();
                        gc.closePath();

                        gc.setLineWidth(10);
                        gc.setStroke(Color.RED);
                        gc.beginPath();
                        gc.moveTo(30,25); //Note moveTo is lfiting the head up, lineto lowers the head.
                        gc.lineTo(40,25); //Assume infinitly precise line, middle of right of rect.
                        gc.closePath();
                        gc.stroke();

                        gc.setLineWidth(5);

                        //try 1 pixel over?
                        gc.beginPath();
                        gc.setFill(Color.GREEN);
                        gc.rect(20,40, 10,10); //add path element to current path for rect. Cords trasnformed by current rasnformed as added, unaffected by subsequent changes. Not affected by save() and restore() operations(?)
                        gc.fill();
                        gc.closePath();

                        gc.setLineWidth(10);
                        gc.setStroke(Color.RED);
                        gc.beginPath();
                        gc.moveTo(36,40); 
                        gc.lineTo(36,50); 
                        //gc.moveTo(31,45); //Note moveTo is lfiting the head up, lineto lowers the head.
                        //gc.lineTo(31,55); //Assume infinitly precise line, middle of right of rect.
                        gc.closePath();
                        gc.stroke();


                        gc.setLineWidth(5);

                        //try 1/2 pixel over?
                        gc.beginPath();
                        gc.setFill(Color.GREEN);
                        gc.rect(20,60, 10,10); //add path element to current path for rect. Cords trasnformed by current rasnformed as added, unaffected by subsequent changes. Not affected by save() and restore() operations(?)
                        gc.fill();
                        gc.closePath();

                        gc.setLineWidth(10);
                        gc.setStroke(Color.RED);
                        gc.beginPath();
                        gc.moveTo(35.5,60); //Note moveTo is lfiting the head up, lineto lowers the head.
                        gc.lineTo(35.5,70); //Assume infinitly precise line, middle of right of rect.
                        gc.closePath();
                        gc.stroke();


                        gc.setLineWidth(5);

                        //try 1.5 pixel over?
                        gc.beginPath();
                        gc.setFill(Color.GREEN);
                        gc.rect(20,80, 10,10); //add path element to current path for rect. Cords trasnformed by current rasnformed as added, unaffected by subsequent changes. Not affected by save() and restore() operations(?)
                        gc.fill();
                        gc.closePath();

                        gc.setLineWidth(10);
                        gc.setStroke(Color.RED);
                        gc.beginPath();
                        gc.moveTo(36.5,80); //Note moveTo is lfiting the head up, lineto lowers the head.
                        gc.lineTo(36.5,90); //Assume infinitly precise line, middle of right of rect.
                        gc.closePath();
                        gc.stroke();


                        gc.setLineWidth(5);

                        //Results: Next to: Darkened border, 1 over: visible gap, 1/2 over: lightened border, 1.5 over: visible gap.
                        //Assuming this might be a blend mode issue.


                        //TODO: Find out what the current ransform may be, and what the 'current path is a path attribute used for any of the path methods as speciifed in therendering attributes table and is not affected by sae and restore operations'.
                        break; 

                    case "Global Alpha Test1": //BlendMode will need more tests in a seperate file. Requires closer observation towards their effects.
                        gc.setStroke(Color.BLACK); 
                        double newAlpha=.5;   
                        
                        //Control
                        gc.setFill(Color.GREEN); 
                        gc.rect(10,10, 50,50); //x,y, w, h
                        gc.strokeRect(10, 10, 50,50);
                        gc.fillRect(10, 10, 50,50);
                        //Test Global Alpha  
                        gc.setGlobalAlpha(newAlpha); //NOTE Look at how the two sided border ends up showing.
                        gc.setFill(Color.GREEN);
                        gc.rect(85,85, 50,50);  
                        gc.strokeRect(85, 85, 50,50);
                        gc.fillRect(85, 85, 50,50);
                        //Line Cap 
                        //Line join,
                        //line cap
                        //miter limit
                        //clip
                        //font, text algin, text baseline, effect (italics, bold?), 
                        //Fill rule
                        break;

                    case "Alpha=50":
                        gc.setGlobalAlpha(.5);
                        break;
                    case "transforms": //STUCK
                    //NOTE: Do not use Affine directly, use Transform subcalss for scale, rotate, sheer, etc.
                        Transform oldTransform=gc.getTransform();
                        Transform scaleTransform = Transform.scale(2,2);
                        Transform rotateTransform =Transform.rotate(0,0,0);
                        Transform sheerTransform = Transform.shear(25,25);
                        Transform translateTransform = Transform.translate(10,10);
                        testPixelControl.getCanvas();
                        

                        Transform selectedTransform=scaleTransform;
                        gc.setTransform(selectedTransform.getMxx(), selectedTransform.getMyx(), selectedTransform.getMxy(), selectedTransform.getMyy(), selectedTransform.getTx(), selectedTransform.getTy());
                        
                        //Transform
                        
                        gc.setFill(Color.ORANGE);
                        gc.rect(130,130, 50,50);
                        break;

                    case "fontTests":
                        gc.setFill(Color.BLACK);
                        gc.setStroke(Color.GREEN); 
                        //Note: I believe the font size also includes the spacing between each line... The spacing, Is it set at 10 pixels, or is it size dependent?
                        Font test1 = Font.font("Castellar", FontWeight.NORMAL, 38);
                        gc.setFont(test1);
                        gc.fillText("Hello World 1!", 10,28); //Minimum distance to fit font? 
                        
                        Font test2 = Font.font("Castellar", FontWeight.NORMAL, 38);
                        gc.setFont(test2);
                        gc.fillText("Hello World 2!", 10,66);
                        gc.setFont(oldFont);
                        break;
                    case "fontTests2":
                        gc.setFill(Color.BLACK);
                        gc.setStroke(Color.GREEN);
                        
                        Font test3 = Font.font("Castellar", FontWeight.NORMAL, 28); 
                        //Previous thought: 10 is sapce up and down from the actual text. So 38 = 10 space, 18 pixel, 10 space below. 
                        //So this should have 10 space above, 10 below, and text size of 8. Or something like that.
                        //Nope that's not true.
                        //Is it a percentage of the text?
                        //10/38 ~25%. , 28-25% = 21
                        //YUP! 
                        //Is it font specific?
                        gc.setFont(test3);
                        gc.fillText("Hello World 3!", 10,21); //Minimum distance to fit font? 

                        Font test4 = Font.font("Castellar", FontWeight.NORMAL, 28);
                        gc.setFont(test4);
                        gc.fillText("Hello World 4!", 10,49);
                        gc.setFont(oldFont);
                        break;
                    case "fontTests3":
                        gc.setFill(Color.BLACK);
                        gc.setStroke(Color.GREEN);
                        
                        Font test5 = Font.font("Times New Roman", FontWeight.NORMAL, 28); 
                         
                        //Is it font specific?
                        //Likely. 
                        //The fillText takes the bottom left corner of the text to determine it's starting 'line'. The white space above and below each font doesn't seem to be controlled... The best bet is to just justify the Y axis each line by the font size... 
                        gc.setFont(test5);
                        gc.fillText("Hello World 5!", 10,28); //Minimum distance to fit font? 
                        break;
                    //Test rotation effects. Does it affect the entire canvas, or just the drawn items?
                    case "Rotate":
                        //Seems to be just a transform. Doesn't rotate the canvas, but the point's resultant line(s)/axies. Origin remains the same.
                        //Thus rotate May have some interesting behavior if the points were translated first to be centered. And if there were an optoin to mirror and such.
                        gc.rotate(45);
                        gc.setFill(Color.BLACK);
                        gc.rect(0,0,10,10);
                        gc.fill();
                        break;
                    case "DeRotateNeg":
                        gc.rotate(-45);
                        gc.setFill(Color.BLACK);
                        gc.rect(0,0,10,10);
                        gc.fill();
                        break;
                    case "DeRotatePos":
                        gc.rotate(315);
                        gc.setFill(Color.BLACK);
                        gc.rect(0,0,10,10);
                        gc.fill();
                        break;

                    //Blend Tests
                    case "BlendMode=Add": 
                        gc.setGlobalBlendMode(BlendMode.ADD);
                        break;
                    case "BlendMode=Blue":
                        gc.setGlobalBlendMode(BlendMode.BLUE);
                        break;
                    case "BlendMode=Color Burn":
                        gc.setGlobalBlendMode(BlendMode.COLOR_BURN);
                        break;
                    case "BlendMode=Color Dodge":
                        gc.setGlobalBlendMode(BlendMode.COLOR_DODGE);
                        break;
                    case "BlendMode=Darken":
                        gc.setGlobalBlendMode(BlendMode.DARKEN);
                        break;
                    case "BlendMode=Difference":
                        gc.setGlobalBlendMode(BlendMode.DIFFERENCE);
                        break;
                    case "BlendMode=Exclusion":
                        gc.setGlobalBlendMode(BlendMode.EXCLUSION);
                        break;
                    case "BlendMode=Green":
                        gc.setGlobalBlendMode(BlendMode.GREEN);
                        break;
                    case "BlendMode=Hard Light":
                        gc.setGlobalBlendMode(BlendMode.HARD_LIGHT);
                        break;
                    case "BlendMode=Lighten":
                        gc.setGlobalBlendMode(BlendMode.LIGHTEN);
                        break;
                    case "BlendMode=Multiply":
                        gc.setGlobalBlendMode(BlendMode.MULTIPLY);
                        break;
                    case "BlendMode=Overlay":
                        gc.setGlobalBlendMode(BlendMode.OVERLAY);
                        break;
                    case "BlendMode=Red":
                        gc.setGlobalBlendMode(BlendMode.RED);
                        break;
                    case "BlendMode=Screen":
                        gc.setGlobalBlendMode(BlendMode.SCREEN);
                        break;
                    case "BlendMode=Soft Light":
                        gc.setGlobalBlendMode(BlendMode.SOFT_LIGHT);
                        break;
                    case "BlendMode=Source Atop":
                        gc.setGlobalBlendMode(BlendMode.SRC_ATOP);
                        break;
                    case "BlendMode=Source Over":
                        gc.setGlobalBlendMode(BlendMode.SRC_OVER);
                        break;
                        
                    
                    case "Venn Diagram":
                        double initialX=75;
                        double initialY=75;
                        double w=50;
                        double h=50; 
                        Font drawOrder = Font.font("Times New Roman", FontWeight.NORMAL, 15);
                        gc.setFont(drawOrder); 
                        //Assuming the w/h are all the same...
                        //Venn diagram forms eq. triangle between origins.
                        double changeInY=Math.cos(30*(Math.PI/180))*h*.5;
                        double changeInX=Math.sin(30*(Math.PI/180))*w*.5;  

                        System.out.printf("change Y: %f, change X: %f\n",changeInY, changeInX);
                        
                        //Local method trick.
                        interface vennDiagram{
                            public void drawDiagram(GraphicsContext gc, Color color1, Color color2, Color color3, double w, double h, double initX, double initY, double changeInX, double changeInY);
                        }
                        vennDiagram fakeLocalMethod = new vennDiagram(){
                            @Override
                            public void drawDiagram(GraphicsContext gc, Color color1, Color color2, Color color3, double w, double h,  double initX, double initY, double changeInX, double changeInY){
                                //Bottom Layer, Top in Venn
                                gc.setFill(color1); 
                                gc.fillOval(initX,initY,w,h);
                                gc.fillText("1", initX, initY);
                                //Middle Layer, left Venndiagram
                                gc.setFill(color2);
                                gc.fillOval(initX-changeInX,initY+changeInY,w,h);
                                gc.fillText("2", initX-changeInX, initY+changeInY);
                                //Top Layer, right Venndiagram
                                gc.setFill(color3);
                                gc.fillOval(initX+changeInX,initY+changeInY,w,h);
                                gc.fillText("3", initX+changeInX+w, initY+changeInY);
                            };
                        };

                        //RGB
                            //Bottom Layer, Top in Venn
                            fakeLocalMethod.drawDiagram(gc, Color.RED, Color.GREEN, Color.BLUE, w, h, initialX, initialY, changeInX, changeInY); 
                            initialX+=w*2;
                            
                        //RBG
                            fakeLocalMethod.drawDiagram(gc, Color.RED, Color.BLUE, Color.GREEN, w, h, initialX, initialY, changeInX, changeInY); 
                            initialX-=w*2;
                            initialY+=h*2;
                            
                        //GRB
                            fakeLocalMethod.drawDiagram(gc, Color.GREEN, Color.RED, Color.BLUE, w, h, initialX, initialY, changeInX, changeInY);  
                            initialX+=w*2;
                        
                        //GBR
                            fakeLocalMethod.drawDiagram(gc, Color.GREEN, Color.BLUE, Color.RED, w, h, initialX, initialY, changeInX, changeInY);  
                            initialX-=w*2;
                            initialY+=h*2;    
                        
                        //BRG
                            fakeLocalMethod.drawDiagram(gc, Color.BLUE, Color.RED, Color.GREEN, w, h, initialX, initialY, changeInX, changeInY);  
                            initialX+=w*2; 
                        

                        //BGR
                            fakeLocalMethod.drawDiagram(gc, Color.BLUE, Color.GREEN, Color.RED, w, h, initialX, initialY, changeInX, changeInY);  
                            initialX-=w*2;
                            initialY+=h*2;
                            
                        gc.setFont(oldFont);
                        break;

                    case "Oval":
                        gc.setFill(Color.AQUA);
                        //gc.setStroke(Color.AZURE);
                        gc.fillOval(20,20,10,10);
                        //gc.strokeOval(20, 20, 10, 10);
                        gc.fillText("1", 20, 20-.5*10);
                            
                        break;
                    case "Save": //does not save canvas state, as expected
                        gc.save(); 
                        //Need a simple case to show the result of these attributes:
                            //Gloabl Alpha
                            //Global Blend Operation
                            //Transform
                            //Fill Paint
                            //StrokePaint
                            //LineWidth
                            //LineCap
                            //Line Join
                            //Miter Limit
                            //Clip
                            //font 
                            //text align
                            //text baseline
                            //effect
                            //fill rule.
                        break;
                    case "Restore": //does not restore canvas state
                        gc.restore();
                        break;
                    case "Reset":
                        double width=testPixelControl.getCanvas().getWidth(); 
                        double height=testPixelControl.getCanvas().getHeight();  
                        gc.setGlobalBlendMode(oldBlendMode);
                        gc.setGlobalAlpha(oldAlpha);
                        gc.setFont(oldFont);
                        gc.clearRect(0,0,width,height);
                        break;
                    default:
                        //getFillRule, getFont, getFontSMoothingType, getGlobalAlpha, 
                        //getGlobalBlendMode,  getLineCap, getLineDashes, getLineDashOffset(), getLineJoin(), 
                        //getLineWidth(), getMiterLimit(), getPixelWriter(),
                        //getStroke, getTextAlign, getTextBaseLine(), getTransform
                        //is point in path,
                        //QuadraticCruveTo(), rect(), rotate() scale(), setEffect(), setFillRule
                        //setLineJoin(strokeLineJoin), setLineDAshOffset, setlinedashes, setline cap
                        //setTransform, 
                        //transfrom, transslate,  LineJoin
                        System.out.println("Unrecognized test setup");
                        break;               
                
                }
 
                //TODO: Change blend modes and trhy test5rect with 5 stroke line.
                //TODO: Line text multi line, Line text spacing [make even even with different font sizes, so that the distance between two lines remains the same]
                //TODO: Line distance [so that the distance between two lines in a font change shrinks, i.e. smaller text between two larger text should be snug between the two, not 'floating' or having space more above or below to unreasonable degree],

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

//Text size does not edit the coordinates of the line. The line is also not evenly distirbuted around the coordinate Y axis....

//4/28/24
    /*
     * Current: Testing blends, have some questions which were likely revealed already by looking at how the blends work.
     * TODO: Effect testing, Custom Blends/Effects [bloom on 1 side of an edge], are blends able to be applied global and by shape [pretty sure using effects yes], effects testing via venn diagram, 
     * Today: Want to get started figuring out a UI for drawing shapes, without fully drawing it before the user is done. Must apply blends and effects while being drawn. 
     */