package org.allengueco.converter;

import org.allengueco.game.Guesses;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@WritingConverter
public class GuessesToMultiMapConverter implements Converter<Guesses, Map<String, List<String>>> {

    @Override
    public Map<String, List<String>> convert(Guesses source) {
        return Map.of(
                "afterGuesses", source.getAfter().stream().toList(),
                "beforeGuesses", source.getBefore().stream().toList()
        );
    }
}
