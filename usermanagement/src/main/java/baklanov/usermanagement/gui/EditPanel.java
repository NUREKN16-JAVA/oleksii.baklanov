package baklanov.usermanagement.gui;

import baklanov.usermanagement.User;
import baklanov.usermanagement.db.DatabaseException;
import baklanov.usermanagement.util.Messages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class EditPanel extends JPanel implements ActionListener {

    private final MainFrame parent;
    private JPanel buttonPanel;
    private JPanel fieldPanel;
    private JButton cancelButton;
    private JButton okButton;
    private JTextField dayOfBirthField;
    private JTextField lastNameField;
    private JTextField firstNameField;
    private Color bgColor= Color.WHITE;
    User user;

    public EditPanel(MainFrame parent){
        this.parent=parent;
        initialize();
    }

    private void initialize() {
        this.setName("editPanel");
        this.setLayout(new BorderLayout());
        this.add(getFieldPanel(),BorderLayout.NORTH);
        this.add(getButtonPanel(), BorderLayout.SOUTH);

    }

    private JPanel getButtonPanel() {
        if(buttonPanel==null){
            buttonPanel=new JPanel();
            buttonPanel.add(getOkButton(),null);
            buttonPanel.add(getCancelButton(),null);
        }
        return buttonPanel;
    }

    private JButton getCancelButton() {
        if (cancelButton == null){
            cancelButton = new JButton();
            cancelButton.setText(Messages.getString("Cancel"));
            cancelButton.setName("cancelButton");
            cancelButton.setActionCommand("cancel");
            cancelButton.addActionListener(this);
        }
        return cancelButton;
    }

    private JButton getOkButton() {
        if (okButton == null){
            okButton = new JButton();
            okButton.setText(Messages.getString("Edit"));
            okButton.setName("okButton");
            okButton.setActionCommand("ok");
            okButton.addActionListener(this);
        }
        return okButton;
    }

    private JPanel getFieldPanel() {
        if(fieldPanel==null){
            fieldPanel=new JPanel();
            fieldPanel.setLayout(new GridLayout(3,2));
            addLabeledField(fieldPanel, Messages.getString("Name"), getFirstNameField());
            addLabeledField(fieldPanel, Messages.getString("Surname"), getLastNameField());
            addLabeledField(fieldPanel, Messages.getString("Date of Birth"), getDateOfBirthField());
        }
        return fieldPanel;
    }

    private JTextField getDateOfBirthField() {
        if(dayOfBirthField==null){
            dayOfBirthField=new JTextField();
            dayOfBirthField.setName("dateOfBirthField");
        }
        return dayOfBirthField;
    }

    private JTextField getLastNameField() {
        if(lastNameField==null){
            lastNameField=new JTextField();
            lastNameField.setName("lastNameField");
        }
        return lastNameField;
    }

    private JTextField getFirstNameField() {
        if(firstNameField==null){
            firstNameField=new JTextField();
            firstNameField.setName("firstNameField");
        }
        return firstNameField;
    }

    private void addLabeledField(JPanel panel, String labelText, JTextField textField) {
        JLabel label=new JLabel(labelText);
        label.setLabelFor(textField);
        panel.add(label);
        panel.add(textField);
    }

    public void showEditPanel(User user) {
        this.user = user;
        getFirstNameField().setText(user.getFirstName());
        getLastNameField().setText(user.getLastName());
        DateFormat dateFormat = DateFormat.getDateInstance();
        getDateOfBirthField().setText(dateFormat.format(user.getDateOfBirth()));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if("ok".equalsIgnoreCase(e.getActionCommand())){
            user.setFirstName(getFirstNameField().getText());
            user.setLastName(getLastNameField().getText());

            try {
                user.setDateOfBirth(new SimpleDateFormat("dd-MM-yyyy").parse(getDateOfBirthField().getText()));
            } catch (ParseException e1) {
                getDateOfBirthField().setBackground(Color.RED);
                return;
            }
            try {
                parent.getDao().update(user);
            } catch (DatabaseException e1) {
                JOptionPane.showMessageDialog(this,e1.getMessage(),"Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
        clearFields();
        this.setVisible(false);
        parent.showBrowsePanel();
    }

    private void clearFields() {
        getFirstNameField().setText("");
        getFirstNameField().setBackground(bgColor);
        getLastNameField().setText("");
        getLastNameField().setBackground(bgColor);
        getDateOfBirthField().setText("");
        getDateOfBirthField().setBackground(bgColor);
    }
}
