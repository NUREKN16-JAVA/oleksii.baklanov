package baklanov.usermanagement.agent;

import javax.swing.*;
import java.awt.*;

public abstract class Validate {

    public static String validate(JTextField field){
        if (field.getText().equals("")){
            field.setBackground(Color.RED);
            return null;
        }
        else{
            field.setBackground(Color.WHITE);
            return field.getText();
        }
    }
}
