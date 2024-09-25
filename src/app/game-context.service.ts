import { inject, Injectable, signal } from '@angular/core';
import { WordService } from './word.service';
import { interval, takeUntil, timer } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class GameContextService {
  readonly options: Intl.DateTimeFormatOptions = {
    timeZone: 'America/New_York',
    year: 'numeric',
    month: 'numeric',
    day: 'numeric',
    hour: 'numeric',
    minute: 'numeric',
    second: 'numeric',
  };
  wordService = inject(WordService);
  todayFormatter = Intl.DateTimeFormat(['en-US'], this.options);

  today = new Date(this.todayFormatter.format(Date.now()));

  startOfNextDay = new Date();
  newDay$ = interval(1000).pipe(takeUntil(timer(this.startOfNextDay)));
}
