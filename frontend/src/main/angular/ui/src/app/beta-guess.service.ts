import {inject, Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable, share, shareReplay, tap} from "rxjs";
import {SubmitResult} from "./model/SubmitResult.model";

@Injectable({
  providedIn: "root"
})
export class BetaGuessService {
  http = inject(HttpClient);

  submit(guess: string): Observable<SubmitResult> {
    console.log(`submitting ${guess}`);

    return this.http.post<SubmitResult>(`/api/submit`, {guess: guess}).pipe(
      tap(console.log)
    )
  }

  currentSession(): Observable<SubmitResult> {
    return this.http.get<SubmitResult>(`/api/current-session`).pipe(
      tap(console.log)
    )
  }
}
