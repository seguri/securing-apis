package com.application.db.repository;

import com.application.domain.UserRepository;
import com.application.domain.entity.User;
import com.application.domain.exceptions.BusTicketException;
import com.application.domain.utils.PaginatedResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostgresUserRepository implements UserRepository {

    private DataSource ds;

    private final Logger log = LoggerFactory.getLogger(PostgresUserRepository.class);

    public PostgresUserRepository(DataSource ds) {
        this.ds = ds;
    }

    @Override
    public void createUser(User user) {
        String sql = "INSERT INTO" +
                        " user_account(id, name, age, phone, address)" +
                        " VALUES (?, ?, ?, ?, ?)";

        try(Connection con = this.ds.getConnection()) {
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setLong(1, user.getId());
            statement.setString(2, user.getName());
            statement.setInt(3, user.getAge());
            statement.setString(4, user.getPhone());
            statement.setString(5, user.getAddress());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new BusTicketException(e);
        }
    }

    @Override
    public void createUsers(List<User> users) {
        String sql = "INSERT INTO" +
            " user_account(id, name, age, phone, address)" +
            " VALUES (?, ?, ?, ?, ?)";
        try(Connection con = this.ds.getConnection()) {
            PreparedStatement statement = con.prepareStatement(sql);
            for (User user: users) {
                statement.setLong(1, user.getId());
                statement.setString(2, user.getName());
                statement.setInt(3, user.getAge());
                statement.setString(4, user.getPhone());
                statement.setString(5, user.getAddress());
                statement.addBatch();
            }
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new BusTicketException(e);
        }
    }

    @Override
    public User getUserByID(Long id) {
        String sql = "SELECT id, name, phone, age, address" +
                " FROM user_account" +
                " WHERE id = ?";

        try(Connection con = this.ds.getConnection()) {
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setLong(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                rs.next();
                return readUserFromResultSet(rs);
            }
        } catch (SQLException e) {
            throw new BusTicketException(e);
        }
    }

    @Override
    public PaginatedResponse<User> listUsers(Long lastFetchedId, Integer limit) {
        String sql = "SELECT id, name, phone, age, address " +
                " FROM user_account" +
                " WHERE id > ?" +
                " ORDER BY id" +
                " LIMIT ?";

        List<User> users = new ArrayList<>();
        Long cursor = 0L;
        try(Connection con = this.ds.getConnection()) {
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setLong(1, lastFetchedId);
            statement.setLong(2, limit);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    User user = readUserFromResultSet(rs);
                    users.add(user);
                    cursor = user.getId();
                }
            }
        } catch (SQLException e) {
            throw new BusTicketException(e);
        }

        return new PaginatedResponse<>(users, cursor.toString());
    }

    private User readUserFromResultSet(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getLong(1));
        user.setName(rs.getString(2));
        user.setPhone(rs.getString(3));
        user.setAge(rs.getInt(4));
        user.setAddress(rs.getString(5));
        return user;
    }
}
