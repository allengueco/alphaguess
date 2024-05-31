import { Injectable, Signal, computed, inject, signal } from "@angular/core";
import { GameSessionSummary } from "./guess-session-summary.model";
import { WordService } from "./word.service";
import { SessionService } from "./session.service";

@Injectable({
    providedIn: 'root'
})
export class BetaGuessService {
    sessionService = inject(SessionService);
    state = signal(this.sessionService.currentGameOrDefault());
    wordService = inject(WordService);
    hints = computed(() => this.updateHints(this.state()))
    before = computed(() => this.state()['guesses']['before'])
    after = computed(() => this.state()['guesses']['after'])
    startTime = computed(() => new Date(this.state().startTime || Date.now()))
    chosenWord = computed(() => this.wordService.wordOfTheDay(this.startTime()))
    updateHints(state: GameSessionSummary) {
        const afterLength = state.guesses.after.length;
        const top = state.guesses.after[afterLength - 1] ?? '';
        const bottom = state.guesses.before[0] ?? '';

        const l = Math.min(top.length, bottom.length);

        let letters = '';
        let index = 0;

        for (let i = index; i < l; i++) {
            if (top.charAt(i) === bottom.charAt(i)) {
                letters += top.charAt(i);
                index++;
            } else {
                break;
            }
        }
        return { letters, index };
    }

    addGuess(guess: string) {
        const pos = this.wordService.compare(this.chosenWord(), guess);
        switch (pos) {
            case 'before':
            case 'after':
                this.state.update(s => {
                    const items = s.guesses[pos];
                    items.push(guess);
                    items.sort();
                    const guesses = { ...s.guesses };
                    const startTime = (s.startTime ||= new Date().toString());
                    return { ...s, guesses, startTime };
                });
                break;
            case 'equal':
                this.state.update(s => ({ ...s, isGameOver: true }));
        }
        this.sessionService.update(this.state());
    }
    reset() {
        this.sessionService.reset()
    }
}

