import {ChangeDetectionStrategy, Component, computed, effect, input, model} from '@angular/core';
import {Hint} from "./hint.model";
import { JsonPipe } from '@angular/common';
import { GuessLimitDirective } from './guess-limit.directive';
@Component({
    selector: 'app-guess-list',
    standalone: true,
    imports: [JsonPipe, GuessLimitDirective],
    template: `
        <div class="flex flex-col text-3xl items-center" appGuessLimit [position]="position()">
            @for (g of guesses(); track g) {
                @if (highlight($first, $last)) {
                    <h2 #guess>
                        <a class="underline text-emerald-700 decoration-emerald-400">{{ hints().letters }}</a>{{ g.substring(hints().index) }}
                    </h2>
                } @else {
                    <h2 #guess>{{ g }}</h2>
                }
            }
            @empty {
                <h2>...</h2>
            }
        </div>
    `
})
export class GuessListComponent {
    guesses = model.required<string[]>();
    position = input.required<'first' | 'last'>();
    hints = input.required<Hint>()
    splitWords = computed(() => this.divide(this.guesses()))
    private readonly LIMIT = 5;

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

    divide(guesses: string[]): {collapsed: string[], show: string[]} {
        const index = this.position() == 'first' ? this.LIMIT : guesses.length - this.LIMIT
        const x = this.splitAt(index, guesses)
        switch(this.position()) {
            case 'first':
                return { collapsed: x[1], show: x[0] }
            case 'last':
                return { collapsed: x[0], show: x[1] }
        }
    }

    currentSplit() {
        return this.splitWords().show
    }
}
