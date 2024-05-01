import {inject, Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Trie} from "@kamilmielnik/trie";
import {Observable} from "rxjs";

@Injectable({providedIn: "root"})
export class DictionaryService {
    http = inject(HttpClient)
    private dictionaryFp: string = "assets/d.txt"
    private validFp: string = "assets/valid_words.txt"
    private dictionary: Trie = new Trie();
    private validWords: string[] = [];

    constructor() {
        this.readFromFile(this.dictionaryFp).subscribe(d =>
            this.dictionary = Trie.deserialize(d)
        )
        this.readFromFile(this.validFp).subscribe(d =>
            this.validWords = d.split("\r\n")
        )
    }

    contains(guess: string) {
        return this.dictionary.has(guess)
    }

    randomWord() {
        const chosen = this.validWords[Math.floor(Math.random() * this.validWords.length)]

        console.log(chosen)
        return chosen
    }

    private readFromFile(filePath: string): Observable<string> {
        return this.http.get(filePath, {responseType: 'text'});
    }
}
