import {inject, Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Trie} from "@kamilmielnik/trie";
import {map, Observable, shareReplay} from "rxjs";

@Injectable({providedIn: "root"})
export class DictionaryService {
    http = inject(HttpClient)
    private dictionaryFp: string = "assets/d.txt"
    private validFp: string = "assets/valid_words.txt"
    private dictionary: Observable<Trie> = this.readFromFile(this.dictionaryFp).pipe(map(Trie.deserialize), shareReplay());
    private validWords: Observable<string[]> = this.readFromFile(this.validFp).pipe(map(w => w.split("\r\n")), shareReplay());

    contains(guess: string) {
        return this.dictionary.pipe(map(d => d.has(guess)))
    }

    randomWord() {
        return this.validWords.pipe(map(w => w[Math.floor(Math.random()) * w.length]))
    }

    private readFromFile(filePath: string): Observable<string> {
        return this.http.get(filePath, {responseType: 'text'});
    }
}
