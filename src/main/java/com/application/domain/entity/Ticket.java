package com.application.domain.entity;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class Ticket {
    private Long id;
    @SerializedName("user_id")
    private Long userId;
    private Long price;
    private String currency;
    private String source;
    private String destination;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ticket)) return false;
        Ticket ticket = (Ticket) o;
        return Objects.equals(getId(), ticket.getId()) &&
                Objects.equals(getUserId(), ticket.getUserId()) &&
                Objects.equals(getPrice(), ticket.getPrice()) &&
                Objects.equals(getCurrency(), ticket.getCurrency()) &&
                Objects.equals(source, ticket.source) &&
                Objects.equals(destination, ticket.destination);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUserId(), getPrice(), getCurrency(), source, destination);
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "ID=" + id +
                ", user_id='" + userId + '\'' +
                ", price=" + price +
                ", currency='" + currency + '\'' +
                ", source=" + source +
                ", destination=" + destination +
                '}';
    }
}
