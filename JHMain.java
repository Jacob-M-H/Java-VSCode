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
        
        BorderPane root = new BorderPane();


        TestTabPane tabRoot=new TestTabPane();
        tabRoot.addTab("example");
        tabRoot.addPaneToTab(0, coloredPane);
        root.setCenter(tabRoot);

        final Menu menu1 = new Menu("File"); //https://docs.oracle.com/javase/8/javafx/api/javafx/scene/control/ContextMenu.html
        final Menu menu2 = new Menu("Options");
        final Menu menu3 = new Menu("Help");
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(menu1, menu2, menu3);
        root.setTop(menuBar);

        VBox tabTreeContainer=new VBox();
        root.setLeft(tabTreeContainer);
        VBox Extra=new VBox();
        root.setRight(Extra);
        AnchorPane bottomRibbon =new AnchorPane();
        root.setBottom(bottomRibbon);

        Pane C1=new Pane();
        C1.setBackground(new Background(backgroundFill));
        Pane C2=new Pane();
        C2.setBackground(new Background(backgroundFill));
        Pane C3=new Pane();
        C3.setBackground(new Background(backgroundFill));
        int bottomRibbonMinH=20;
        int tabTreeContainerMinW=20;
        int ExtraMinW=20;
        tabTreeContainer.getChildren().add(C1);
        tabTreeContainer.setMinWidth(tabTreeContainerMinW);
        tabTreeContainer.prefHeight(100);

        Extra.getChildren().add(C2);
        Extra.setMinWidth(ExtraMinW);
        Extra.prefHeight(100);

        bottomRibbon.getChildren().add(C3);
        bottomRibbon.minHeight(bottomRibbonMinH);
        bottomRibbon.prefWidth(100);
        Button temp=new Button("does this help?");
        C1.getChildren().add(temp);
        C1.setBackground(new Background(backgroundFill));

        
        appScene=new Scene(root,minWidth, minHeight);
        window.setScene(appScene); 
        window.show();


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