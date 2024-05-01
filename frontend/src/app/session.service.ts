import {Injectable} from "@angular/core";
import {GameSessionSummary} from "./guess-session-summary.model";
import {BehaviorSubject} from "rxjs";

@Injectable({providedIn: "root"})
export class SessionService {
    private $summary: BehaviorSubject<GameSessionSummary> = new BehaviorSubject<GameSessionSummary>(this.emptySummary());
    private KEY = "session"

    currentGame() {
        const currentSession = localStorage.getItem(this.KEY)
        if (currentSession) {
            this.$summary.next(JSON.parse(currentSession))
        }
        return this.$summary.asObservable()
    }

    update(summary: GameSessionSummary) {
        this.$summary.next(summary)
        localStorage.setItem(this.KEY, JSON.stringify(summary));
    }

    reset() {
        this.$summary.next(this.emptySummary())
        localStorage.removeItem(this.KEY)
    }

    emptySummary(): GameSessionSummary {
        return {
            guesses: {
                after: [],
                before: []
            },
            lastSubmissionTimestamp: Date.now().toString(),
            isGameOver: false,
            hints: {
                letters: "",
                index: -1
            }
        }
    }

    currentSessionOrDefault(): GameSessionSummary {
        const currentSession = localStorage.getItem(this.KEY)
        return currentSession == null ? this.emptySummary() : JSON.parse(currentSession)
    }
}
