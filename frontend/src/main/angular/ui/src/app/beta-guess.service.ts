import {inject, Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

@Injectable({
  providedIn: "root"
})
export class BetaGuessService {
  http = inject(HttpClient);

  submit(guess: string): Observable<SubmitResult> {
    return this.http.post<SubmitResult>(`/api/submit`, {guess: guess})
  }
}
