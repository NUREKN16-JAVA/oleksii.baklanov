package baklanov.usermanagement;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;

import static org.junit.Assert.*;

public class UserTest {
    User user;
    private static final String FIRST_NAME_ETALONE="Ivan";
    private static final String LAST_NAME_ETALONE="Ivanov";
    private static final String FULL_NAME_ETALONE="Ivanov, Ivan";

    private static final int CURRENT_YEAR=2018;
    private static final int YEAR_OF_BIRTH=1999;
    /*тест для случая, когда ДР уже прошел, но месяц еще идет*/

    private static final int ETALONE_AGE_1=19;
    private static final int DAY_OF_BIRTH_1=19;
    private static final int MONTH_OF_BIRTH_1=Calendar.MAY;

    @Before
    public void setUp() throws Exception {
        user=new User();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getFullName() {
        user.setFirstName(FIRST_NAME_ETALONE);
        user.setLastName(LAST_NAME_ETALONE);
        assertEquals(FULL_NAME_ETALONE, user.getFullName());
    }

    @Test
    public void testGetAge(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(YEAR_OF_BIRTH, MONTH_OF_BIRTH_1, DAY_OF_BIRTH_1);
        user.setDateOfBirth(calendar.getTime());
        assertEquals(ETALONE_AGE_1, user.getAge());
    }
                    /* October, 29*/
    @Test
    public void testGetAgeWhenMonthAfterBday(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(YEAR_OF_BIRTH, Calendar.getInstance().get(Calendar.MONTH)+1, DAY_OF_BIRTH_1);
        user.setDateOfBirth(calendar.getTime());
        assertEquals(ETALONE_AGE_1-1, user.getAge());
    }
    @Test
    public void testGetAgeWhenMonthBeforeBday()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.set(YEAR_OF_BIRTH, Calendar.getInstance().get(Calendar.MONTH)-1, DAY_OF_BIRTH_1);
        user.setDateOfBirth(calendar.getTime());
        assertEquals(ETALONE_AGE_1, user.getAge());
    }
    @Test
    public void testGetAgeWhenDayAfterBday()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.set(YEAR_OF_BIRTH, Calendar.getInstance().get(Calendar.MONTH) , Calendar.getInstance().get(Calendar.DAY_OF_MONTH)+1 );
        user.setDateOfBirth(calendar.getTime());
        assertEquals(ETALONE_AGE_1-1, user.getAge());
    }
    @Test
    public void testGetAgeWhenDayBeforeBday()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.set(YEAR_OF_BIRTH, Calendar.getInstance().get(Calendar.MONTH) , Calendar.getInstance().get(Calendar.DAY_OF_MONTH)-1);
        user.setDateOfBirth(calendar.getTime());
        assertEquals(ETALONE_AGE_1, user.getAge());
    }
}