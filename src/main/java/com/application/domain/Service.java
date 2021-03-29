package com.application.domain;


import com.application.domain.entity.Ticket;
import com.application.domain.entity.User;
import com.application.domain.utils.PaginatedResponse;

import java.io.ByteArrayInputStream;

public interface Service {
    void addUser(User user);
    void addUserBatch(ByteArrayInputStream bIn);
    User addUserPhoto(Long userID, ByteArrayInputStream photoInputStream);
    User getUserByID(Long userID);
    PaginatedResponse<User> listUsers(Long lastFetchedId, Integer limit);

    void bookTicket(Ticket ticket);
    Ticket getTicket(Integer ticketId);
    PaginatedResponse<Ticket> listTickets(Long lastFetchedId, Integer limit);

}
