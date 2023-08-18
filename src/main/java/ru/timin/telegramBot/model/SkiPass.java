package ru.timin.telegramBot.model;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Entity
@Data
public class SkiPass implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long skiPassId;

    private String skiPassNumber;

    private int lifts;

    @OneToOne(mappedBy = "skiPass", cascade = CascadeType.ALL)
    private Client client;

    public SkiPass() {

    }

    public SkiPass(long skiPassId, String skiPassNumber, int lifts, Client client) {
        this.skiPassId = skiPassId;
        this.skiPassNumber = skiPassNumber;
        this.lifts = lifts;
        this.client = client;
    }
}
