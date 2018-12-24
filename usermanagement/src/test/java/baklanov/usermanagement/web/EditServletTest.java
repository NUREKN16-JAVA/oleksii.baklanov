package baklanov.usermanagement.web;

import baklanov.usermanagement.User;
import org.junit.Before;
import org.junit.Test;

import java.text.DateFormat;
import java.util.Date;

import static org.junit.Assert.*;

public class EditServletTest extends MockServletTestCase {
    private static final String FIRST_NAME_ETALONE="John";
    private static final String LAST_NAME_ETALONE="Doe";
    private static final long ETALONE_LONG_ID=1000;
    @Before
    public void setUp() throws Exception {
        super.setUp();
        createServlet(EditServlet.class);
    }

    @Test
    public void testEdit(){
        Date date= new Date();
        User user=new User(ETALONE_LONG_ID,FIRST_NAME_ETALONE,LAST_NAME_ETALONE,date);
        getMockUserDao().expect("update", user);
        addRequestParameter("id", "1000");
        addRequestParameter("firstName", FIRST_NAME_ETALONE);
        addRequestParameter("lastName", LAST_NAME_ETALONE);
        addRequestParameter("date", DateFormat.getDateInstance().format(date));
        addRequestParameter("okButton", "Ok");
        doPost();
    }

    @Test
    public void testEditEmptyFirstName(){
        Date date= new Date();
        addRequestParameter("id", "1000");
        addRequestParameter("lastName", LAST_NAME_ETALONE);
        addRequestParameter("date", DateFormat.getDateInstance().format(date));
        addRequestParameter("okButton", "Ok");
        doPost();
        String errorMessage= (String) getWebMockObjectFactory().getMockRequest().getAttribute("error");
        assertNotNull("Could not find error message in session scope", errorMessage);
    }

    @Test
    public void testEditEmptyLastName(){
        Date date= new Date();
        addRequestParameter("id", "1000");
        addRequestParameter("firstName", FIRST_NAME_ETALONE);
        addRequestParameter("date", DateFormat.getDateInstance().format(date));
        addRequestParameter("okButton", "Ok");
        doPost();
        String errorMessage= (String) getWebMockObjectFactory().getMockRequest().getAttribute("error");
        assertNotNull("Could not find error message in session scope", errorMessage);
    }

    @Test
    public void testEditEmptyDate(){
        addRequestParameter("id", "1000");
        addRequestParameter("firstName", FIRST_NAME_ETALONE);
        addRequestParameter("lastName", LAST_NAME_ETALONE);
        addRequestParameter("okButton", "Ok");
        doPost();
        String errorMessage= (String) getWebMockObjectFactory().getMockRequest().getAttribute("error");
        assertNotNull("Could not find error message in session scope", errorMessage);
    }

    @Test
    public void testEditDateIncorrect(){
        addRequestParameter("id", "1000");
        addRequestParameter("firstName", FIRST_NAME_ETALONE);
        addRequestParameter("lastName", LAST_NAME_ETALONE);
        addRequestParameter("date", "asfsaferghe");
        addRequestParameter("okButton", "Ok");
        doPost();
        String errorMessage= (String) getWebMockObjectFactory().getMockRequest().getAttribute("error");
        assertNotNull("Could not find error message in session scope", errorMessage);
    }
}