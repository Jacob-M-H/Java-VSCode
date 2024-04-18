import java.lang.reflect.Array;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
//Toolkit java.awt may be required.
    
//JavaFX runtime components are missing and are requried to run this aplication. 
    //add VM options?
    //run, edit config, paste command in modify options->>add VM options
    //The path to the javafx sdk must be corrected where java fx sdk is. Lib of javafx-sdk, 
    //https://openjfx.io/openjfx-docs/
    //Launch.json add "vmArgs": "--module-path /Users/<user>/Downloads/javafx-sdk-11.0.2/lib --add-modules javafx.controls,javafx.fxml", isnide config of file want to use javafx in (obv change path to where javafx lib is).
    

public class JHMain extends Application{
    Stage window;
    Scene appScene;
    BorderPane root;
  
    //Eventually from a config file.
    private double defaultWidthMin=200;
    private double defaultWidthMax=500;
    private double defaultHeightMin=200;
    private double defaultHeightMax=500;

    private DoubleProperty minWidth= new SimpleDoubleProperty(defaultWidthMin);
    private DoubleProperty maxWidth= new SimpleDoubleProperty(defaultWidthMax); 
    private DoubleProperty minHeight= new SimpleDoubleProperty(defaultHeightMin);
    private DoubleProperty maxHeight= new SimpleDoubleProperty(defaultHeightMax);  
    
    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) throws Exception{
        //Set up Stage
        this.window=primaryStage;
        primaryStage.setTitle("Basic layout"); 
         
        Pane coloredPane=new Pane();
        BackgroundFill backgroundFill = new BackgroundFill(Color.valueOf("#ff00ff"), new CornerRadii(10), new Insets(10));
        coloredPane.setBackground(new Background(backgroundFill));
         
        this.root=new Root();

        TestTabPane tabRoot=new TestTabPane();
        tabRoot.addTab("example");
        tabRoot.addPaneToTab(0, coloredPane);
        this.root.setCenter(tabRoot);
        
      
  


        //Not happy with this, should default to full screen or something. :/ later.
        this.appScene=new Scene(root,minWidth.get(), minHeight.get());
        this.window.setScene(appScene); 
        this.window.show();


    }

    public void setStructure(){ 
        //Simply runs to ensure the min/max are still valid SystemSize true - NOTE: May need rigorous testing, instincts telling me unexpected behavior can occur.
            //Runs before others so there is values to be bound to.
        this.setMinBounds(this.defaultWidthMin,this.defaultHeightMin, true);
        this.setMaxBounds(this.defaultWidthMax,this.defaultHeightMax, true);
        
        String[] menuOptions={"File", "Options", "Help"};
        MenuBar menuBar = this.makeMenu(menuOptions);
        GridPane bottomRibbon =this.makeBottom();
        MiddleContainer Mid=new MiddleContainer(); //Need better name
        
        this.root = new BorderPane();
        this.root.setCenter(Mid);
        this.root.setTop(menuBar);
        this.root.setBottom(bottomRibbon);

       
    }


    //HERE, test if we can bind these values as expected.
    private DoubleProperty menuHeight=new SimpleDoubleProperty(40);
    private DoubleProperty bottomHeight=new SimpleDoubleProperty(20);
    private DoubleProperty constHeight=new SimpleDoubleProperty(0);
    


    public MenuBar makeMenu(String[] menuOptions){
        MenuBar menuRibbon=new MenuBar();
        //https://docs.oracle.com/javase/8/javafx/api/javafx/scene/control/ContextMenu.html
  
        for (String i : menuOptions){ 
            menuRibbon.getMenus().add(new Menu(i));
        }
        return menuRibbon;
    }

    public GridPane makeBottom(){
        return new GridPane();
    }





    //Application level min max bounds.
    public void setMinBounds(double wMin,double hMin, boolean systemSize){
        if (systemSize){
            Rectangle2D screenBounds=Screen.getPrimary().getBounds();
            wMin=screenBounds.getMinX(); 
            hMin=screenBounds.getMinY(); 
        }
        if (! verifyBounds(wMin, this.maxWidth.get(), hMin, this.maxHeight.get())){
            wMin=this.maxWidth.get();
            hMin=this.maxHeight.get();
        } 
        this.minWidth.set(wMin);
        this.minHeight.set(hMin); 
    }

    public void setMaxBounds(double wMax, double hMax, boolean systemSize){    
        if (systemSize){
            Rectangle2D screenBounds=Screen.getPrimary().getBounds();
            this.maxWidth.set(screenBounds.getMaxX()); 
            this.maxHeight.set(screenBounds.getMaxY());
        }
        if (! verifyBounds(this.minWidth.get(), wMax, this.minHeight.get(), hMax)){
            wMax=this.minWidth.get();
            hMax=this.minHeight.get(); 
        }
        this.maxWidth.set(wMax);
        this.maxHeight.set(hMax); 
    }

    public static boolean verifyBounds(double minWidth, double maxWidth, double minHeight, double maxHeight){
        if (minWidth>maxWidth){
            return false;
        }
        if (minHeight>maxHeight){
            return false;
        }
        return true;
    }







}