package baklanov.usermanagement.db;

import com.mockobjects.dynamic.Mock;

public class MockDaoFactory extends DaoFactory{

    private Mock mockUserDao;

    @Override
    public UserDao getUserDao() {
        return (UserDao) mockUserDao.proxy();
    }

    public MockDaoFactory(){
        this.mockUserDao=new Mock(UserDao.class);
    }

    public Mock getMockUserDao(){
        return mockUserDao;
    }
}
