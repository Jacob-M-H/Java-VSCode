import javafx.beans.binding.NumberBinding;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

//Declare BindingInterface only extends those who are both a Node and region [for layout X/Y, and pref/min/max Height/width]
public class Root extends BorderPane implements bindingInterface{
    Root(){
        
    }

    
    //

    @Override
    public boolean bindChild(bindingInterface child, String prop, NumberBinding bindingOperation) { 
        switch(prop){
            case "x":
                if (child.layoutXProperty().isBound()){
                    return false;
                }
                child.layoutXProperty().bind(bindingOperation);
                break;
            case "y":
                if (child.layoutYProperty().isBound()){
                    return false;
                }
                child.layoutYProperty().bind(bindingOperation);
                break;
            case "minW":
                if (child.minWidthProperty().isBound()){
                    return false;
                }
                child.minWidthProperty().bind(bindingOperation);
                break;
            case "maxW":
                if (child.maxWidthProperty().isBound()){
                    return false;
                }
                child.maxWidthProperty().bind(bindingOperation);
                break;
            case "prefW":
                if (child.prefWidthProperty().isBound()){
                    return false;
                }
                child.prefWidthProperty().bind(bindingOperation);
                break;
            case "minH":
                if (child.minHeightProperty().isBound()){
                    return false;
                }
                child.minHeightProperty().bind(bindingOperation);
                break;
            case "maxH":
                if (child.maxHeightProperty().isBound()){
                    return false;
                }
                child.maxHeightProperty().bind(bindingOperation);
                break;
            case "prefH":
                if (child.prefHeightProperty().isBound()){
                    return false;
                }
                child.prefHeightProperty().bind(bindingOperation);
                break;
            default:
                throw new IllegalArgumentException("Expected property x|y|minW|maxW|prefW|minH|maxH|prefH");
        }
        return true;
    }
 
    @Override
    public boolean[] bindChild(bindingInterface child, String[] props, NumberBinding[] bindingOperations) {
        // TODO Auto-generated method stub
        int propLen=props.length;
        boolean[] resultArr=new boolean[propLen];    
        String errorMessage="";
        boolean throwError=false;

        if (propLen!=bindingOperations.length){//Array lengths mismatch
            for (int i=0; i<propLen; i++){
                resultArr[i]=false;
            }
            return resultArr;
        }

        for (int i=0; i<propLen; i++){
            try{
                resultArr[i]=this.bindChild(child, props[i], bindingOperations[i]);
            }
            catch(Exception e){
                resultArr[i]=false;
                errorMessage+=e.getMessage() + String.format(" at index %d\n", i); //Prompt user immediately
                throwError=true;
            }

        }
        if (throwError){
            for (int i=0; i<propLen; i++){
                if (resultArr[i]==true){
                    //TODO:
                    throw new UnsupportedOperationException("unbind operations! unbind the successful bindings");
                }
            }
            throw new IllegalArgumentException(errorMessage);
        }


        return resultArr;
    }
 
    @Override
    public boolean unbindChild(bindingInterface child, String prop) {
        switch(prop){
            case "x": 
                child.layoutXProperty().unbind();
                break;
            case "y": 
                child.layoutYProperty().unbind();
                break;
            case "minW": 
                child.minWidthProperty().unbind();
                break;
            case "maxW": 
                child.maxWidthProperty().unbind();
                break;
            case "prefW": 
                child.prefWidthProperty().unbind();
                break;
            case "minH": 
                child.minHeightProperty().unbind();
                break;
            case "maxH": 
                child.maxHeightProperty().unbind();
                break;
            case "prefH": 
                child.prefHeightProperty().unbind();
                break;
            default:
                return false; //Silent exception. Property doesn't exist. Should be used if property isn't bound, or if the unbind doesn't unbind anything.
        }
        return true;
    } 
    @Override
    public boolean[] unbindChild(bindingInterface child, String[] props) {
        int propLen=props.length;
        boolean[] resultArr=new boolean[propLen];
        for (int i=0; i<propLen; i++) {
            resultArr[i]=this.unbindChild(child, props[i]);
        }
        return resultArr;
    }



    @Override
    public boolean bindParent(bindingInterface parent, String prop, NumberBinding bindingOperation) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Root should not have a parent, thus 'bindParent' is undefined. Root should be bound to final(s) or a config file.");
    }
    @Override
    public boolean[] bindParent(bindingInterface parent, String[] props, NumberBinding[] bindingOperations) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Root should not have a parent, thus 'bindParent' is undefined. Root should be bound to final(s) or a config file.");
    }
    @Override
    public boolean unbindParent(bindingInterface parent, String prop) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Root should not have a parent, thus 'bindParent' is undefined. Root should be bound to final(s) or a config file.");
    }
    @Override
    public boolean[] unbindParent(bindingInterface parent, String[] props) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Root should not have a parent, thus 'bindParent' is undefined. Root should be bound to final(s) or a config file.");
    }
    


    



    
}
