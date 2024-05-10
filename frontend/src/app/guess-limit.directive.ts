import { AfterContentInit, AfterViewChecked, AfterViewInit, contentChildren, Directive, ElementRef, inject, input, Renderer2, viewChildren } from '@angular/core';

@Directive({
  selector: '[appGuessLimit]',
  standalone: true
})
export class GuessLimitDirective implements AfterContentInit {
  limit = input(5)
  renderer = inject(Renderer2)
  position = input.required<'first'|'last'>()

  guesses = contentChildren<ElementRef>('h2')

  ngAfterContentInit(): void {
    this.guesses().forEach((c, i, _) => {
      console.log(c)
      this.renderer.setStyle(c, 'display', 'none')
    })
  }
}
