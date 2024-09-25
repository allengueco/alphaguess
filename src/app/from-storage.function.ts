import {
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
  defaultIfNull: Required<TValue>
): WritableSignal<TValue> => {
  const storage = inject(StorageService);

  const initialValue = storage.getItem<TValue>(storageKey) ?? defaultIfNull;

  const fromStorageSignal = signal<TValue>(initialValue);

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
    const newValue = storage.getItem<TValue>(storageKey) ?? defaultIfNull;

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
