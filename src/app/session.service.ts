import { Injectable } from '@angular/core';
import { GameSessionSummary } from './guess-session-summary.model';

@Injectable({ providedIn: 'root' })
export class SessionService {
  private SESSION = 'session';

  constructor() {
    const currentSession = localStorage.getItem(this.SESSION);
    if (currentSession) {
      const today = new Date();
      const sessionStartDate = new Date(JSON.parse(currentSession).startTime);

      if (today.getDay() != sessionStartDate.getDay()) {
        console.log(`Removing old session...`);
        this.reset();
      }
    }
  }

  update(summary: GameSessionSummary) {
    localStorage.setItem(this.SESSION, JSON.stringify(summary));
  }

  reset() {
    localStorage.removeItem(this.SESSION);
  }

  emptySummary(): GameSessionSummary {
    return {
      guesses: {
        after: [],
        before: [],
      },
      startTime: '',
      isGameOver: false,
    };
  }

  currentGameOrDefault(): GameSessionSummary {
    const currentSession = localStorage.getItem(this.SESSION);
    return currentSession == null
      ? this.emptySummary()
      : JSON.parse(currentSession);
  }
}
