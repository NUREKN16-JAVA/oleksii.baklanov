package baklanov.usermanagement.web;

import baklanov.usermanagement.User;
import org.junit.Test;

import java.text.DateFormat;

import java.util.Date;
import static org.junit.Assert.assertNotNull;


public class AddServletTest extends MockServletTestCase {
    private static final String FIRST_NAME_ETALONE="John";
    private static final String LAST_NAME_ETALONE="Doe";
    private static final long ETALONE_LONG_ID=1000;

    public void setUp() throws Exception {
        super.setUp();
        createServlet(AddServlet.class);
    }

    @Test
    public void testAdd(){
        Date date= new Date();
        User newUser = new User(FIRST_NAME_ETALONE, LAST_NAME_ETALONE, date);
        User user=new User(ETALONE_LONG_ID,FIRST_NAME_ETALONE,LAST_NAME_ETALONE,date);
        getMockUserDao().expectAndReturn("create",newUser, user);
        addRequestParameter("firstName", FIRST_NAME_ETALONE);
        addRequestParameter("lastName", LAST_NAME_ETALONE);
        addRequestParameter("date", DateFormat.getDateInstance().format(date));
        addRequestParameter("okButton", "Ok");
        doPost();
    }

    @Test
    public void testAddEmptyFirstName(){
        Date date= new Date();
        addRequestParameter("lastName", LAST_NAME_ETALONE);
        addRequestParameter("date", DateFormat.getDateInstance().format(date));
        addRequestParameter("okButton", "Ok");
        doPost();
        String errorMessage= (String) getWebMockObjectFactory().getMockRequest().getAttribute("error");
        assertNotNull("Could not find error message in session scope", errorMessage);
    }

    @Test
    public void testAddEmptyLastName(){
        Date date= new Date();
        addRequestParameter("firstName", FIRST_NAME_ETALONE);
        addRequestParameter("date", DateFormat.getDateInstance().format(date));
        addRequestParameter("okButton", "Ok");
        doPost();
        String errorMessage= (String) getWebMockObjectFactory().getMockRequest().getAttribute("error");
        assertNotNull("Could not find error message in session scope", errorMessage);
    }

    @Test
    public void testAddEmptyDate(){
        addRequestParameter("firstName", FIRST_NAME_ETALONE);
        addRequestParameter("lastName", LAST_NAME_ETALONE);
        addRequestParameter("okButton", "Ok");
        doPost();
        String errorMessage= (String) getWebMockObjectFactory().getMockRequest().getAttribute("error");
        assertNotNull("Could not find error message in session scope", errorMessage);
    }

    @Test
    public void testAddDateIncorrect(){
        addRequestParameter("firstName", FIRST_NAME_ETALONE);
        addRequestParameter("lastName", LAST_NAME_ETALONE);
        addRequestParameter("date", "asfsaferghe");
        addRequestParameter("okButton", "Ok");
        doPost();
        String errorMessage= (String) getWebMockObjectFactory().getMockRequest().getAttribute("error");
        assertNotNull("Could not find error message in session scope", errorMessage);
    }

}
