import {inject, Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable, tap} from "rxjs";
import {SubmitResult} from "./model/SubmitResult.model";

@Injectable({
  providedIn: "root"
})
export class BetaGuessService {
  http = inject(HttpClient);

  submit(guess?: string): Observable<SubmitResult> {
    console.log(`submitting ${guess}`);
    const g = guess ? { guess: guess } : null;

    return this.http.post<SubmitResult>(`/api/submit`, g).pipe(
      tap(console.log)
    )
  }
}
