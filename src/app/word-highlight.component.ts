import { ChangeDetectionStrategy, Component, input } from '@angular/core';
import { Hint } from './hint.model';

@Component({
  selector: 'app-word-highlight',
  standalone: true,
  imports: [],
  template: `
    <h2>
      <a class="underline text-emerald-700 decoration-emerald-400">{{
        hints().letters
      }}</a
      >{{ guess().substring(hints().index) }}
    </h2>
  `,
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class WordHighlightComponent {
  guess = input.required<string>();
  hints = input.required<Hint>();
}
