package ru.timin.telegramBot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.timin.telegramBot.model.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {
}
