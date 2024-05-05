import {Component, computed, inject, Signal,} from '@angular/core';
import {CommonModule} from '@angular/common';
import {RouterOutlet} from '@angular/router';
import {BetaGuessService} from "./beta-guess.service";
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {GameSessionSummary} from "./guess-session-summary.model";
import {Hint} from "./hint.model";
import {GuessListComponent} from "./guess-list.component";

@Component({
    selector: 'app-root',
    standalone: true,
    imports: [CommonModule, RouterOutlet, ReactiveFormsModule, GuessListComponent],
    templateUrl: './app.component.html'
})
export class AppComponent {
    betaGuessService = inject(BetaGuessService);
    submitResult: Signal<GameSessionSummary> = this.betaGuessService.summary
    before: Signal<string[]> = computed(() => this.submitResult().guesses.before)
    after: Signal<string[]> = computed(() => this.submitResult().guesses.after)
    hints: Signal<Hint> = computed(() => this.updateWordHints(this.submitResult()))

    form = new FormGroup(
        {
            guess: new FormControl("", {
                nonNullable: true,
                validators: [Validators.required]
            })
        });

    onSubmit() {
        const guess = this.form.controls.guess.value
        this.betaGuessService.addGuess(guess)
        this.form.controls.guess.reset("", {onlySelf: true})
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
}
