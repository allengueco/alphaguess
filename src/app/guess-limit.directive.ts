import {
  AfterViewChecked,
  Directive,
  inject,
  input,
  Renderer2,
  viewChildren,
} from '@angular/core';

@Directive({
  selector: '[appGuessLimit]',
  standalone: true,
})
export class GuessLimitDirective implements AfterViewChecked {
  limit = input(5);
  renderer = inject(Renderer2);
  position = input.required<'first' | 'last'>();

  guesses = viewChildren<HTMLHeadingElement>('guess');

  ngAfterViewChecked(): void {
    this.guesses().forEach(c => {
      console.log(c);
      this.renderer.setStyle(c, 'fontSize', 'x-large');
    });
  }
}
