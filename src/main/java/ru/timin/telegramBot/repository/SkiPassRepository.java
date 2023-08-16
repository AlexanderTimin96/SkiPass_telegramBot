package ru.timin.telegramBot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.timin.telegramBot.model.SkiPass;

@Repository
public interface SkiPassRepository extends JpaRepository<SkiPass, Long> {
}
