import {
    CreateSignalOptions,
  DestroyRef,
  effect,
  inject,
  signal,
  untracked,
  type WritableSignal,
} from '@angular/core';

import { StorageService } from './storage.service';
import { isEqual } from 'lodash';

export const fromStorage = <TValue>(
  storageKey: string,
  opts?: CreateSignalOptions<TValue | null>
): WritableSignal<TValue | null> => {
  const storage = inject(StorageService);

  const initialValue = storage.getItem<TValue>(storageKey);

  const fromStorageSignal = signal<TValue | null>(initialValue, opts);

  const writeToStorageOnUpdateEffect = effect(() => {
    const updated = fromStorageSignal();
    untracked(() => storage.setItem(storageKey, updated));
  });

  const storageEventListener = (event: StorageEvent) => {
    const isWatchedValueTargeted = event.key === storageKey;
    if (!isWatchedValueTargeted) {
      return;
    }

    const currentValue = fromStorageSignal();
    const newValue = storage.getItem<TValue>(storageKey);

    const hasValueChanged = !isEqual(newValue, currentValue);
    if (hasValueChanged) {
      fromStorageSignal.set(newValue);
    }
  };

  window.addEventListener('storage', storageEventListener);

  inject(DestroyRef).onDestroy(() => {
    window.removeEventListener('storage', storageEventListener);
  });

  return fromStorageSignal;
};
