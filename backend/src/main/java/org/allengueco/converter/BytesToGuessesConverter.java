package org.allengueco.converter;

import org.allengueco.game.Guesses;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;


@ReadingConverter
public class BytesToGuessesConverter implements Converter<byte[], Guesses> {
    Jackson2JsonRedisSerializer<Guesses> serializer;

    public BytesToGuessesConverter(Jackson2JsonRedisSerializer<Guesses> serializer) {
        this.serializer = serializer;
    }

    @Override
    public Guesses convert(byte[] source) {
        return serializer.deserialize(source);
    }
}
