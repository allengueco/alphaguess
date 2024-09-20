import { Injectable, Signal, computed, inject, signal } from "@angular/core";
import { GameSessionSummary } from "./guess-session-summary.model";
import { WordService } from "./word.service";
import { SessionService } from "./session.service";
import { NgForm } from "@angular/forms";
import { fromStorage } from "./from-storage.function";
import { Hint } from "./hint.model";
import { StorageService } from "./storage.service";

@Injectable({
    providedIn: 'root'
})
export class BetaGuessService {
    readonly storageService = inject(StorageService)
    readonly state = fromStorage<GameSessionSummary>('session')

    wordService = inject(WordService)

    hints = computed(() => this.updateHints(this.state()))
    before = computed(() => this.state()?.guesses?.before)
    after = computed(() => this.state()?.guesses?.after)

    updateHints(state: GameSessionSummary | null): Hint | null {
        if (state === null) return null;
        const afterLength = state.guesses.after.length
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
        const pos = this.wordService.compare(guess);
        switch (pos) {
            case 'before':
            case 'after':
                this.state.update(s => {
                    const items = s?.guesses[pos] ?? [];
                    items.push(guess);
                    items.sort();
                    const guesses = { ...s?.guesses } ?? { guesses: { before: [], after: [] } };
                    const startTime = s?.startTime ?? new Date().toString();
                    return { isGameOver: true, guesses, startTime };
                });
                break;
            case 'equal':
                this.state.update(s => ({ ...s, isGameOver: true }));
        }
    }

    reset(guessForm: NgForm) {
        this.storageService.setItem('session', null)
        guessForm.reset();
    }
}

