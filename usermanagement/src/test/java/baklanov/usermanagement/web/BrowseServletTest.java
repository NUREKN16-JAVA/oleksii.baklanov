package baklanov.usermanagement.web;

import baklanov.usermanagement.User;
import org.junit.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

public class BrowseServletTest extends MockServletTestCase {
    private static final String FIRST_NAME_ETALONE="John";
    private static final String LAST_NAME_ETALONE="Doe";
    private static final long ETALONE_LONG_ID=1000;

    public void setUp() throws Exception {
        super.setUp();
        createServlet(BrowseServlet.class);
    }

    @Test
    public void testBrowse(){
        User user=new User(ETALONE_LONG_ID,FIRST_NAME_ETALONE,LAST_NAME_ETALONE, new Date());
        List list = Collections.singletonList(user);
        getMockUserDao().expectAndReturn("findAll", list);
        doGet();
        Collection<User> collection = (Collection) getWebMockObjectFactory()
                .getMockSession()
                .getAttribute("users");
        assertNotNull("Could not find list of users in session",collection);
        assertSame(list,collection);
    }

    @Test
    public void testEdit(){
        User user=new User(ETALONE_LONG_ID,FIRST_NAME_ETALONE,LAST_NAME_ETALONE, new Date());

        addRequestParameter("editButton", "Edit");
        addRequestParameter("id", "1000");
        getMockUserDao().expectAndReturn("find", new Long(1000), user);
        doPost();
        User userInSession = (User) getWebMockObjectFactory().getMockSession().getAttribute("user");
        assertNotNull("Could not find user in session", user);
        assertSame(user, userInSession);
    }
}
