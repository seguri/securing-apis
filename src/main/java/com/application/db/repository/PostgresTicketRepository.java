package com.application.db.repository;

import com.application.domain.TicketRepository;
import com.application.domain.entity.Ticket;
import com.application.domain.exceptions.BusTicketException;
import com.application.domain.utils.PaginatedResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostgresTicketRepository implements TicketRepository {

    private DataSource ds;

    private final Logger log = LoggerFactory.getLogger(PostgresTicketRepository.class);

    public PostgresTicketRepository(DataSource ds) {
        this.ds = ds;
    }

    @Override
    public void bookTicket(Ticket ticket) {
        String sql = "INSERT INTO" +
                        " ticket(id, user_id, price, currency, source, destination)" +
                        " VALUES (?, ?, ?, ?, ?, ?)";
        try(Connection con = this.ds.getConnection()) {
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setLong(1, ticket.getId());
            statement.setLong(2, ticket.getUserId());
            statement.setLong(3, ticket.getPrice());
            statement.setString(4, ticket.getCurrency());
            statement.setString(5, ticket.getSource());
            statement.setString(6, ticket.getDestination());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new BusTicketException(e);
        }
    }

    @Override
    public PaginatedResponse<Ticket> listTickets(Long lastFetchedId, Integer limit) {
        String sql = "SELECT id, user_id, price, currency, source, destination" +
                " FROM ticket" +
                " WHERE id > ?" +
                " ORDER BY id" +
                " LIMIT ?";

        List<Ticket> tickets = new ArrayList<>();
        Long cursor = 0L;
        try (Connection con = this.ds.getConnection()) {
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setLong(1, lastFetchedId);
            statement.setInt(2, limit);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    Ticket ticket = readTicketFromResultSet(rs);
                    tickets.add(ticket);
                    cursor = ticket.getId();
                }
            }
        } catch (SQLException e) {
            throw new BusTicketException(e);
        }

        return new PaginatedResponse<>(tickets, cursor.toString());
    }

    @Override
    public Ticket getTicket(Integer ticketId) {
        String sql = "SELECT id, user_id, price, currency, source, destination" +
                " FROM ticket" +
                " WHERE id = ?";

        try (Connection con = this.ds.getConnection()) {
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setLong(1, ticketId);
            try (ResultSet rs = statement.executeQuery()) {
                rs.next();
                return readTicketFromResultSet(rs);
            }
        } catch (SQLException e) {
            throw new BusTicketException(e);
        }
    }

    private Ticket readTicketFromResultSet(ResultSet rs) throws SQLException {
        Ticket t = new Ticket();
        t.setId(rs.getLong(1));
        t.setUserId(rs.getLong(2));
        t.setPrice(rs.getLong(3));
        t.setCurrency(rs.getString(4));
        t.setSource(rs.getString(5));
        t.setDestination(rs.getString(6));
        return t;
    }
}
