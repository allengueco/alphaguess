import {inject, Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Trie} from "@kamilmielnik/trie";

@Injectable({providedIn: "root"})
export class DictionaryService {
    http = inject(HttpClient)
    private dictionaryFp: string = "assets/d.txt"
    private dictionary!: Trie;

    constructor() {
        this.http.get(this.dictionaryFp, {responseType: 'text'}).subscribe(res => this.dictionary = Trie.deserialize(res))
    }

    contains(guess: string) {
        return this.dictionary.has(guess)
    }
}
