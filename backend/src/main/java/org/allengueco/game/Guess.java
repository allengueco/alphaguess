package org.allengueco.game;

import jakarta.persistence.*;

@Entity
@Table(name = "guess")
public class Guess {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @ManyToOne
    @JoinColumn(name = "session_id")
    GameSession session;
    String word;
    Position position;

    public Guess() {
    }

    public Guess(String word, Position position) {
        this.word = word;
        this.position = position;
    }

    public GameSession getSession() {
        return session;
    }

    public void setSession(GameSession session) {
        this.session = session;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public enum Position {
        BEFORE,
        AFTER,
    }
}
