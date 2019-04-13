package Client;

import javax.swing.JOptionPane;
/**
 *
 * @author gumula ronewa
 */
public class PopsUp extends JOptionPane{
    
    public int pop(String size, String name){
      @SuppressWarnings("LocalVariableHidesMemberVariable")
      String message;
      message = "Do you want to receive a "+size+" file from "+name+" ?";
      return showConfirmDialog(null, message , "Want To Accept File Transmission?", YES_NO_OPTION, QUESTION_MESSAGE);
    }

}
