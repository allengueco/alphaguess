import {computed, inject, Injectable, signal} from "@angular/core";
import {WordService} from "./word.service";
import {SessionService} from "./session.service";
import {DictionaryService} from "./dictionary.service";
import {Hint} from "./hint.model";

@Injectable({
    providedIn: "root"
})
export class BetaGuessService {
    wordService = inject(WordService);
    sessionService = inject(SessionService);
    dictionaryService = inject(DictionaryService)
    readonly summary = signal(this.sessionService.currentGameOrDefault())
    readonly hints = signal({letters: "", index: -1} as Hint)
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
                this.hints.update(_ => this.updateWordHints())
                break;
            case 'equal':
                this.summary.update(s => ({...s, isGameOver: true}))
        }
        this.sessionService.update(this.summary())
    }

    giveUp() {
        this.sessionService.reset();
        this.currentWord = computed(() => this.wordService.randomWord())
    }

    getSuccessMessage() {
        return this.wordService.randomSuccessMessage()
    }

    updateWordHints(): Hint {
        const afterLength = this.summary().guesses.after.length;
        const top = this.summary().guesses.after[afterLength - 1] ?? '';
        const bottom = this.summary().guesses.before[0] ?? '';

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
