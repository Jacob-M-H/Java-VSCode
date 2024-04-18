import javafx.beans.binding.NumberBinding;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class MiddleContainer extends HBox implements bindingInterface{
    MiddleContainer(){
        TabLogicContainer tabContainer = new TabLogicContainer();
        VBox extra = new VBox();
        this.getChildren().addAll(tabContainer, extra);
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