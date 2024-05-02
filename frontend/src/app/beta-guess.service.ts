import {inject, Injectable} from "@angular/core";
import {WordService} from "./word.service";
import {SessionService} from "./session.service";
import {GameSessionSummary} from "./guess-session-summary.model";
import {DictionaryService} from "./dictionary.service";
import {tap} from "rxjs";

@Injectable({
    providedIn: "root"
})
export class BetaGuessService {
    wordService = inject(WordService);
    sessionService = inject(SessionService);
    dictionaryService = inject(DictionaryService)
    private _summary: GameSessionSummary = this.sessionService.currentSessionOrDefault()

    private currentWord: string = "";

    constructor() {
        this.dictionaryService.randomWord().pipe(tap(console.log)).subscribe(w => this.currentWord = w)
    }

    currentGame() {
        return this.sessionService.currentGame()
    }

    addGuess(guess: string) {
        this.dictionaryService.contains(guess)
            .subscribe(r => {
                if (r) {
                    const pos = this.wordService.compare(this.currentWord, guess)
                    switch (pos) {
                        case 'before':
                        case 'after':
                            this._summary.guesses[pos].push(guess)
                            this._summary.guesses[pos].sort()
                            this.updateWordHints()
                            break
                        case 'equal':
                            this._summary.isGameOver = true
                    }

                    this.sessionService.update(this._summary)
                }
            })
    }

    reset() {
        this.sessionService.reset();
        this.dictionaryService.randomWord().subscribe(w => this.currentWord = w)
    }

    updateWordHints() {
        const afterLength = this._summary.guesses.after.length;
        const top = this._summary.guesses.after[afterLength - 1] ?? '';
        const bottom = this._summary.guesses.before[0] ?? '';

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
        this._summary.hints = {letters, index}
    }
}
