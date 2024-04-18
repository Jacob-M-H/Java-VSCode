import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Main extends Application{
    public static void main(final String[] args){
        launch(args); 
         
    } 
    @Override
    public void start(Stage primaryStage) throws Exception {
        TestBindings tester=new TestBindings();
        AnchorPane required=new AnchorPane();
        Scene appScene=new Scene(required, 0, 0);
        primaryStage.setScene(appScene); 
        primaryStage.show();
    }
}
