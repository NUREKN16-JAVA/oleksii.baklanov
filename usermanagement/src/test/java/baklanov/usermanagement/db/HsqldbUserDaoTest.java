package baklanov.usermanagement.db;

import baklanov.usermanagement.User;
import org.dbunit.DatabaseTestCase;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.XmlDataSet;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import java.util.Collection;
import java.util.Date;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class HsqldbUserDaoTest extends DatabaseTestCase {
    private HsqldbUserDao dao;
    private ConnectionFactory connectionFactory;
    private DaoFactory daoFactory;
    private static final long ETALON_ID_USER = 1;
    private static final String ETALON_FIRSTNAME_USER = "Test";
    private static final String ETALON_LASTNAME_USER = "Update";

    @Before
    public void setUp()  {
        daoFactory=new DaoFactory();
        connectionFactory=daoFactory.getConnectionFactory();
        dao = new HsqldbUserDao(connectionFactory);
    }

    @Test
    public void testCreate() {
        try
        {
            User user= new User();
            user.setFirstName("John");
            user.setLastName("Doe");
            user.setDateOfBirth(new Date(2018,11,22));
            assertNull(user.getId());
            user=dao.create(user);
            assertNotNull(user); //null or not null?
            assertNotNull(user.getId());
        }
        catch (DatabaseException e)
        {
            e.printStackTrace();
            fail(e.toString());
        }
    }

    @Test
    public void testFindAll()
    {
        try {
           Collection collection = dao.findAll();
           assertNotNull("Collection is null", collection);
           assertEquals("Collection size",1,collection.size());
        } catch (DatabaseException e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }

    @Test
    public void testUpdate() {
        try {
            User user = new User();
            user.setId(ETALON_ID_USER);
            user.setFirstName(ETALON_FIRSTNAME_USER);
            user.setLastName(ETALON_LASTNAME_USER);
            user.setDateOfBirth(new Date());
            dao.update(user);
            User updUser = dao.find(ETALON_ID_USER);
            assertEquals(ETALON_FIRSTNAME_USER, updUser.getFirstName());
            assertEquals(ETALON_LASTNAME_USER, updUser.getLastName());
        } catch (DatabaseException e) {
            e.printStackTrace();
            fail(e.toString());
        }

    }

    @Test
    public void testFind(){
        try{
            User user=dao.find(ETALON_ID_USER);
            assertNotNull(user);
            long userId = user.getId();
            assertEquals(ETALON_ID_USER, userId);

        } catch (DatabaseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUserDelete()
    {
        try {
            User user = dao.find(ETALON_ID_USER);
            int actualBefore = dao.findAll().size();
            dao.delete(user);
            int actualAfter = dao.findAll().size();
            assertNotSame(actualBefore, actualAfter);
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected IDatabaseConnection getConnection() throws Exception {
       connectionFactory = daoFactory.getConnectionFactory();
        return new DatabaseConnection(connectionFactory.createConnection());
    }

    @Override
    protected IDataSet getDataSet() throws Exception {
        return new XmlDataSet(getClass().getClassLoader().getResourceAsStream("..\\..usermanagement\\src\\test\\resources\\usersDataSet.xml"));
    }
}