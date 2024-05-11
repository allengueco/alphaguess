import { computed, inject, Injectable, signal } from '@angular/core';
import { WordService } from './word.service';
import { SessionService } from './session.service';
import { DictionaryService } from './dictionary.service';

@Injectable({
  providedIn: 'root',
})
export class BetaGuessService {
  wordService = inject(WordService);
  sessionService = inject(SessionService);
  dictionaryService = inject(DictionaryService);
  readonly summary = signal(this.sessionService.currentGameOrDefault());
  private startTime = signal(this.summary().startTime);
  private currentWord = computed(() =>
    this.wordService.wordOfTheDay(new Date(this.startTime() || Date.now()))
  );

  addGuess(guess: string) {
    const pos = this.wordService.compare(this.currentWord(), guess);
    switch (pos) {
      case 'before':
      case 'after':
        this.summary.update(s => {
          const items = s.guesses[pos];
          items.push(guess);
          items.sort();
          const guesses = { ...s.guesses };
          const startTime = (s.startTime ||= new Date().toString());
          return { ...s, guesses, startTime };
        });
        break;
      case 'equal':
        this.summary.update(s => ({ ...s, isGameOver: true }));
    }
    this.sessionService.update(this.summary());
  }

  giveUp() {
    this.sessionService.reset();
  }
}
