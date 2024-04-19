
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;


public class TestBindings {
    
    private DoubleProperty menuHeight=new SimpleDoubleProperty(40);
    private DoubleProperty bottomHeight=new SimpleDoubleProperty(20);
    private DoubleProperty constHeight=new SimpleDoubleProperty(0);
    private DoubleProperty x=new SimpleDoubleProperty();
    private DoubleProperty y= new SimpleDoubleProperty();

    private DoubleProperty A=new SimpleDoubleProperty();
    private DoubleProperty B=new SimpleDoubleProperty();
    //private double settingNumber=20;//Bound values cannot be changed. 
    private DoubleProperty settingNumber=new SimpleDoubleProperty(20);
    /*
     * TAKEAWAYS:
     *  Bound values when using constants, note that they cannot change. Thus use keyword FINAL, or other observable values when binding.
     *  All binds prevent an additional bind from being constructed.
     *  There is some danger [probably] of a cyclic bind causing unexpected behavior.
     */
    
    TestBindings(){
        //this.testBindings();
        //this.testBidirectional();
        this.testBiBondPure();
    }   

    public void testBindings(){
        this.constHeight.bind(this.menuHeight.add(this.bottomHeight));
        printNew();//Base
        menuHeight.set(menuHeight.get()+10);
        printNew();//should change with menu
        bottomHeight.set(bottomHeight.get()+10);
        printNew();//should change with bottom
        //constHeight.set(constHeight.get()+10); //Not allowed! Okay! Makes sense.

        this.y.set(10);
        this.bindMe(Bindings.add(y,10));
        System.out.printf("x: %f | y: %f", this.x.get(), this.y.get());
        this.y.set(this.y.get()+10);
        System.out.printf("x: %f | y: %f", this.x.get(), this.y.get());
        
    }
    public void bindMe(NumberBinding func){
        this.x.bind(func);
    }

    public void printNew(){
        System.out.println("Pre format");
        String s1=String.format("Menu, Bottom, ConstHeight: %f | %f | %f", menuHeight.get(), bottomHeight.get(), constHeight.get());

        System.out.println("Post format");
        System.out.println(s1);    
    }
    

    public void printBi(){
        System.out.printf("A:%f | B:%f | setting:%f\n", A.getValue(), B.getValue(), settingNumber.getValue());
        //Fail
            //System.out.printf("A:%f | B:%f | setting:%f\n", A.getValue(), B.getValue(), settingNumber);
    }

    public void testBiBondPure(){
        A.unbind();
        B.unbind();
        settingNumber.unbind();
        settingNumber.setValue(20);
        B.setValue(50);
        A.setValue(0);
        //Bindings.createDoubleBinding(()->B.getValue()+settingNumber.getValue(), B, settingNumber); //with just Bindings.createDoubleBinding() they aren't 'bound'... :/?
            //binding to self also get's infinte loop. 
        Bindings.bindBidirectional(B, settingNumber);
        printBi(); //0, 50, 20
        settingNumber.setValue(settingNumber.getValue()+10);
        printBi();//0, 60, 30
        B.setValue(B.getValue()+10);
        printBi(); //0, 70, 40




        //https://www.reddit.com/r/javahelp/comments/41a4q7/javafx_how_to_create_a_bidirectional_bind_between/
        //There seems to be a lack of literature on the biDirectional, but this seems like a route, just would have to make implements functions for them. I'll try my darndest first though to figure this out using the library.
        
        
    }
    public void testBidirectional(){  //Note, pretty sure this is a uni-bind, bound to a bi-bind.
        B.setValue(50);
        //A.bind(Bindings.createDoubleBinding(()->B.add(settingNumber).getValue(), B)); //failure in case settingNumber is non observable.
        A.bind(Bindings.createDoubleBinding(()->B.getValue()+settingNumber.getValue(), B, settingNumber)); //Mysterious.
        printBi();    
        //B+10, A=50+20+10=70
        System.out.println("is B bound?");
        B.setValue(B.getValue()+10);
        printBi();    
        try{
            //A+10 70+10, B+10=80, 
            System.out.println("is A bound?");
            A.setValue(A.getValue()+10);
            printBi();    
            //FAIL
                //settingNumber+10 
                //printBi();       
                System.out.println("Try changing setting");
                settingNumber.setValue(30);
                printBi();
        
        }
        catch(Exception e) {
            System.out.println("Yup, not happy trying to set it!");
            System.out.println("Try changing setting");
            //Setting+10, 30+B, 30+60=90. 
            settingNumber.setValue(30);
            printBi();

        }

        


    /*KEEP - note on a different binding property.
    https://stackoverflow.com/questions/63272038/how-to-apply-bidirectional-method-when-doing-calculation-number-between-integer
     StringConverter<Number> converter = new NumberStringConverter();
    StringConverter<Double> toDouble = new DoubleStringConverter();
    
    TextFormatter<Number> quantity = new TextFormatter<>(converter);
    TextFormatter<Double> price = new TextFormatter<>(toDouble);
    TextFormatter<Double> total = new TextFormatter<>(toDouble);

    Quantity.setTextFormatter(quantity);
    Price.setTextFormatter(price);
    Total.setTextFormatter(total);
    
    total.valueProperty().bindBidirectional(Bindings.createObjectBinding(() -> {
        
        // ?????????????????????????????????????????
        
    }, quantity.valueProperty(), price.valueProperty()));
    

    total.valueProperty().bind(Bindings.createObjectBinding(
    () -> quantity.getValue() * price.getValue(),
    quantity.valueProperty(), price.valueProperty()));
    */
    }


    //TODO: Test bidirectional bindings vs binding. Bidirectional bindings may be useful to lock items to certain scale, while bindings may be nice to move bounds as required.
    //A double bind isn't a terrible idea either, but there is testing that need to be done to evaluate the behavior.
}
