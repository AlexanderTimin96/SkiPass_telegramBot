package ru.timin.telegramBot.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Data;

import java.io.Serializable;

@Entity
@Data
public class Client implements Serializable {
    @Id
    private Long chatId;
    private String firstName;
    private String lastName;
    private String userName;
    private String phone;

    @OneToOne
    private SkiPass skiPass;

    public Client(Long chatId, String firstName, String lastName, String userName, String phone, SkiPass skiPass) {
        this.chatId = chatId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.phone = phone;
        this.skiPass = skiPass;
    }

    public Client() {
    }
}