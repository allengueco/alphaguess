import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  computed,
  inject,
  signal,
} from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { RouterOutlet } from '@angular/router';
import { GuessListComponent } from './guess-list.component';
import { GuessValidatorDirective } from './guess-validator.directive';
import { BetaGuessService } from './beta-guess.service';
import { WordHighlightComponent } from './word-highlight.component';
import { fromStorage } from './from-storage.function';
import { GameSessionSummary } from './guess-session-summary.model';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    CommonModule,
    RouterOutlet,
    FormsModule,
    GuessListComponent,
    GuessValidatorDirective,
    WordHighlightComponent,
  ],
  templateUrl: './app.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class AppComponent {
  betaGuessService = inject(BetaGuessService);
  summary = this.betaGuessService.state;
  hints = this.betaGuessService.hints;
  before = this.betaGuessService.before;
  after = this.betaGuessService.after;
  guess = signal('');
  sanitizedGuess = computed(() => this.sanitized(this.guess()));

  onSubmit(guessForm: NgForm) {
    if (guessForm.valid) {
      this.betaGuessService.submitGuess(this.sanitizedGuess());
    }
    guessForm.reset();
  }

  giveUp(guessForm: NgForm) {
    this.betaGuessService.reset(guessForm);
    guessForm.reset();
  }

  private sanitized(guess: string) {
    return guess.trim().toLowerCase();
  }
}
