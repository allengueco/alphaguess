import { CommonModule } from '@angular/common';
import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { RouterOutlet } from '@angular/router';
import { GuessListComponent } from './guess-list.component';
import { GuessValidatorDirective } from './guess-validator.directive';
import { BetaGuessService } from './beta-guess.service';

@Component({
    selector: 'app-root',
    standalone: true,
    imports: [
        CommonModule,
        RouterOutlet,
        FormsModule,
        GuessListComponent,
        GuessValidatorDirective,
    ],
    templateUrl: './app.component.html',
})
export class AppComponent {
    betaGuessService = inject(BetaGuessService);
    summary = this.betaGuessService.state
    hints = this.betaGuessService.hints
    before = this.betaGuessService.before
    after = this.betaGuessService.after
    guess = signal('');
    sanitizedGuess = computed(() => this.sanitized(this.guess()));

    onSubmit(guessForm: NgForm) {
        if (!guessForm.form.controls['guess'].errors) {
            this.betaGuessService.addGuess(this.sanitizedGuess());
        }
        guessForm.reset();
    }

    giveUp(guessForm: NgForm) {
        this.betaGuessService.reset();
        guessForm.reset();
    }

    private sanitized(guess: string) {
        return guess.trim().toLowerCase();
    }
}
