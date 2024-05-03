import {Injectable} from "@angular/core";
import {GameSessionSummary} from "./guess-session-summary.model";

@Injectable({providedIn: "root"})
export class SessionService {
    private KEY = "session"

    update(summary: GameSessionSummary) {
        localStorage.setItem(this.KEY, JSON.stringify(summary));
    }

    reset() {
        localStorage.removeItem(this.KEY)
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
        const currentSession = localStorage.getItem(this.KEY)
        return currentSession == null ? this.emptySummary() : JSON.parse(currentSession)
    }
}
