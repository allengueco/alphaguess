import {Component, Input} from '@angular/core';
import {Hint} from "./hint.model";

@Component({
    selector: 'app-guess-list',
    standalone: true,
    imports: [],
    template: `
        <div class="text-3xl">
            @for (g of guesses; track $index) {
                @if (highlight($first, $last)) {
                    <h2>
                        <a class="underline text-emerald-700 decoration-emerald-400">{{ hints.letters }}</a>{{ g.substring(hints.index) }}
                    </h2>
                } @else {
                    <h2>{{ g }}</h2>
                }
            }
        </div>
    `
})
export class GuessListComponent {
    @Input({required: true}) guesses: string[] = [];
    @Input({required: true}) position: 'first' | 'last' = 'first';
    @Input({required: true}) hints: Hint = {letters: "", index: -1};

    highlight(first: boolean, last: boolean) {
        switch (this.position) {
            case 'first':
                return first
            case 'last':
                return last
        }
    }
}
