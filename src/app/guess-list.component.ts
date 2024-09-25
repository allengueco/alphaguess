import {
  ChangeDetectionStrategy,
  Component,
  computed,
  input,
  model,
} from '@angular/core';
import { Hint } from './hint.model';
import { JsonPipe } from '@angular/common';
import { GuessLimitDirective } from './guess-limit.directive';
import { WordHighlightComponent } from "./word-highlight.component";
@Component({
  selector: 'app-guess-list',
  standalone: true,
  imports: [JsonPipe, GuessLimitDirective, WordHighlightComponent],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div
      class="flex flex-col text-3xl items-center"
      appGuessLimit
      [position]="position()">
      @for (g of guesses(); track g) {
        @if (highlight($first, $last)) {
            <app-word-highlight [guess]="g" [hints]="hints()"></app-word-highlight>
        } @else {
          <h2>{{ g }}</h2>
        }
      } @empty {
        <h2>...</h2>
      }
    </div>
  `,
})
export class GuessListComponent {
  guesses = model.required<string[]>();
  position = input.required<'first' | 'last'>();
  hints = input.required<Hint>();
  splitWords = computed(() => this.divide(this.guesses()));
  private readonly LIMIT = 5;

  highlight(first: boolean, last: boolean) {
    switch (this.position()) {
      case 'first':
        return first;
      case 'last':
        return last;
    }
  }

  shouldShow(index: number): boolean {
    switch (this.position()) {
      case 'first':
        return index < this.LIMIT - 1;
      case 'last':
        return index >= this.guesses().length - this.LIMIT;
    }
  }

  splitAt(index: number, guesses: string[]) {
    return [guesses.slice(0, index), guesses.slice(index)];
  }

  divide(guesses: string[]): { collapsed: string[]; show: string[] } {
    const index =
      this.position() == 'first' ? this.LIMIT : guesses.length - this.LIMIT;
    const x = this.splitAt(index, guesses);
    switch (this.position()) {
      case 'first':
        return { collapsed: x[1], show: x[0] };
      case 'last':
        return { collapsed: x[0], show: x[1] };
    }
  }

  currentSplit() {
    return this.splitWords().show;
  }
}
