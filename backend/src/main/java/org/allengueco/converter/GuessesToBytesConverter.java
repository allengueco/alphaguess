package org.allengueco.converter;

import org.allengueco.game.Guesses;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

@WritingConverter
public class GuessesToBytesConverter implements Converter<Guesses, byte[]> {
    Jackson2JsonRedisSerializer<Guesses> serializer;

    public GuessesToBytesConverter(Jackson2JsonRedisSerializer<Guesses> serializer) {
        this.serializer = serializer;
    }

    @Override
    public byte[] convert(Guesses source) {
        return serializer.serialize(source);
    }
}
