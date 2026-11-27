package fi.haaga_helia.quizzer.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fi.haaga_helia.quizzer.model.Message;
import fi.haaga_helia.quizzer.repository.MessageRepository;

@RestController 
@RequestMapping("/api/messages")
public class MessageRestController {
    
    private final MessageRepository messageRepository;

    public MessageRestController(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @GetMapping("/messages")
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    @GetMapping("/quiz-not-found")
    public ResponseEntity<String> quizNotFound() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Quiz not found");
    }

    @GetMapping("/server-error")
    public ResponseEntity<String> serverError() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Unexpected server error");
    }

}
