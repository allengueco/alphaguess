package org.allengueco.game;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.eclipse.collections.impl.collector.Collectors2;
import org.eclipse.collections.impl.factory.Multimaps;

import java.io.IOException;
import java.util.List;

public class GuessListSerializer extends JsonSerializer<List<Guess>> {

    @Override
    public void serialize(List<Guess> guesses, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        var m = guesses.stream().collect(
                Collectors2.groupByAndCollect(
                        Guess::getPosition,
                        Guess::getWord,
                        Multimaps.mutable.list::empty));
        gen.writeStartObject();
        gen.writeFieldName("guesses");

        gen.writeEndObject();
    }
}
