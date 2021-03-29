package com.application.domain.service;

import com.application.domain.Service;
import com.application.domain.TicketRepository;
import com.application.domain.UserRepository;
import com.application.domain.entity.Ticket;
import com.application.domain.entity.User;
import com.application.domain.exceptions.BusTicketException;
import com.application.domain.exceptions.InexistentUserException;
import com.application.domain.utils.PaginatedResponse;
import com.application.domain.utils.UserBatchReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class ServiceImpl implements Service {

    private final String photoUploadDirectory;

    private final UserRepository userRepository;
    private final TicketRepository ticketRepository;

    private final Logger log = LoggerFactory.getLogger(ServiceImpl.class);

    public ServiceImpl(UserRepository userRepository, TicketRepository ticketRepository, String photoUploadDirectory) {
        this.userRepository = userRepository;
        this.ticketRepository = ticketRepository;
        this.photoUploadDirectory = photoUploadDirectory;
    }

    @Override
    public void addUser(User u) {
        log.info("Adding user");
        this.userRepository.createUser(u);
    }

    @Override
    public void addUserBatch(ByteArrayInputStream in) {
        log.info("adding user batch");
        try {
            List<User> users = UserBatchReader.read(in);
            this.userRepository.createUsers(users);
        } catch (BusTicketException e) {
            throw e;
        } catch (Exception e) {
            throw new BusTicketException(e);
        }
    }

    @Override
    public User addUserPhoto(Long userID, ByteArrayInputStream photoInputStream) {
        log.info("Adding user photo");

        User user = this.userRepository.getUserByID(userID);
        if (user == null) {
            throw new InexistentUserException(userID);
        }

        try {
            String filePrefix = user.getName().replaceAll(" ", "");
            File temp = File.createTempFile(filePrefix, ".png");

            RandomAccessFile rw = new RandomAccessFile(temp, "rw");
            rw.write(photoInputStream.readAllBytes());
            rw.close();

            String command = String.format("mv %s %s", temp.getAbsolutePath(), photoUploadDirectory + temp.getName());
            Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", command}).waitFor();
        } catch (Exception e) {
            throw new BusTicketException(e);
        }

        return user;
    }


    @Override
    public User getUserByID(Long userID) {
        log.info("Getting user by id " + userID);
        return this.userRepository.getUserByID(userID);
    }

    @Override
    public PaginatedResponse<User> listUsers(Long lastFetchedId, Integer limit) {
        log.info("Listing users");
        return this.userRepository.listUsers(lastFetchedId, limit);
    }

    @Override
    public void bookTicket(Ticket ticket) {
        log.info(String.format("Booking ticket for user %d", ticket.getUserId()));
        this.ticketRepository.bookTicket(ticket);
    }

    @Override
    public Ticket getTicket(Integer ticketId) {
        log.info(String.format("Getting ticket with id %d", ticketId));
        return ticketRepository.getTicket(ticketId);
    }

    @Override
    public PaginatedResponse<Ticket> listTickets(Long lastFetchedId, Integer limit) {
        log.info("Listing tickets");
        return ticketRepository.listTickets(lastFetchedId, limit);
    }
}
