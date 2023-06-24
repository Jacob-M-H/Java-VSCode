package HilstPaint;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class HilstFrame extends JFrame implements ActionListener, MouseListener{

    HilstFrame(){
    //JFrame frame = new JFrame();//creates a frame, the class itself is a  Jframe so no need.
    this.setTitle("Hilst Paint");
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //exit out of application, default is hide on close which runs in background, or do nothing on close.
    this.setResizable(true); //Frame resize will be allowed, default is true
    this.setSize(420,420);//in integers, temporary would like full screen
    this.setVisible(true);
    
    
    //Icon for this.
    ImageIcon frameIcon = new ImageIcon("C:\\Users\\thebl\\Java VSCode\\HilstPaint\\Hilst Paint Icon.png"); //creates an ImageIcon
    this.setIconImage(frameIcon.getImage()); //change icon of frame
    //FUTURE: Include a check if the Image icon is not found! try/catch/final must be used.
    this.getContentPane().setBackground(Color.white);
    //Change menu bar color :https://stackoverflow.com/questions/15648030/change-background-and-text-color-of-jmenubar-and-jmenu-objects-inside-it
    this.addMouseListener(this);
    //FUTURE: MouseListener, MouseMotionListner, and MouseWheel Event.
    }


    //each of these require a componenet to implement. Added to the frame itself for now.

    //Aciton/mouse/event listeners should interact with the backend later. 
    @Override
    public void mouseClicked(MouseEvent e) {
        // TODO Auto-generated method stub
        System.out.println("Mouse Clicked");
        //throw new UnsupportedOperationException("Unimplemented method 'mouseClicked'");
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub
        System.out.println("Mouse Pressed");
        //does not detect clicks in menu bar.
        //throw new UnsupportedOperationException("Unimplemented method 'mousePressed'");
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub
        System.out.println("Mouse Released");
        //FUTURE: (In window? - it can be detected when clicked and held in window then moved outo f it, note this activates if released outside ofthe window. May require some research to handle.)
        //throw new UnsupportedOperationException("Unimplemented method 'mouseReleased'");
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub
        System.out.println("Mouse Entered Window");
        //doesn't detect menu bar
        //throw new UnsupportedOperationException("Unimplemented method 'mouseEntered'");
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub
        System.out.println("Mouse Exited Window");
        //throw new UnsupportedOperationException("Unimplemented method 'mouseExited'");
    }


    //FUTURE: Experiment, action listener can it only respond to one check box, or multiple? Are each interactable element a different 'signal'? 
    //FUTURE: Experiment, a Panel with an action listener vs the frame itself. See if the above responds to multiple or one, and see if it's more useful to have panels lined with aciton listners for the GUI ribbon bar.
    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        System.out.println("Action Event detected");
        //throw new UnsupportedOperationException("Unimplemented method 'actionPerformed'");
    }
    

}
