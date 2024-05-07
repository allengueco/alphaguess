import {Component, computed, inject, signal, Signal,} from '@angular/core';
import {CommonModule} from '@angular/common';
import {RouterOutlet} from '@angular/router';
import {BetaGuessService} from "./beta-guess.service";
import {FormsModule, NgForm} from "@angular/forms";
import {GameSessionSummary} from "./guess-session-summary.model";
import {Hint} from "./hint.model";
import {GuessListComponent} from "./guess-list.component";
import { GuessValidatorDirective } from './guess-validator.directive';

@Component({
    selector: 'app-root',
    standalone: true,
    imports: [CommonModule, RouterOutlet, FormsModule, GuessListComponent, GuessValidatorDirective],
    templateUrl: './app.component.html'
})
export class AppComponent {
    betaGuessService = inject(BetaGuessService);
    summary: Signal<GameSessionSummary> = this.betaGuessService.summary
    before: Signal<string[]> = computed(() => this.summary().guesses.before)
    after: Signal<string[]> = computed(() => this.summary().guesses.after)
    hints: Signal<Hint> = computed(() => this.updateWordHints(this.summary()))

    guess = signal<string>('')
    sanitizedGuess = computed(() => this.sanitized(this.guess()))
    
    onSubmit(guessForm: NgForm) {
        if (!guessForm.form.controls['guess'].errors) {
            this.betaGuessService.addGuess(this.sanitizedGuess())
        }
        this.guess.set('')
    }


    giveUp() {
        this.betaGuessService.giveUp()
    }

    updateWordHints(summary: GameSessionSummary): Hint {
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
    
    private sanitized(guess: string) {
        return guess.toLowerCase()
    }
}
