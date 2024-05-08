import {Component, computed, input} from '@angular/core';
import {Hint} from "./hint.model";
import { JsonPipe } from '@angular/common';

@Component({
    selector: 'app-guess-list',
    standalone: true,
    imports: [JsonPipe],
    template: `
        <div class="flex flex-col text-3xl items-center">
            @for (g of splitWords().show; track g) {
                @if (highlight($first, $last)) {
                    <h2>
                        <a class="underline text-emerald-700 decoration-emerald-400">{{ hints().letters }}</a>{{ g.substring(hints().index) }}
                    </h2>
                } @else {
                    <h2>{{ g }}</h2>
                }
            }
            @empty {
                <h2>...</h2>
            }
        </div>
    `
})
export class GuessListComponent {
    guesses = input.required<string[]>();
    position = input.required<'first' | 'last'>();
    hints = input.required<Hint>()
    splitWords = computed<{collapsed: string[], show: string[]}>(() => this.divide(this.guesses()))
    private readonly LIMIT = 4;

    highlight(first: boolean, last: boolean) {
        switch (this.position()) {
            case 'first':
                return first
            case 'last':
                return last
        }
    }

    shouldShow(index: number, first: boolean, last:boolean): boolean {
        switch(this.position()) {
            case 'first':
                return index < 4
            case 'last':
                return index >= this.guesses().length - this.LIMIT
        }
    }

    splitAt(index: number, guesses: string[]) {
        return [guesses.slice(0, index), guesses.slice(index)]
    }

    divide(guesses: string[]) {
        const index = this.position() == 'first' ? this.LIMIT : guesses.length - this.LIMIT
        const x = this.splitAt(index, guesses)
        switch(this.position()) {
            case 'first':
                return { collapsed: x[1], show: x[0] }
            case 'last':
                return { collapsed: x[0], show: x[1] }
        }
    }

}
