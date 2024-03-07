import {inject, Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable, shareReplay} from "rxjs";
import {SubmitResult} from "./model/SubmitResult.model";

@Injectable({
  providedIn: "root"
})
export class BetaGuessService {
  http = inject(HttpClient);

  submit(guess: string): Observable<SubmitResult> {
    console.log(`submitting ${guess}`);

    return this.http.post<SubmitResult>(`/api/submit`, guess).pipe(shareReplay(1))
  }
}
