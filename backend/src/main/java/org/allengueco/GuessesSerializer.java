package org.allengueco;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.allengueco.game.Guesses;
import org.eclipse.collections.impl.set.sorted.mutable.TreeSortedSet;

import java.io.IOException;

/**
 * Ensures that the arrays are sorted.
 */
public class GuessesSerializer extends JsonSerializer<Guesses> {
    @Override
    public void serialize(Guesses value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();

        writeSortedField(value.before(), "before", gen);

        writeSortedField(value.after(), "after", gen);

        gen.writeEndObject();
    }

    private void writeSortedField(Iterable<String> listToSort, String fieldName, JsonGenerator gen) throws IOException {
        gen.writeArrayFieldStart(fieldName);

        for (String s : TreeSortedSet.newSet(String.CASE_INSENSITIVE_ORDER, listToSort)) {
            gen.writeString(s);
        }

        gen.writeEndArray();
    }
}
