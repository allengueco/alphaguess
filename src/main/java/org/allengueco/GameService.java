package org.allengueco;

import org.allengueco.game.states.GameContext;
import org.eclipse.collections.api.factory.Maps;
import org.eclipse.collections.api.map.MutableMap;
import org.springframework.stereotype.Service;

@Service
public class GameService {
    MutableMap<Long, GameContext> games;

    public GameService() {
        this.games = Maps.mutable.empty();
    }

    public GameContext getGame(Long id) {
        return games.get(id);
    }

    public GameContext removeGame(Long id) {
        return games.remove(id);
    }
}
