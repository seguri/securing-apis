package com.application.api;

import com.application.api.helpers.PaginationHelper;
import com.application.api.helpers.ResponseHelper;
import com.application.domain.Service;
import com.application.domain.entity.Ticket;
import com.application.domain.entity.User;
import com.application.domain.exceptions.BadRequestParamException;
import com.application.domain.exceptions.InvalidXMLSchemaException;
import com.application.domain.utils.PaginatedResponse;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;

import java.io.ByteArrayInputStream;
import java.util.List;

import static spark.Spark.*;

public class ApplicationAPI {

    private final List<String> secrets;
    private final Service service;

    private final Logger log = LoggerFactory.getLogger(ApplicationAPI.class);
    private final Gson gson = new Gson();

    public ApplicationAPI(List<String> secrets, Service service) {
        this.secrets = secrets;
        this.service = service;
    }

    public void Start() {
        port(8080);

        // security filters
        before((req, res) -> {
            String xApiKey = req.headers("X-API-KEY");
            if (xApiKey == null || !secrets.contains(xApiKey)) {
                halt(401, "unauthorized request");
            }
        });

        path("/v1", () -> {
            // application paths
            path("/tickets", () -> {
                get("", this::listTickets, gson::toJson);
                get("/:ticketId", this::getTicket, gson::toJson);
                post("", this::bookTicket);
            });

            path("/users", () -> {
                get("", this::listUsers, gson::toJson);
                get("/:userId", this::getUser, gson::toJson);
                post("", this::createUser);
                post("/batch", this::createUserBatch);
                post("/:userId/photos/", this::uploadPhoto, gson::toJson);
            });
        });
    }

    private Object createUserBatch(Request req, Response res) {
        try {
            ByteArrayInputStream bIn = new ByteArrayInputStream(req.body().getBytes());

            this.service.addUserBatch(bIn);

            res.status(201);
            return "";
        } catch(InvalidXMLSchemaException e) {
            return ResponseHelper.badRequestError(res, "invalid XML schema informed, please refer to the documentation");
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseHelper.internalServerError(res);
        }
    }

    public Object createUser(Request req, Response res) {
        try {
            User user = gson.fromJson(req.body(), User.class);

            this.service.addUser(user);

            res.status(201);
            return "";
        } catch(JsonSyntaxException e) {
            return ResponseHelper.badRequestError(res, e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseHelper.internalServerError(res);
        }
    }

    public Object getUser(Request req, Response res) {
        try {
            Long userId = Long.parseLong(req.params("userId"));

            User user = this.service.getUserByID(userId);

            res.status(200);
            return user;
        } catch(NumberFormatException e) {
            return ResponseHelper.badRequestError(res, e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseHelper.internalServerError(res);
        }
    }

    public Object uploadPhoto(Request req, Response res) {
        try {
            Long userId = Long.parseLong(req.params("userId"));
            var bodyStream = new ByteArrayInputStream(req.bodyAsBytes());

            User user = this.service.addUserPhoto(userId, bodyStream);

            res.status(200);
            return user;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseHelper.internalServerError(res);
        }
    }

    public Object listUsers(Request req, Response res) {
        try {
            Integer limit = PaginationHelper.getPageLimit(req);
            var lastFetchedId = PaginationHelper.getOffset(req);

            PaginatedResponse<User> users = this.service.listUsers(lastFetchedId, limit);

            res.header("x-next", PaginationHelper.buildNextPageLink(req, users.getCursor()));
            res.status(200);
            return users.getElements();
        } catch(BadRequestParamException e) {
            return ResponseHelper.badRequestError(res, e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseHelper.internalServerError(res);
        }
    }

    public Object getTicket(Request req, Response res) {
        try {
            Integer ticketId = Integer.parseInt(req.params("ticketId"));

            Ticket ticket = this.service.getTicket(ticketId);

            res.status(200);
            return ticket;
        } catch(NumberFormatException e) {
            return ResponseHelper.badRequestError(res, e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseHelper.internalServerError(res);
        }
    }

    public Object bookTicket(Request req, Response res) {
        try {
            Ticket ticket = gson.fromJson(req.body(), Ticket.class);

            this.service.bookTicket(ticket);

            res.status(201);
            return "";
        } catch(JsonSyntaxException e) {
            return ResponseHelper.badRequestError(res, e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseHelper.internalServerError(res);
        }
    }

    public Object listTickets(Request req, Response res) {
        try {
            Integer limit = PaginationHelper.getPageLimit(req);
            var lastFetchedId = PaginationHelper.getOffset(req);

            PaginatedResponse<Ticket> tickets = this.service.listTickets(lastFetchedId, limit);

            res.header("x-next", PaginationHelper.buildNextPageLink(req, tickets.getCursor()));
            res.status(200);
            return tickets.getElements();
        } catch(BadRequestParamException e) {
            return ResponseHelper.badRequestError(res, e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseHelper.internalServerError(res);
        }
    }
}
