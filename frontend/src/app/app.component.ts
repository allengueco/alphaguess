import {ChangeDetectionStrategy, Component, computed, inject, Signal,} from '@angular/core';
import {CommonModule} from '@angular/common';
import {RouterOutlet} from '@angular/router';
import {BetaGuessService} from "./beta-guess.service";
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {GameSessionSummary} from "./guess-session-summary.model";
import {Hint} from "./hint.model";

@Component({
    selector: 'app-root',
    standalone: true,
    imports: [CommonModule, RouterOutlet, ReactiveFormsModule],
    templateUrl: './app.component.html',
    changeDetection: ChangeDetectionStrategy.OnPush
})
export class AppComponent {
    betaGuessService = inject(BetaGuessService);
    submitResult: Signal<GameSessionSummary> = this.betaGuessService.currentGame()
    before: Signal<string[]> = computed(() => this.submitResult().guesses.before)
    after: Signal<string[]> = computed(() => this.submitResult().guesses.after)
    hints: Signal<Hint> = this.betaGuessService.currentHints()


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

    getSuccessMessage() {
        return this.betaGuessService.getSuccessMessage()
    }
}
