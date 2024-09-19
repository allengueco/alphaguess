import { APP_INITIALIZER, ApplicationConfig } from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import { HttpClient, provideHttpClient } from '@angular/common/http';
import { DictionaryService } from './dictionary.service';
import { WordService } from './word.service';

export function loadDictionary(dictionaryService: DictionaryService) {
  return () => dictionaryService.init();
}

export function loadWords(wordService: WordService) {
  return () => wordService.init();
}

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes),
    provideHttpClient(),
    {
      provide: APP_INITIALIZER,
      useFactory: loadDictionary,
      deps: [DictionaryService, HttpClient],
      multi: true,
    },
    {
      provide: APP_INITIALIZER,
      useFactory: loadWords,
      deps: [WordService, HttpClient],
      multi: true,
    },
  ],
};
