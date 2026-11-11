package fi.haaga_helia.quizzer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fi.haaga_helia.quizzer.model.Message;

public interface MessageRepository
        extends JpaRepository<Message, Long> {
}