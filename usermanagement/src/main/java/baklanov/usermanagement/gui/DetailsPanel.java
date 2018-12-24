package baklanov.usermanagement.gui;
import baklanov.usermanagement.User;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class DetailsPanel extends JPanel implements ActionListener {

    private MainFrame parent;
    private JPanel buttonPanel;
    private JPanel fieldPanel;
    private JButton okButton;
    private JTextField dateOfBirthField;
    private JTextField lastNameField;
    private JTextField firstNameField;

    public DetailsPanel(MainFrame frame) {
        this.parent = frame;
        initialize();
    }

    private void initialize() {
        this.setName("detailsPanel");
        this.setLayout(new BorderLayout());
        this.add(getFieldPanel(), BorderLayout.CENTER);
        this.add(getButtonPanel(), BorderLayout.SOUTH);
    }


    private JPanel getButtonPanel() {
        if (buttonPanel == null) {
            buttonPanel = new JPanel();
            buttonPanel.add(getOkButton(), null);

        }
        return buttonPanel;
    }


    private JButton getOkButton() {
        if (okButton == null) {
            okButton = new JButton();
            okButton.setText("Ok");
            okButton.setName("okButton");
            okButton.setActionCommand("ok");
            okButton.addActionListener(this);
        }
        return okButton;
    }

    private JPanel getFieldPanel() {
        if (fieldPanel == null) {
            fieldPanel = new JPanel();
            fieldPanel.setLayout(new GridLayout(3, 2));
            addLabeledField(fieldPanel, "First name",getFirstNameField());
            addLabeledField(fieldPanel, "Last name", getLastNameField());
            addLabeledField(fieldPanel, "Date of Birth", getDateOfBirthField());

        }
        return fieldPanel;
    }


    private void addLabeledField(JPanel panel, String labelText, JTextField textField) {
        JLabel label = new JLabel(labelText);
        textField.setEditable(false);
        label.setLabelFor(textField);
        panel.add(label);
        panel.add(textField);

    }

    private JTextField getFirstNameField() {
        if (firstNameField == null) {
            firstNameField = new JTextField();
            firstNameField.setName("firstNameField");
        }
        return firstNameField;
    }

    private JTextField getDateOfBirthField() {
        if (dateOfBirthField == null) {
            dateOfBirthField = new JTextField();
            dateOfBirthField.setName("dateOfBirthField");
        }
        return dateOfBirthField;
    }

    private JTextField getLastNameField() {
        if (lastNameField == null) {
            lastNameField = new JTextField();
            lastNameField.setName("lastNameField");
        }
        return lastNameField;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if ("ok".equalsIgnoreCase(e.getActionCommand())) {
            this.setVisible(false);
            parent.showBrowsePanel();

        }

    }

    public void showDetailsPanel(User user) {
        getFirstNameField().setText(user.getFirstName());
        getLastNameField().setText(user.getLastName());
        DateFormat dateFormat = DateFormat.getDateInstance();
        getDateOfBirthField().setText(dateFormat.format(user.getDateOfBirth()));
    }
}