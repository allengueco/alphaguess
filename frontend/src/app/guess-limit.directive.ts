import { AfterContentChecked, AfterContentInit, AfterViewChecked, AfterViewInit, contentChildren, Directive, ElementRef, inject, input, Renderer2, viewChildren } from '@angular/core';

@Directive({
  selector: '[appGuessLimit]',
  standalone: true
})
export class GuessLimitDirective implements AfterViewInit {
  limit = input(5)
  renderer = inject(Renderer2)
  position = input.required<'first'|'last'>()

  guesses = viewChildren<ElementRef>('h2')

  ngAfterViewInit(): void {
    this.guesses().forEach((c, i, _) => {
      this.renderer.setStyle(c, 'display', 'none')
    })
  }
}
