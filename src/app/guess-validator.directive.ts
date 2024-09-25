import { Directive, inject, input } from '@angular/core';
import {
  FormControl,
  NG_VALIDATORS,
  ValidationErrors,
  Validator,
} from '@angular/forms';
import { DictionaryService } from './dictionary.service';
import { GameSessionSummary } from './guess-session-summary.model';

@Directive({
  selector: '[appGuessValidator]',
  standalone: true,
  providers: [
    {
      provide: NG_VALIDATORS,
      useExisting: GuessValidatorDirective,
      multi: true,
    },
  ],
})
export class GuessValidatorDirective implements Validator {
  dictionaryService = inject(DictionaryService);
  summary = input.required<GameSessionSummary>({ alias: 'appGuessValidator'});

  validate(control: FormControl<string>): ValidationErrors | null {
    if (!control.value) return null;
    const guess = control.value.toLowerCase();
    if (
      this.summary().guesses.after.includes(guess) ||
      this.summary().guesses.before.includes(guess)
    ) {
      return { alreadyGuessed: guess };
    }

    if (!this.dictionaryService.contains(guess)) {
      return { isInvalidWord: guess };
    }
    return null;
  }
}
