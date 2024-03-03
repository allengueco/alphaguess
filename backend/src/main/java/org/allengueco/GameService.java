package org.allengueco;

import org.allengueco.game.states.GameContext;
import org.eclipse.collections.api.factory.Maps;
import org.eclipse.collections.api.map.MutableMap;
import org.eclipse.collections.impl.tuple.Tuples;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class GameService {
    private final Logger LOG = LoggerFactory.getLogger(GameService.class);
    MutableMap<String, GameContext> games;

    public GameService() {
        this.games = Maps.mutable.empty();
    }

    public void addGame(String id, GameContext context) {
        this.games.add(Tuples.pair(id, context));
    }

    public GameContext getGame(String id) {
        return games.get(id);
    }

    public GameContext removeGame(String id) {
        return games.remove(id);
    }
}
