import {inject, Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";

@Injectable({
    providedIn: "root"
})
export class WordService {
    http = inject(HttpClient)
    validWordsTxt = "assets/vw.txt"
    successTxt = "assets/success.txt"
    validWords: string[] = [];
    successWords: string[] = [];
    readonly startDate: Date = new Date(2024, 0, 0) //jan 1st
    readonly startOffset: number = 1337;

    constructor() {
        this.http.get(this.validWordsTxt, {responseType: 'text'})
            .subscribe(res =>
                this.validWords = res.split("\r\n")
            )

        this.http.get(this.validWordsTxt, {responseType: 'text'})
            .subscribe(res =>
                this.successWords = res.split("\r\n")
            )
    }

    /**
     * Assuming inputs are sanitized
     * @param w1
     * @param w2
     */
    compare(w1: string, w2: string): 'before' | 'after' | 'equal' {
        const c: number = w1.localeCompare(w2);

        if (c > 0) {
            return 'after'
        } else if (c < 0) {
            return 'before'
        } else {
            return 'equal'
        }
    }


    randomWord() {
        return this.validWords[Math.floor(Math.random() * this.validWords.length)]
    }

    /**
     * How far is today's date from January 1st, 2024?
     * @param today
     */
    wordOfTheDay(today: Date) {

        const todayDiff = this.dateDiff(this.startDate, today);
        const todayIndex = this.startOffset + todayDiff
        const chosen = this.validWords[todayIndex % this.validWords.length] // we mod

        console.log(`chosen word: ${chosen}`)
        return chosen
    }

    randomSuccessMessage() {
        return this.successWords[Math.floor(Math.random() * this.successTxt.length)]
    }

    /**
     * Quick and dirty implementation of a date diff
     * @param d1
     * @param d2
     */
    dateDiff(d1: Date, d2: Date) {
        const ONE_DAY = 24 * 60 * 60 * 1000; // 1 day in ms

        return Math.round(Math.abs((d2.valueOf() - d1.valueOf()) / ONE_DAY))
    }

}