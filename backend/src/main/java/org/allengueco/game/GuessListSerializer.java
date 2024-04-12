package org.allengueco.game;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.eclipse.collections.api.multimap.list.MutableListMultimap;
import org.eclipse.collections.impl.collector.Collectors2;
import org.eclipse.collections.impl.factory.Multimaps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GuessListSerializer extends JsonSerializer<List<Guess>> {
    private final Logger log = LoggerFactory.getLogger(GuessListSerializer.class);

    @Override
    public void serialize(List<Guess> guesses, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        MutableListMultimap<String, String> m = guesses.stream().collect(
                Collectors2.groupByAndCollect(
                        g -> g.getPosition().toString().toLowerCase(Locale.ROOT),
                        Guess::getWord,
                        Multimaps.mutable.list::empty));
        log.info(m.toString());
        gen.writeStartObject();
        serializers.defaultSerializeField("before", m.get("before"), gen);
        serializers.defaultSerializeField("after", m.get("after"), gen);
        gen.writeEndObject();
    }
}
