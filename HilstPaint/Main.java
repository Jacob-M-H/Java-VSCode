package HilstPaint;

import javax.swing.JPanel;

public class Main { 
    public static void main(String[] args){
        System.out.println("Initialize Hilst Paint Project.");
    
        HilstFrame mainFrame = new HilstFrame(); //create basic frame. 
        //Idea, have small matrices based on each shape, and then blip the whole matrice on to a 'parent' matrix. 
        //This maintains each things shape, and allows I think some fun stuff.
        mainFrame.setLayout(null); //I don't want things to mvoe relative tothe frame.
        //layered panes I'm not sure I want to use. 
 
 


    } 

    //Short term goals: Create screen, mouse and event and key listeners, menu bar, and think about layers and how we want to use them.
    //6/24/23: Find jPanel and look at how it draws/stores info. After that, we may make some temporary versions of drawing on a matrix, or storing values to draw a matrix.

    
  

}
