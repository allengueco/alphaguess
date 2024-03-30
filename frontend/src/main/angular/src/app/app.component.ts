import {ChangeDetectionStrategy, Component, inject, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {RouterOutlet} from '@angular/router';
import {BetaGuessService} from "./beta-guess.service";
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {GameSessionSummary} from "./model/SubmitResult.model";
import {map, Observable, of, share} from 'rxjs';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet, ReactiveFormsModule],
  templateUrl: './app.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush

})
export class AppComponent implements OnInit {
  betaGuessService = inject(BetaGuessService);
  title = 'betaguess';
  submitResult: Observable<GameSessionSummary & { letters: string, index: number }> = of();

  form = new FormGroup(
    {
      guess: new FormControl("", {
        nonNullable: true,
        validators: [Validators.required]
      })
    });

  ngOnInit() {
    this.submitResult = this.betaGuessService.submit()
      .pipe(map(s => ({...s, ...this.updateWordHints(s)})),
        share()
      )
  }

  onSubmit() {
    const guess = this.form.controls.guess.value
    this.submitResult = this.betaGuessService.submit(guess)
      .pipe(map(s => ({...s, ...this.updateWordHints(s)})));
    this.form.controls.guess.reset();
  }

  updateWordHints(summary: GameSessionSummary): { letters: string, index: number } {
    const afterLength = summary.guesses.after.length;
    const top = summary.guesses.after[afterLength - 1] ?? '';
    const bottom = summary.guesses.before[0] ?? '';

    console.log(`top: ${top}, bottom: ${bottom}`);
    const l = Math.min(top.length, bottom.length);
    console.log(l);

    let letters = '';
    let idx = 0;

    for (let i = idx; i < l; i++) {
      if (top.charAt(i) === bottom.charAt(i)) {
        letters += top.charAt(i);
        idx++
      } else {
        break
      }
    }

    return {
      letters: letters,
      index: idx
    }
  }
}
