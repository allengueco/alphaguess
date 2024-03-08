import {Component, inject} from '@angular/core';
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
export class AppComponent {
  betaGuessService = inject(BetaGuessService);
  title = 'betaguess';

  submitResult: Observable<SubmitResult> = this.betaGuessService.currentSession();

  errorValidator: AsyncValidatorFn = (form) => {
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

  onSubmit() {
    const guess = this.form.controls.guess.value
    this.submitResult = this.betaGuessService.submit(guess);
    this.form.reset();
  }
}
