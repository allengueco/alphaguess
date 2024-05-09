import {inject, Injectable, OnInit} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Trie} from "@kamilmielnik/trie";

@Injectable({providedIn: "root"})
export class DictionaryService {
    http = inject(HttpClient)
    private DICTIONARY_URL: string = "assets/d.txt"
    private dictionary!: Trie;

    init() {
        return this.http.get(this.DICTIONARY_URL, {responseType: 'text'})
            .subscribe(res => this.dictionary = Trie.deserialize(res))
    }

    contains(guess: string) {
        return this.dictionary.has(guess)
    }
}
