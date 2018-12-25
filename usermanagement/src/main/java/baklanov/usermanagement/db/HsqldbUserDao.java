package baklanov.usermanagement.db;

import baklanov.usermanagement.User;

import java.sql.*;
import java.util.Collection;
import java.util.LinkedList;

class HsqldbUserDao implements UserDao{

    public static final String INSERT_QUERY = "INSERT INTO PUBLIC.users (firstname,lastname,dateofbirth) VALUES (?,?,?);";
    public static final String SELECT_ALL_QUERY = "SELECT id, firstname, lastname, dateofbirth FROM PUBLIC.users;";
    public static final String UPDATE_QUERY = "UPDATE users SET firstname = ?, lastname = ?, dateofbirth = ?  WHERE id = ?;";
    public static final String SELECT_ONE = "SELECT id, firstname, lastname, dateofbirth FROM PUBLIC.users WHERE id=?;";
    private static final String DELETE_QUERY = "DELETE FROM users WHERE id = ?";
    private static final String SELECT_ONE_BY_NAME = "SELECT id, firstname, lastname, dateofbirth FROM PUBLIC.users WHERE firstname=? AND lastname=?;";
    private ConnectionFactory connectionFactory;

    public HsqldbUserDao() {

    }

    public HsqldbUserDao(ConnectionFactory connectionFactory) {
        this.connectionFactory=connectionFactory;
    }

    public ConnectionFactory getConnectionFactory() {
        return connectionFactory;
    }

    public void setConnectionFactory(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }


    @Override
    public User create(User user) throws DatabaseException {
        try (Connection connection = connectionFactory.createConnection(); PreparedStatement statement = connection.prepareStatement(INSERT_QUERY)) {
            statement.setString(1, user.getFirstName());
            statement.setString(2, user.getLastName());
            statement.setDate(3, new Date(user.getDateOfBirth().getTime()));
            int n = statement.executeUpdate();
            if (n != 1) {
                throw new DatabaseException("Number if the inserted rows:" + n);
            }
            try (CallableStatement callableStatement = connection.prepareCall("call IDENTITY() "); ResultSet keys = callableStatement.executeQuery()) {
                if (keys.next()) {
                    user.setId(new Long(keys.getLong(1)));
                }
            }
            return user;
        } catch (DatabaseException e) {
            throw e;
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void update(User user) throws DatabaseException {
        try (Connection connection = connectionFactory.createConnection(); PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {
            statement.setString(1, user.getFirstName());
            statement.setString(2, user.getLastName());
            statement.setDate(3, new Date(user.getDateOfBirth().getTime()));
            statement.setLong(4, user.getId());
            int n = statement.executeUpdate();
            if (n != 1) {
                throw new DatabaseException("Number if the updated rows:" + n);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(User user) throws DatabaseException {
        try (Connection connection = connectionFactory.createConnection(); PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)) {
            statement.setLong(1, user.getId());
            int n = statement.executeUpdate();
            if (n != 1) {
                throw new DatabaseException("Number if the updated rows:" + n);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public User find(Long id) throws DatabaseException {

        try (Connection connection = connectionFactory.createConnection(); PreparedStatement statement = connection.prepareStatement(SELECT_ONE)) {
            User user;

            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            user = new User();
            while (resultSet.next()) {
                user.setId(resultSet.getLong(1));
                user.setFirstName(resultSet.getString(2));
                user.setLastName(resultSet.getString(3));
                user.setDateOfBirth(resultSet.getDate(4));
            }
            resultSet.close();
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Collection findAll() throws DatabaseException {
        Collection result = new LinkedList();
        try (Connection connection = connectionFactory.createConnection(); Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(SELECT_ALL_QUERY)) {
            while (resultSet.next()) {
                User user = new User();
                user.setId(new Long(resultSet.getString(1)));
                user.setFirstName(resultSet.getString(2));
                user.setLastName(resultSet.getString(3));
                user.setDateOfBirth(resultSet.getDate(4));
                result.add(user);
            }
        } catch (DatabaseException e) {
            throw e;
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }

        return result;
    }

    @Override
    public Collection<User> find(String firstName, String lastName) throws DatabaseException {
        Collection result = new LinkedList();
        try{
            Connection connection=connectionFactory.createConnection();
            PreparedStatement statement = connection.prepareStatement(SELECT_ONE_BY_NAME);
            statement.setString(1,firstName);
            statement.setString(1,lastName);
            ResultSet resultSet=statement.executeQuery();
            while (resultSet.next()){
                User user=new User();
                user.setId(new Long(resultSet.getLong(1)));
                user.setFirstName(resultSet.getString(2));
                user.setLastName(resultSet.getString(3));
                user.setDateOfBirth(resultSet.getDate(4));
                result.add(user);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
