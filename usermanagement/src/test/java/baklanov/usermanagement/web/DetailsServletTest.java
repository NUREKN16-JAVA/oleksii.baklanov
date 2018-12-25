package baklanov.usermanagement.web;

import baklanov.usermanagement.User;
import org.junit.Test;

import static org.junit.Assert.assertNull;

public class DetailsServletTest extends MockServletTestCase {
    private static final String USER_ETALONE="user";

    public void setUp() throws Exception {
        super.setUp();
        createServlet(DetailsServlet.class);
    }

    @Test
    public void testDetails(){
        addRequestParameter("okButton", "Ok");
        User user = (User) getWebMockObjectFactory().getMockSession().getAttribute(USER_ETALONE);
        assertNull("User wasn't deleted!", user);
    }

}
