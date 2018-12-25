package baklanov.usermanagement.gui;

import baklanov.usermanagement.db.DatabaseException;
import baklanov.usermanagement.util.Messages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class BrowsePanel extends JPanel implements ActionListener {
    private MainFrame parent;
    private JPanel buttonsPanel;
    private JScrollPane tablePanel;
    private JButton addButton;
    private JButton editButton;
    private JButton detailsButton;
    private JButton deleteButton;
    private JTable userTable;

    public BrowsePanel(MainFrame frame) {
        parent=frame;
        initialize();
    }

    private void initialize() {
        this.setName("browsePanel");
        this.setLayout(new BorderLayout());
        this.add(getTablePanel(),BorderLayout.CENTER);
        this.add(getButtonsPanel(),BorderLayout.SOUTH);
    }

    private JPanel getButtonsPanel() {
        if(buttonsPanel==null){
            buttonsPanel=new JPanel();
            buttonsPanel.add(getAddButton(),null);
            buttonsPanel.add(getEditButton(),null);
            buttonsPanel.add(getDeleteButton(),null);
            buttonsPanel.add(getDetailsButton(),null);
        }
        return buttonsPanel;
    }

    private JButton getDetailsButton() {
        if(detailsButton==null){
            detailsButton=new JButton();
            detailsButton.setText(Messages.getString("Details"));
            detailsButton.setName("detailsButton");
            detailsButton.setActionCommand("details");
            detailsButton.addActionListener(this);
        }
        return detailsButton;
    }

    private JButton getDeleteButton() {
        if(deleteButton==null){
            deleteButton=new JButton();
            deleteButton.setText(Messages.getString("Delete"));
            deleteButton.setName("deleteButton");
            deleteButton.setActionCommand("delete");
            deleteButton.addActionListener(this);
        }
        return deleteButton;
    }

    private JButton getEditButton() {
        if(editButton==null){
            editButton=new JButton();
            editButton.setText(Messages.getString("Edit"));
            editButton.setName("editButton");
            editButton.setActionCommand("edit");
            editButton.addActionListener(this);
        }
        return editButton;
    }

    private JButton getAddButton() {
        if(addButton==null){
            addButton=new JButton();
            addButton.setText(Messages.getString("Add"));
            addButton.setName("addButton");
            addButton.setActionCommand("add");
            addButton.addActionListener(this);
        }
        return addButton;
    }

    private JScrollPane getTablePanel() {
        if(tablePanel==null){
            tablePanel=new JScrollPane(getUserTable());
        }
        return tablePanel;
    }

    private JTable getUserTable() {
        if(userTable==null){
            userTable=new JTable();
            userTable.setName("userTable");
        }
        initTable();
        return userTable;
    }

    public void initTable() {
        UserTableModel model;
        try {
            model = new UserTableModel(parent.getDao().findAll());
        } catch (DatabaseException e) {
            model=new UserTableModel(new ArrayList());
            JOptionPane.showMessageDialog(this,e.getMessage(),"Error",
                    JOptionPane.ERROR_MESSAGE);
        }
        userTable.setModel(model);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String actionCommand=e.getActionCommand();
        if("add".equalsIgnoreCase(actionCommand)){
            this.setVisible(false);
            parent.showAddPanel();
        }
        else if("edit".equalsIgnoreCase(actionCommand)){
            Long id = (Long) userTable.getValueAt(userTable.getSelectedRow(), 0);
            this.setVisible(false);
            try{
                parent.showEditPanel(parent.getDao().find(id));
            }catch (DatabaseException e1){
                new Exception(e1.getMessage());
            }
        }
        else if("delete".equalsIgnoreCase(actionCommand)) { //$NON-NLS-1$
            Long id=(long) userTable.getValueAt(userTable.getSelectedRow(), 0);
            this.setVisible(false);
            try {
                parent.showDeletePanel(parent.getDao().find(id));
            } catch (DatabaseException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
        else if("details".equalsIgnoreCase(actionCommand)) { //$NON-NLS-1$
            Long id=(long) userTable.getValueAt(userTable.getSelectedRow(), 0);
            this.setVisible(false);
            try {
                parent.showDetailsPanel(parent.getDao().find(id));
            } catch (DatabaseException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
    }
}
