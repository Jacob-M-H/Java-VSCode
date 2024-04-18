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
    

public class HBind extends Application{
    Stage window;
    Scene appScene;
    private double minWidth;
    private double minHeight;
    private double maxWidth;
    private double maxHeight;
  
    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) throws Exception{
        //Set up Stage
        window=primaryStage;
        primaryStage.setTitle("Basic layout");
        this.setMinBounds(200,200, false);
        this.setMaxBounds(500,500, false);
        
        /* 
        TabPane basicTabPane=new TabPane();
        Tab exampleTab=new Tab("example");
        basicTabPane.getTabs().add(exampleTab);
        */
        Pane coloredPane=new Pane();
        BackgroundFill backgroundFill = new BackgroundFill(Color.valueOf("#ff00ff"), new CornerRadii(10), new Insets(10));
        coloredPane.setBackground(new Background(backgroundFill));
        Pane C1=new Pane();
        C1.setBackground(new Background(backgroundFill));
        Pane C2=new Pane(); 
        C2.setBackground(new Background(backgroundFill));
        
        BorderPane root = new BorderPane();

 



        VBox tabTreeContainer=new VBox();
        TestTabPane tabRoot=new TestTabPane();
        VBox extra=new VBox();
    
        HBox tabCenter=new HBox();
        tabCenter.getChildren().add(tabTreeContainer); 
        tabCenter.getChildren().add(tabRoot);
        tabCenter.getChildren().add(extra);

        tabRoot.addTab("example");
        tabRoot.addPaneToTab(0, coloredPane);


        root.setCenter(tabCenter);
   
        
        
        
         
        int tabTreeContainerMinW=20;
        int ExtraMinW=20;  

        tabTreeContainer.getChildren().add(C1);
        tabTreeContainer.setMinWidth(tabTreeContainerMinW);
        tabTreeContainer.prefHeight(100);

        extra.getChildren().add(C2);
        extra.setMinWidth(ExtraMinW);
        extra.prefHeight(100);
 
        Button temp1=new Button("does this help?");
        C1.getChildren().add(temp1);
        Button temp2=new Button("does this help?");
        C2.getChildren().add(temp2);
        
        //The parent expands always to fit children, unless min/max set? or pref?

        appScene=new Scene(root,minWidth, minHeight);
        window.setScene(appScene); 
        window.show();


    }


    public void setBinds(Stage bindTo, Pane boundFeature){
        bindTo.getProperties(); //Assert window has properties
        bindTo.widthProperty();
        bindTo.heightProperty();

         


        boundFeature.minHeightProperty().bind(bindTo.heightProperty().subtract(0));
        boundFeature.maxHeightProperty().bind(bindTo.heightProperty().subtract(0));


    }

    public void setMinBounds(double wMin,double hMin, boolean systemSize){
        if (systemSize){
            Rectangle2D screenBounds=Screen.getPrimary().getBounds();
            wMin=screenBounds.getMinX(); 
            hMin=screenBounds.getMinY(); 
        }
        if (! verifyBounds(wMin, this.maxWidth, hMin, this.maxHeight)){
            wMin=this.maxWidth;
            hMin=this.maxHeight;
        } 
        this.minWidth=wMin;
        this.minHeight=hMin; 
    }

    public void setMaxBounds(double wMax, double hMax, boolean systemSize){    
        if (systemSize){
            Rectangle2D screenBounds=Screen.getPrimary().getBounds();
            this.maxWidth=screenBounds.getMaxX(); 
            this.maxHeight=screenBounds.getMaxY();
        }
        if (! verifyBounds(this.minWidth, wMax, this.minHeight, hMax)){
            wMax=this.minWidth;
            hMax=this.minHeight; 
        }
        this.maxWidth=wMax;
        this.maxHeight=hMax; 
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

/*

Set the Middle [VBox, TabPane, VBox] to have Height bound to Window+Top+Bottom H properties.
Set the Middle Width bound to Window W property

Set the TabPane W bound to W of Middle-Left/Right VBoxes.
Set the Middle H children pane's Propretie(s) to the Middle H.

Create container Middle: TabContainer, Extra
                TabContainer: Tab Tree VBox, TabPaneControler TabPAne
                -to track and represent closer to the required controls. Generalizable containers useful.
                -opt tied to directory?

Encapsulate Middle, reintroduce with top and bottom in other file
Encapsulate Top, bottom

test resizes

Begin track controls/UI for Tabs/Tab Tree.

Implement Resizeable scrollable region from earlier files into TabPane Pane's.

clean UI look of each component [Selectable regions should be colored better, focused region should be consistent, dark vs light theme [globals], flaggable UI text coolors. rounded/themed buttons or tabs.]

Pixel Control/Menu controls. [main butons/cursor selections]
Action Tree stack [maybe tied to the Pixel control/menu controls, ctlr z ctrl c etc]

File Save/open. Tab saving file extensions=images+action stack+layer values+layer position+tab names
Meta data access, pos on bottom ribbon.

'free' menu controls [still bound to functions that result in cahgnes, but some way to decouple its position for future UIs would be nice]

 */