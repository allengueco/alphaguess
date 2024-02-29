package org.allengueco;

import org.allengueco.game.states.GameContext;
import org.eclipse.collections.api.factory.Maps;
import org.eclipse.collections.api.map.MutableMap;
import org.springframework.stereotype.Service;

@Service
public class GameService {
    MutableMap<String, GameContext> games;

    public GameService() {
        this.games = Maps.mutable.empty();
    }

    public GameContext getGame(String id) {
        return games.get(id);
    }

    public GameContext removeGame(String id) {
        return games.remove(id);
    }
}
