package baklanov.usermanagement.gui;
import baklanov.usermanagement.db.DaoFactory;
import baklanov.usermanagement.db.DaoFactoryImpl;
import junit.extensions.jfcunit.JFCTestCase;
import junit.extensions.jfcunit.JFCTestHelper;
import junit.extensions.jfcunit.eventdata.MouseEventData;
import junit.extensions.jfcunit.eventdata.StringEventData;
import junit.extensions.jfcunit.finder.NamedComponentFinder;

import javax.swing.*;
import java.awt.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class MainFrameTest extends JFCTestCase{

    private MainFrame mainFrame;

    protected void setUp() throws Exception{
        super.setUp();
        Properties properties=new Properties();
        properties.setProperty(
                "baklanov.usermanagement.db.UserDao",
                MockUserDao.class.getName());
        properties.setProperty("dao.factory", DaoFactoryImpl.class.getName());
        DaoFactory.getInstance().init(properties);
        setHelper(new JFCTestHelper());
        mainFrame=new MainFrame();
        mainFrame.setVisible(true);
    }

    protected void tearDown() throws Exception{
        mainFrame.setVisible(false);
        getHelper().cleanUp(this);
        super.tearDown();
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
        assertEquals("ID", table.getColumnName(0));
        assertEquals("Имя", table.getColumnName(1));
        assertEquals("Фамилия", table.getColumnName(2));
        find(JTable.class,"userTable");
        find(JButton.class,"addButton");
        find(JButton.class,"editButton");
        find(JButton.class,"deleteButton");
        find(JButton.class,"detailsButton");
    }

    public void testAddUser(){
        JTable table=(JTable) find(JTable.class,"userTable");
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
}
