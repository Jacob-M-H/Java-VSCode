import java.util.ArrayList;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.*;

public class TabbedCanvas extends Pane{ 
    //Not statics, so each instance has own.
    private TabPane tabPane;
    private double zoomLevel;
    private double maxY;
    private double minY;
    private double maxX;
    private double minX; 
    private ArrayList<Tab> listOfTabs =new ArrayList<Tab>();

    TabbedCanvas(){
        this.tabPane=new TabPane();
    }
    public <T extends Tab> boolean addTab(T tab){
        boolean validate=true;//TODO: check tab against TabbedCanvas 
        if (!validate){
            return false;
        }

        this.listOfTabs.add(tab);

        return true;
    }

    public boolean lockTab(int idx){
        boolean validate=true;//TODO: check if valid index.
        if (!validate){
            return false;
        }
        this.listOfTabs.get(idx).setClosable(false);
        return true;
    }
    public boolean isLockedTab(int idx){
        boolean validate=true;//TODO: check if valid index.
        if (!validate){
            return false;
        }
        return this.listOfTabs.get(idx).isClosable(); 
    }
    public boolean renameTab(int idx, String str){
        boolean validate=true;//TODO: check if valid index.
        if (!validate){
            return false;
        }
        for (int i=0; i<this.listOfTabs.size(); i++){
            if (idx !=i){
                if (str==this.listOfTabs.get(i).getId()){ //TODO: Test if this is as expected with smaller example.
                    return false;
                }
            }
        }
        this.listOfTabs.get(idx).setId(str);
        return true;
    } 
    public <P extends Pane> boolean changeTabContent(P content, int idx){
        boolean validate=true;//TODO: check if valid index.
        if (!validate){
            return false;
        }
        //TODO: Opt check the type of Pane is what is expected.
        this.listOfTabs.get(idx).setContent(content);
        return true;
    }
    

}


//TODO: Create tab extension with a lock icon/button that sets is locked.
//TODO: opt max tabs 
//TODO: Hid tab button in context menu, keep it's order adn such, but if hidden it shouldn't be checked against when dragging.
//TODO: when more tabs than handle, arrow buttons on sides of tab ribbon, and or scrollable with wheel. When dragging those should scroll for user.
//TODO: make tabpane tabs dragable to reorder, update Array list when it is dropped [update visually when dragging to remove it, but should still be visible in cursor, and wherever it appears between tabs]
//TODO: Make canvas holder hold onto validation logic required, it will pass each item to cavas's to maintain all of them in line.
//TODO:Validation functions
//TODO: remove tab
//TODO: tab(s) content history(s) //will have ot wait until events of drawing and such are set in stone. But a stack root wouldn't be bad to implement.
//TODO: Pane must implement history ^
//TODO: Draw order must reflext tabs. So if the tabPane doesn't allow for render all tabs, we'll have to make our own control.
//https://forums.oracle.com/ords/apexds/post/how-to-make-tabpane-completely-transparent-1105

//Qs
//Can we have a default Background panel for tabPane without a tab?
//Can we set default color for tab ribbon?

//Let's make a simple tab objectand test those thigns
