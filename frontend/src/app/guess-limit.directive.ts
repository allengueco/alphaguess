import { Directive, ElementRef, input, viewChildren } from '@angular/core';

@Directive({
  selector: '[appGuessLimit]',
  standalone: true
})
export class GuessLimitDirective {
  limit = input(5)
  position = input.required<'first'|'last'>()

  guesses = viewChildren<ElementRef>('h2')
}
