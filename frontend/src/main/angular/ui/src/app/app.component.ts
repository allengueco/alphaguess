import {Component, inject, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {RouterOutlet} from '@angular/router';
import {BetaGuessService} from "./beta-guess.service";
import {AsyncValidatorFn, FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {SubmitResult} from "./model/SubmitResult.model";
import {map, Observable, of} from 'rxjs';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet, ReactiveFormsModule],
  templateUrl: './app.component.html',
})
export class AppComponent implements OnInit {
  betaGuessService = inject(BetaGuessService);
  title = 'betaguess';

  submitResult: Observable<SubmitResult> = of();

  errorValidator: AsyncValidatorFn = (_form) => {
    return this.submitResult.pipe(
      map(r => ({
        submitError: r.error
      })));
  }

  form = new FormGroup(
    {
      guess: new FormControl("", {
        nonNullable: true,
        validators: [Validators.required],
        asyncValidators: this.errorValidator
      })
    });

  ngOnInit() {
    this.submitResult = this.betaGuessService.submit()
  }

  onSubmit() {
    const guess = this.form.controls.guess.value
    this.submitResult = this.betaGuessService.submit(guess);
    this.form.reset();
  }
}
