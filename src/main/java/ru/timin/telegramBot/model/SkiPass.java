package ru.timin.telegramBot.model;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
public class SkiPass implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long skiPassId;

    private String skiPassNumber;

    private int lifts;

    @OneToOne(mappedBy = "skiPass", cascade = CascadeType.ALL)
    private Client client;

    public long getSkiPassId() {
        return skiPassId;
    }

    public void setSkiPassId(long skiPassId) {
        this.skiPassId = skiPassId;
    }

    public String getSkiPassNumber() {
        return skiPassNumber;
    }

    public void setSkiPassNumber(String skiPassNumber) {
        this.skiPassNumber = skiPassNumber;
    }

    public int getLifts() {
        return lifts;
    }

    public void setLifts(int lifts) {
        this.lifts = lifts;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
