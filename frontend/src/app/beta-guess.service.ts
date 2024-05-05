import {computed, inject, Injectable, signal} from "@angular/core";
import {WordService} from "./word.service";
import {SessionService} from "./session.service";
import {DictionaryService} from "./dictionary.service";
import {Hint} from "./hint.model";
import {GameSessionSummary} from "./guess-session-summary.model";

@Injectable({
    providedIn: "root"
})
export class BetaGuessService {
    wordService = inject(WordService);
    sessionService = inject(SessionService);
    dictionaryService = inject(DictionaryService)
    readonly summary = signal(this.sessionService.currentGameOrDefault())
    hints = computed(() => this.updateWordHints(this.summary()))
    private currentWord = computed(() => this.wordService.wordOfTheDay(
        new Date(this.summary().startTime || Date.now())));

    constructor() {

    }

    currentGame() {
        return this.summary
    }

    currentHints() {
        return this.hints
    }

    addGuess(guess: string) {
        const alreadyGuessed = this.summary().guesses.after.includes(guess) || this.summary().guesses.before.includes(guess)
        const isNotValidWord = !this.dictionaryService.contains(guess)
        if (alreadyGuessed || isNotValidWord) return
        const pos = this.wordService.compare(this.currentWord(), guess)
        switch (pos) {
            case 'before':
            case 'after':
                this.summary.update(s => {
                    const items = s.guesses[pos]
                    items.push(guess)
                    items.sort()
                    s.startTime ||= new Date().toString()
                    return s
                })
                break;
            case 'equal':
                this.summary.update(s => ({...s, isGameOver: true}))
        }
        this.sessionService.update(this.summary())
    }

    giveUp() {
        this.sessionService.reset();
    }

    getSuccessMessage() {
        return this.wordService.randomSuccessMessage()
    }

    updateWordHints(summary: GameSessionSummary): Hint {
        console.log("$$$$")
        const afterLength = summary.guesses.after.length;
        const top = summary.guesses.after[afterLength - 1] ?? '';
        const bottom = summary.guesses.before[0] ?? '';

        const l = Math.min(top.length, bottom.length);

        let letters = '';
        let index = 0;

        for (let i = index; i < l; i++) {
            if (top.charAt(i) === bottom.charAt(i)) {
                letters += top.charAt(i);
                index++
            } else {
                break
            }
        }
        return {letters, index}
    }
}
