package org.allengueco.converter;

import org.allengueco.game.Guesses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@ReadingConverter
public class MultiMapToGuessesConverter implements Converter<Map<String, List<String>>, Guesses> {

    @Override
    public Guesses convert(Map<String, List<String>> source) {
        List<String> before = source.containsKey("before") ? source.get("before") : List.of();
        List<String> after = source.containsKey("after") ? source.get("after") : List.of();
        return Guesses.withGuesses(before, after);
    }
}
