import { HttpClient } from '@angular/common/http';
import { computed, inject, Injectable, signal } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class WordService {
  http = inject(HttpClient);
  private readonly WORDS_URL = 'assets/vw.txt';
  private validWords: string[] = [];
  readonly startDate: Date = new Date(2024, 0, 0); //jan 1st
  readonly startOffset: number = 1337; // random number to offset

  init() {
    this.http
      .get(this.WORDS_URL, { responseType: 'text' })
      .subscribe(res => (this.validWords = res.split('\n')));
  }

  /**
   * Assuming inputs are sanitized
   * @param w1
   * @param w2
   */
  compare(w1: string, w2: string): 'before' | 'after' | 'equal' {
    const c: number = w1.localeCompare(w2);

    if (c > 0) {
      return 'after';
    } else if (c < 0) {
      return 'before';
    } else {
      return 'equal';
    }
  }

  /**
   * @param today
   */
  wordOfTheDay(today: Date): string {
    const todayDiff = this.dateDiff(this.startDate, today);
    const todayIndex = this.startOffset + todayDiff;
    const chosen = this.validWords[todayIndex % this.validWords.length]; // we mod

    console.log(`chosen word: ${chosen}`);
    return chosen;
  }

  /**
   * Quick and dirty implementation of a date diff
   * How far is today's date from January 1st, 2024?
   * @param d1
   * @param d2
   */
  dateDiff(d1: Date, d2: Date): number {
    const ONE_DAY = 24 * 60 * 60 * 1000; // 1 day in ms

    return Math.round(Math.abs((d2.valueOf() - d1.valueOf()) / ONE_DAY));
  }
}
