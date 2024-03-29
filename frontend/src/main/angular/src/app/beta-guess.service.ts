import {inject, Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable, shareReplay, tap} from "rxjs";
import {GameSessionSummary} from "./model/SubmitResult.model";

@Injectable({
  providedIn: "root"
})
export class BetaGuessService {
  http = inject(HttpClient);

  submit(guess?: string): Observable<GameSessionSummary> {
    const g = guess ? {guess: guess} : null;

    return this.http.post<GameSessionSummary>(`/api/submit`, g).pipe(
      tap(console.log),
      shareReplay()
    )
  }
}
