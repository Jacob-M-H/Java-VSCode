import javafx.beans.binding.NumberBinding;
import javafx.scene.control.TabPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class TabLogicContainer extends HBox implements bindingInterface{
    TabLogicContainer(){
        VBox tabTree=new VBox();
        TestTabPane tabActual = new TestTabPane();
        this.getChildren().addAll(tabTree,tabActual);
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
 
}
