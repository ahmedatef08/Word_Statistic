package GUI;

import javax.swing.*;

public class FrameGUI extends JFrame{
    static JFrame Frame = new JFrame(); 
    ImageIcon icon = new ImageIcon(getClass().getResource("/Icon/icon.png"));
    static BottomPanel bottom;
    // Constructor of the GUI that add it's properties 
    public FrameGUI() {
        Frame.setTitle("Word Statistics");
        Frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Make the Frame Full Screen
        Frame.setIconImage(icon.getImage());
        
        TopPanel top = new TopPanel();
        bottom = new BottomPanel();
        Frame.setVisible(true);
    }
}