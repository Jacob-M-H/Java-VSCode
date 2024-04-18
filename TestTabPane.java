import javafx.beans.binding.NumberBinding;
import javafx.geometry.Insets;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

//Goal is for additional functionalities to be tracked within this subclass. For example if I run into a feature Ireally want. Such as tab pane enforcement across all tabs.

//Note the Pane for addPane to Tab should be a subclass of Pane, with static properties that each Pane will look at for H/V Bars/zoom/LayoutX/Y. This allows all layers to remain in synch with each other.
public class TestTabPane extends TabPane implements bindingInterface{ 
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
        System.err.println("Add Pane to Tab 2");
        this.getTabs().get(idx).setContent(panel);
    }
    public void addPaneToTab(Pane panel){ 
        System.err.println("Add Pane to Tab 1");
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
    @Override
    public boolean bindParent(bindingInterface parent, String prop, NumberBinding bindingOperation) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'bindParent'");
    }
    @Override
    public boolean[] bindParent(bindingInterface parent, String[] props, NumberBinding[] bindingOperations) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'bindParent'");
    }
    @Override
    public boolean unbindParent(bindingInterface parent, String prop) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'unbindParent'");
    }
    @Override
    public boolean[] unbindParent(bindingInterface parent, String[] props) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'unbindParent'");
    }
    @Override
    public boolean bindChild(bindingInterface child, String prop, NumberBinding bindingOperation) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'bindChild'");
    }
    @Override
    public boolean[] bindChild(bindingInterface child, String[] props, NumberBinding[] bindingOperations) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'bindChild'");
    }
    @Override
    public boolean unbindChild(bindingInterface child, String prop) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'unbindChild'");
    }
    @Override
    public boolean[] unbindChild(bindingInterface child, String[] props) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'unbindChild'");
    }
    
//See implementation of + button and perhaps extend it to model 'locked', 'hidden', and 'grouped' tabs. [grouped tabs should treat all panes as a singular pane and simply store a way to reverse the grouping at the index of the merged tab.]


}
