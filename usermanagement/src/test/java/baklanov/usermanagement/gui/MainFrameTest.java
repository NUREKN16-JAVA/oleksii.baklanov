package baklanov.usermanagement.gui;
import baklanov.usermanagement.User;
import baklanov.usermanagement.db.DaoFactory;
import baklanov.usermanagement.db.MockDaoFactory;
import baklanov.usermanagement.util.Messages;
import com.mockobjects.dynamic.Mock;
import junit.extensions.jfcunit.JFCTestCase;
import junit.extensions.jfcunit.JFCTestHelper;
import junit.extensions.jfcunit.eventdata.MouseEventData;
import junit.extensions.jfcunit.eventdata.StringEventData;
import junit.extensions.jfcunit.finder.NamedComponentFinder;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

public class MainFrameTest extends JFCTestCase{

    private MainFrame mainFrame;
    private Mock mockUserDao;

    protected void setUp() throws Exception{
        super.setUp();
        Properties properties=new Properties();
        properties.setProperty("dao.factory", MockDaoFactory.class.getName());
        DaoFactory.getInstance().init(properties);
        mockUserDao = ((MockDaoFactory) DaoFactory.getInstance()).getMockUserDao();
        setHelper(new JFCTestHelper());
        mockUserDao.expectAndReturn("findAll", new ArrayList());
        mainFrame=new MainFrame();
        mainFrame.setVisible(true);
    }

    protected void tearDown() throws Exception{
        try {

            mockUserDao.verify();
            mainFrame.setVisible(false);
            getHelper().cleanUp(this);
            super.tearDown();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private Component find(Class componentClass, String name) {
        NamedComponentFinder finder;
        finder = new NamedComponentFinder(componentClass,name);
        finder.setWait(0);
        Component component=finder.find(mainFrame,0);
        assertNotNull("Could not find component '"+name+"'",component);
        return component;
    }

    public void testBrowseControls(){
        find(JPanel.class, "browsePanel");
        JTable table = (JTable) find(JTable.class, "userTable");
        assertEquals(3,table.getColumnCount());
        assertEquals(Messages.getString("Id"), table.getColumnName(0));
        assertEquals(Messages.getString("Name"), table.getColumnName(1));
        assertEquals(Messages.getString("Surname"), table.getColumnName(2));
        find(JTable.class,"userTable");
        find(JButton.class,"addButton");
        find(JButton.class,"editButton");
        find(JButton.class,"deleteButton");
        find(JButton.class,"detailsButton");
    }

    public void testAddUser(){
        String firstName="John";
        String lastName="Doe";
        Date now=new Date();
        User user=new User(firstName,lastName,now);
        User expectedUser = new User(new Long(1),firstName,lastName,now);
        mockUserDao.expectAndReturn("create", user, expectedUser);

        ArrayList users = new ArrayList();
        users.add(expectedUser);

        JTable table=(JTable) find(JTable.class,"userTable");
        mockUserDao.expectAndReturn("findAll", users);
        assertEquals(0,table.getRowCount());

        JButton addButton = (JButton) find(JButton.class,"addButton");
        getHelper().enterClickAndLeave(new MouseEventData(this,addButton));

        find(JPanel.class,"addPanel");

        JTextField firstNameField= (JTextField) find(JTextField.class,"firstNameField");
        JTextField lastNameField= (JTextField)find(JTextField.class,"lastNameField");
        JTextField dateOfBirthField= (JTextField)find(JTextField.class,"dateOfBirthField");
        JButton okButton = (JButton) find(JButton.class,"okButton");
        find(JButton.class,"cancelButton");

        getHelper().sendString(new StringEventData(this,firstNameField,"John"));
        getHelper().sendString(new StringEventData(this,lastNameField,"Doe"));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String myDate=dateFormat.format(new Date());

        getHelper().sendString(new StringEventData(this,dateOfBirthField,myDate));

        getHelper().enterClickAndLeave(new MouseEventData(this,okButton));

        find(JPanel.class,"browsePanel");
        table=(JTable) find(JTable.class,"userTable");
        assertEquals(1,table.getRowCount());
    }

    public void testEditUser() {
        JTable table=(JTable) find(JTable.class,"userTable");
        testAddUser();
        table.setRowSelectionInterval(0, 0);
        assertEquals(1,table.getRowCount());
        JButton editButton = (JButton) find(JButton.class,"editButton");
        getHelper().enterClickAndLeave(new MouseEventData(this, editButton));
        find(JPanel.class,"editPanel");

        JTextField firstNameField= (JTextField) find(JTextField.class,"firstNameField");
        JTextField lastNameField= (JTextField)find(JTextField.class,"lastNameField");
        JTextField dateOfBirthField= (JTextField)find(JTextField.class,"dateOfBirthField");
        JButton okButton = (JButton) find(JButton.class,"okButton");
        firstNameField.setText("");
        lastNameField.setText("");
        dateOfBirthField.setText("");
        find(JButton.class,"cancelButton");
        getHelper().sendString(new StringEventData(this,firstNameField,"NotJohn"));
        getHelper().sendString(new StringEventData(this,lastNameField,"NotDoe"));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String myDate=dateFormat.format(new Date());

        getHelper().sendString(new StringEventData(this,dateOfBirthField,myDate));

        int x=table.getRowCount();
        getHelper().enterClickAndLeave(new MouseEventData(this,okButton));

        find(JPanel.class,"browsePanel");
        table=(JTable) find(JTable.class,"userTable");
        assertEquals(x,table.getRowCount());
    }

    public void testDeleteUser() {
        JTable table = (JTable) find(JTable.class, "userTable");
        testAddUser();
        table.setRowSelectionInterval(0, 0);
        JButton deleteButton = (JButton) find(JButton.class, "deleteButton");
        getHelper().enterClickAndLeave(new MouseEventData(this, deleteButton));
        find(JPanel.class,"deletePanel");
        JButton okButtonDelete = (JButton) find(JButton.class, "okButton");
        getHelper().enterClickAndLeave(new MouseEventData(this, okButtonDelete));
        assertEquals(0, table.getRowCount());
        find(JPanel.class, "browsePanel");
    }

    public void testDetails() {
        JTable table = (JTable) find(JTable.class, "userTable");
        testAddUser();
        table.setRowSelectionInterval(0, 0);
        JButton detailsButton = (JButton) find(JButton.class, "detailsButton");
        getHelper().enterClickAndLeave(new MouseEventData(this, detailsButton));
        find(JPanel.class,"detailsPanel");
        JTextField firstNameFieldDetails = (JTextField) find(JTextField.class, "firstNameField");
        JTextField lastNameFieldDetails = (JTextField) find(JTextField.class, "lastNameField");
        JTextField dateOfBirthFieldDetails = (JTextField) find(JTextField.class, "dateOfBirthField");
        assertNotNull(firstNameFieldDetails.getText());
        assertNotNull(lastNameFieldDetails.getText());
        assertNotNull(dateOfBirthFieldDetails.getText());
        JButton okButtonDetails = (JButton) find(JButton.class, "okButton");
        getHelper().enterClickAndLeave(new MouseEventData(this, okButtonDetails));
        find(JPanel.class,"browsePanel");
    }

    public void testCancelAddUser() {
        String firstName = "John";
        String lastName = "Doe";
        Date date = new Date();
        JTable table = (JTable) find(JTable.class, "userTable");
        assertEquals(0, table.getRowCount());
        JButton addButton = (JButton) find(JButton.class, "addButton");
        getHelper().enterClickAndLeave(new MouseEventData(this, addButton));
        find(JPanel.class,"addPanel");
        JTextField firstNameField = (JTextField) find(JTextField.class, "firstNameField");
        JTextField lastNameField = (JTextField) find(JTextField.class, "lastNameField");
        JTextField dateOfBirthField = (JTextField) find(JTextField.class, "dateOfBirthField");
        find(JButton.class, "okButton");
        JButton cancelButton = (JButton) find(JButton.class, "cancelButton");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateInString=dateFormat.format(date);
        getHelper().sendString(new StringEventData(this, firstNameField, firstName));
        getHelper().sendString(new StringEventData(this, lastNameField, lastName));
        getHelper().sendString(new StringEventData(this, dateOfBirthField, dateInString));
        getHelper().enterClickAndLeave(new MouseEventData(this, cancelButton));
        find(JPanel.class, "browsePanel");
        assertEquals(0, table.getRowCount());
    }

    public void testCancelDeleteUser() {
        String firstName = "John";
        String lastName = "Doe";
        Date now = new Date();
        JTable table = (JTable) find(JTable.class, "userTable");
        JButton addButton = (JButton) find(JButton.class, "addButton");
        getHelper().enterClickAndLeave(new MouseEventData(this, addButton));
        find(JPanel.class,"addPanel");
        JTextField firstNameField = (JTextField) find(JTextField.class, "firstNameField");
        JTextField lastNameField = (JTextField) find(JTextField.class, "lastNameField");
        JTextField dateOfBirthField = (JTextField) find(JTextField.class, "dateOfBirthField");
        JButton okButton = (JButton) find(JButton.class, "okButton");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date=dateFormat.format(now);
        getHelper().sendString(new StringEventData(this, firstNameField, firstName));
        getHelper().sendString(new StringEventData(this, lastNameField, lastName));
        getHelper().sendString(new StringEventData(this, dateOfBirthField, date));
        getHelper().enterClickAndLeave(new MouseEventData(this, okButton));
        assertEquals(1, table.getRowCount());
        find(JPanel.class, "browsePanel");
        table.setRowSelectionInterval(0, 0);
        JButton deleteButton = (JButton) find(JButton.class, "deleteButton");
        getHelper().enterClickAndLeave(new MouseEventData(this, deleteButton));
        find(JPanel.class,"deletePanel");
        JButton cancelButtonDelete = (JButton) find(JButton.class, "cancelButton");
        getHelper().enterClickAndLeave(new MouseEventData(this, cancelButtonDelete));
        assertEquals(1, table.getRowCount());
        find(JPanel.class, "browsePanel");
    }

    public void testCancelEditUser() {
        String firstName = "NotJohn";
        String lastName = "NotDoe";
        Date date = new Date();
        JTable table = (JTable) find(JTable.class, "userTable");
        testAddUser();
        table.setRowSelectionInterval(0, 0);
        JButton editButton = (JButton) find(JButton.class, "editButton");
        getHelper().enterClickAndLeave(new MouseEventData(this, editButton));
        find(JPanel.class,"editPanel");
        JTextField firstNameFieldEdit = (JTextField) find(JTextField.class, "firstNameField");
        JTextField lastNameFieldEdit = (JTextField) find(JTextField.class, "lastNameField");
        JTextField dateOfBirthFieldEdit = (JTextField) find(JTextField.class, "dateOfBirthField");
        firstNameFieldEdit.setText("");
        lastNameFieldEdit.setText("");
        dateOfBirthFieldEdit.setText("");
        JButton  cancelButtonCancelEdit = (JButton) find(JButton.class, "cancelButton");
        SimpleDateFormat dateFormatEdit = new SimpleDateFormat("yyyy-MM-dd");
        String dateEdit=dateFormatEdit.format(date);
        getHelper().sendString(new StringEventData(this, firstNameFieldEdit, firstName));
        getHelper().sendString(new StringEventData(this, lastNameFieldEdit, lastName));
        getHelper().sendString(new StringEventData(this, dateOfBirthFieldEdit, dateEdit));
        getHelper().enterClickAndLeave(new MouseEventData(this, cancelButtonCancelEdit));
        assertEquals(table.getModel().getValueAt(0, 1).toString(),"John");
        assertEquals(table.getModel().getValueAt(0, 2).toString(),"Doe");
        find(JPanel.class, "browsePanel");
        assertEquals(1, table.getRowCount());
    }

}
