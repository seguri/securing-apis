package com.application.domain;

import com.application.domain.entity.Ticket;
import com.application.domain.utils.PaginatedResponse;

public interface TicketRepository {
    void bookTicket(Ticket ticket);
    PaginatedResponse<Ticket> listTickets(Long lastFetchedId, Integer limit);
    Ticket getTicket(Integer ticketId);
}
