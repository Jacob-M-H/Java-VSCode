import javafx.geometry.Insets;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

//Goal is for additional functionalities to be tracked within this subclass. For example if I run into a feature Ireally want. Such as tab pane enforcement across all tabs.
public class TestTabPane extends TabPane{ 
    TabPane tp;
    TestTabPane(){
        System.err.println("Make new TestTabPane");  
    }
    public void addTab(String str){
        System.err.println("Add Tab");
        Tab newTab=new Tab(str);    
        this.getTabs().add(newTab);
    }
    public void addTab(String str, int idx){
        System.err.println("Add Tab");
        Tab newTab=new Tab(str);    
        this.getTabs().add(idx, newTab);
    }
    public void addPaneToTab(int idx, Pane panel){ 
        System.err.println("Add Pane to Tab");
        this.getTabs().get(idx).setContent(panel);
    }
    public void addPaneToTab(Pane panel){ 
        System.err.println("Add Pane to Tab");
        int end=this.getTabs().size()-1;
        this.getTabs().get(end).setContent(panel);
    }

    public void setPaneBackground(String colorHex){
        System.err.println("Set Tab container Pane Bckgd");
        this.setBackground(null);
        //May not work.
        this.setBackground(new Background(new BackgroundFill(Color.web(colorHex), CornerRadii.EMPTY, Insets.EMPTY)));
        /*
            String enteredByUser = "abcdef";
            yournode.setStyle("-fx-background-color: #" + enteredByUser); 
            //May work, mayhaps.    
        */
    
    }

    



}
