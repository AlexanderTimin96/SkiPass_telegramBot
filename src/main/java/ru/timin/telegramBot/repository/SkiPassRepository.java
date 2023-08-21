package ru.timin.telegramBot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.timin.telegramBot.entity.Client;
import ru.timin.telegramBot.entity.SkiPass;

import java.util.Optional;

@Repository
public interface SkiPassRepository extends JpaRepository<SkiPass, Long> {
    Optional<SkiPass> findBySkiPassNumber(String number);
    Optional<SkiPass> findByClient(Client client);
}
