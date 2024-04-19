package org.allengueco.game;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "guess")
public class Guess {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @ManyToOne(fetch = FetchType.LAZY)
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Guess guess = (Guess) o;
        return Objects.equals(session, guess.session) && Objects.equals(word, guess.word) && position == guess.position;
    }

    @Override
    public int hashCode() {
        return Objects.hash(session, word, position);
    }

    public enum Position {
        BEFORE,
        AFTER,
    }
}
