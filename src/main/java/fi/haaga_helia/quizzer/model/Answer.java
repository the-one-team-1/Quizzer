package fi.haaga_helia.quizzer.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;

// One answer given by a student. Anonymous: only the chosen option is stored.
@Entity
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // If the teacher deletes the question or option, its answers are deleted too
    @ManyToOne(optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Question question;

    @ManyToOne(optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private AnswerOption answerOption;

    private boolean correct;

    private LocalDateTime answeredAt;

    @PrePersist
    protected void onCreate() {
        answeredAt = LocalDateTime.now();
    }

    public Answer() {}

    public Answer(Question question, AnswerOption answerOption, boolean correct) {
        this.question = question;
        this.answerOption = answerOption;
        this.correct = correct;
    }

    public Long getId() { return id; }
    public Question getQuestion() { return question; }
    public AnswerOption getAnswerOption() { return answerOption; }
    public boolean isCorrect() { return correct; }
    public LocalDateTime getAnsweredAt() { return answeredAt; }
}
