import { Injectable, computed, inject } from '@angular/core';
import { GameSessionSummary } from './guess-session-summary.model';
import { WordService } from './word.service';
import { NgForm } from '@angular/forms';
import { fromStorage } from './from-storage.function';
import { Hint } from './hint.model';

@Injectable({
  providedIn: 'root',
})
export class BetaGuessService {
  state = fromStorage<GameSessionSummary>('session', this.defaultState());

  wordService = inject(WordService);

  hints = computed(() => this.updateHints(this.state()));
  before = computed(() => this.state().guesses.before);
  after = computed(() => this.state().guesses.after);

  defaultState() {
    return {
      isGameOver: false,
      guesses: {
        after: [],
        before: [],
      },
      startTime: '',
    };
  }

  updateHints(state: GameSessionSummary): Hint {
    const afterLength = state.guesses.after.length;
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

  submitGuess(guess: string) {
    const timestamp = new Date().toISOString();
    const pos = this.wordService.compare('feeble', guess);

    switch (pos) {
      case 'before':
      case 'after':
        this.state.update(s => {
          const items = s.guesses[pos];
          items.push(guess);
          items.sort();
          const startTime = s.startTime === '' ? timestamp : s.startTime;
          return { ...s, startTime };
        });
        break;
      case 'equal':
        this.state.update(s => ({ ...s, isGameOver: true }));
    }
  }

  reset(guessForm: NgForm) {
    this.state.set(this.defaultState());
    guessForm.reset();
  }
}
