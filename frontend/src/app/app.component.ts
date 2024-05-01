import {ChangeDetectionStrategy, Component, inject,} from '@angular/core';
import {CommonModule} from '@angular/common';
import {RouterOutlet} from '@angular/router';
import {BetaGuessService} from "./beta-guess.service";
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {GameSessionSummary} from "./guess-session-summary.model";
import {Observable} from 'rxjs';

@Component({
    selector: 'app-root',
    standalone: true,
    imports: [CommonModule, RouterOutlet, ReactiveFormsModule],
    templateUrl: './app.component.html',
    changeDetection: ChangeDetectionStrategy.OnPush
})
export class AppComponent {
    betaGuessService = inject(BetaGuessService);
    title = 'betaguess';
    submitResult: Observable<GameSessionSummary> = this.betaGuessService.currentGame();

    form = new FormGroup(
        {
            guess: new FormControl("", {
                nonNullable: true,
                validators: [Validators.required]
            })
        });

    onSubmit() {
        const guess = this.form.controls.guess.value
        // TODO: sanitize inputs here
        this.betaGuessService.addGuess(guess)
        this.form.controls.guess.reset("", {onlySelf: true})
    }

    reset() {
        this.betaGuessService.reset()
    }
}
