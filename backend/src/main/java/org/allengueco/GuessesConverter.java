package org.allengueco;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.StdConverter;
import org.allengueco.game.Guesses;

public class GuessesConverter extends StdConverter<Guesses, String> {
    private static final ObjectMapper mapper = new ObjectMapper();


    @Override
    public String convert(Guesses value) {
        return "";
    }
}
