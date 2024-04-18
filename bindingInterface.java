
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Node; 

interface bindingInterface{ //TODO: In the future use a sealed which permits a wrapper class, which is simply a Node/Region, or perhaps a interface that simply ensures getLayoutX/Y and min/max/pref Width/Height are defined
    
    /**
         * Takes an object with defined Properties and binds it to a binding operation. Implied that the parent nested the source of the binding operation values.
         *  @param parent: Node/Region subclass. To be bound by bindingOperation [presumably using a property of a child, or a constant] Hiding nasty expressions.
         *  @param prop: x, y, minW, maxW, prefW, minH, maxH, prefH.        
         *  @param bindingOperation: Binding.add(value1, value2), or another expression of the form Binding.math(value, [Binding operation]+).
         *  @return None, however in the future a true false would be nice for testing.
    */      
    abstract public boolean bindParent(bindingInterface parent, String prop, NumberBinding bindingOperation);
    //Implement array verisions, that allow for a single line to quickly look through and bind each expression. 
    /**
     * @param props takes an array with values ['x','y','minW','maxW','prefW','minH','maxH','prefH']
     * @param bindingOperations takes an array of NumberBinding operations, which correspond with one of the prop values.
     * @return will return an array of length = len(props), with values corresponding to each bind - True if bound success, false if bound unsuccessful. This allows reversing the operation if needed.
     * len(props)!=len(bindingOperations) returns [false]*len of props
     * Empty props returns empty array. Thus for fail condition look for either len 0 or false.
    */
    abstract public boolean[] bindParent(bindingInterface parent, String[] props, NumberBinding[] bindingOperations);
    abstract public boolean unbindParent(bindingInterface parent, String prop);
    abstract public boolean[] unbindParent(bindingInterface parent, String[] props);
    
    /**
         * Takes an object with defined Properties and binds it to a binding operation. Implied that the child is nested inside the parent at some point, and it will be the child's prop who is bound to some values supplied by a parent property.
         *  @param child: Node/Region subclass. To be bound by bindingOperation [presumably using a property of hte parent, or a constant] Hiding nasty expressions.
         *  @param prop: x, y, minW, maxW, prefW, minH, maxH, prefH.        
         *  @param bindingOperation: Binding.add(value1, value2), or another expression of the form Binding.math(value, [Binding operation]+).
         *  @return None, however in the future a true false would be nice for testing.
        
    */
    abstract public boolean bindChild(bindingInterface child, String prop, NumberBinding bindingOperation);
    abstract public boolean[] bindChild(bindingInterface child, String[] props, NumberBinding[] bindingOperations);
    abstract public boolean unbindChild(bindingInterface child, String prop);
    abstract public boolean[] unbindChild(bindingInterface child, String[] props);
    



    //Note for invalid bindings, we also want bidirectional bindings, and when any property is updated, vlaidate it? Perhaps? Or would the bidirectional impose the most restrictive bound?
        //Should be handled by Node and Region class parents, but if not, the user must define these! [probably]
    abstract public DoubleProperty layoutXProperty();
    abstract public DoubleProperty layoutYProperty();

    abstract public DoubleProperty minWidthProperty();
    abstract public DoubleProperty maxWidthProperty();
    abstract public DoubleProperty prefWidthProperty();
    
    abstract public DoubleProperty minHeightProperty();
    abstract public DoubleProperty maxHeightProperty();
    abstract public DoubleProperty prefHeightProperty();


    //Bidirectional - Saw some old 2019 posts about not being fully featured.
        //Also don't know if bidirectional supports multiple bindings. 
        //bindParent and child are convience functions, but should in a final release be replaced with the bind lines, and or the bind(ObservableList<> ObservableList<>) implementation in docs
    //If bidirectional tests work tomorrow, implement a decouple that is the unbindversion.


    /*
    Simply control for deciding how to take the binding statement, to the target's property.
    switch(prop): 
        case "x":
        case "y":
        case "minW":
        case "maxW":
        case "prefW":
        case "minH":
        case "maxH":
        case "prefH":
    */    
    
    /*
    
    childArg.widthProperty.bind(this.widthProperty /... +/-/*.../)

    //Secondary arugment must be observableValue<T>
        //observablevalue wraps a value acts like listner.
            //cahgne and invalidation events. Change is value has been changed. Invalidation generated if current value not valid.
            //Lazy evaluation value doesn't know if invalid really has changed until recomputed. Generating change events require eager evaluation while invalid can be generated for eager and lazy.
            //Generate as few events as possible.
    
    //Secondary argument, function desired.
    //Third argument, parent property to 'watch'.
    
    //So the goal is 
    //childArg.observableProperty.bind(func(parent.property))
        
    */


}
