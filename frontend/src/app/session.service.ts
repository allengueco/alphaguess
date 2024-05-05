import {Injectable} from "@angular/core";
import {GameSessionSummary} from "./guess-session-summary.model";

@Injectable({providedIn: "root"})
export class SessionService {
    private SESSION = "session"
    private HINT = "hint"

    update(summary: GameSessionSummary) {
        localStorage.setItem(this.SESSION, JSON.stringify(summary));
    }

    reset() {
        localStorage.removeItem(this.SESSION)
    }

    emptySummary(): GameSessionSummary {
        return {
            guesses: {
                after: [],
                before: []
            },
            startTime: "",
            isGameOver: false
        }
    }

    currentGameOrDefault(): GameSessionSummary {
        const currentSession = localStorage.getItem(this.SESSION)
        return currentSession == null ? this.emptySummary() : JSON.parse(currentSession)
    }
}
