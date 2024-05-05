import {ChangeDetectionStrategy, Component, input} from '@angular/core';
import {Hint} from "./hint.model";

@Component({
    selector: 'app-guess-list',
    standalone: true,
    imports: [],
    template: `
        <div class="text-3xl">
            @for (g of guesses(); track $index) {
                @if (highlight($first, $last)) {
                    <h2>
                        <a class="underline text-emerald-700 decoration-emerald-400">{{ hints().letters }}</a>{{ g.substring(hints().index) }}
                    </h2>
                } @else {
                    <h2>{{ g }}</h2>
                }
            }
        </div>
    `,
    changeDetection: ChangeDetectionStrategy.OnPush
})
export class GuessListComponent {
    guesses = input.required<string[]>();
    position = input.required<'first' | 'last'>();
    hints = input.required<Hint>()

    highlight(first: boolean, last: boolean) {
        switch (this.position()) {
            case 'first':
                return first
            case 'last':
                return last
        }
    }
}
