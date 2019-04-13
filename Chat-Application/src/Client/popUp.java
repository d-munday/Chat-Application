//package Client;

import javax.swing.JOptionPane;

/**
 *
 * @author gumula ronewa
 */
public class popUp {
    public static int pop(String name,String size){
      String message="Do you want to receive a "+size+" file from "+name+" ?";
      return JOptionPane.showConfirmDialog(null, message);
    }
    public static void main(String args[]){
     pop("Wynberg","23 mb");
    }
}
