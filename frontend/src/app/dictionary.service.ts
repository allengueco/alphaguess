import {inject, Injectable, OnInit} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Trie} from "@kamilmielnik/trie";
import { firstValueFrom } from "rxjs";

@Injectable({providedIn: "root"})
export class DictionaryService {
    http = inject(HttpClient)
    private DICTIONARY_URL: string = "assets/d.txt"
    private dictionary!: Trie;

    init() {
        return firstValueFrom(this.http.get(this.DICTIONARY_URL, {responseType: 'text'}))
            .then(res => {
                this.dictionary = Trie.deserialize(res)
                return this.dictionary
            })
            .catch(console.error)
    }

    contains(guess: string) {
        return this.dictionary.has(guess)
    }
}
