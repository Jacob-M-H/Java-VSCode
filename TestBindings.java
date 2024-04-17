
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;


public class TestBindings {
    
    private DoubleProperty menuHeight=new SimpleDoubleProperty(40);
    private DoubleProperty bottomHeight=new SimpleDoubleProperty(20);
    private DoubleProperty constHeight=new SimpleDoubleProperty(0);
    

    TestBindings(){
        this.testBindings();
    }   

    public void testBindings(){
        this.constHeight.bind(this.menuHeight.add(this.bottomHeight));
        printNew();//Base
        menuHeight.set(menuHeight.get()+10);
        printNew();//should change with menu
        bottomHeight.set(bottomHeight.get()+10);
        printNew();//should change with bottom
        //constHeight.set(constHeight.get()+10); //Not allowed! Okay! Makes sense.

        
    }

    public void printNew(){
        System.out.println("Pre format");
        String s1=String.format("Menu, Bottom, ConstHeight: %f | %f | %f", menuHeight.get(), bottomHeight.get(), constHeight.get());

        System.out.println("Post format");
        System.out.println(s1);    
    } 
}
