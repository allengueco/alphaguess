import { inject, Inject, InjectionToken, signal } from "@angular/core";

export const STORAGE = new InjectionToken<Storage>('Web Storage Injection Token')

@Inject({ providedIn: 'root' })
export class StorageService<T> {
    readonly storage = inject(STORAGE);

    state = signal({} as T)

    getItem<T>(key: string): T | null {
        const raw = this.storage.getItem(key);
        return raw == null ? null : JSON.parse(raw) as T;
    }

    setItem<T>(key: string, value: T | null) {
        const stringified = JSON.stringify(value)
        this.storage.setItem(key, stringified)

        const storageEvent = new StorageEvent('storage', {
            key: key,
            newValue: stringified,
            storageArea: this.storage
        })

        window.dispatchEvent(storageEvent)
    }
}
