import { computed, effect, Inject, Signal, signal } from "@angular/core";

@Inject({ providedIn: 'root' })
export class LocalStorageStoreService<T> {
    state = signal({} as T)
    constructor() {
        effect(() => {

        })
    }

    public set<K extends keyof T>(key: K, data: T[K]) {
        this.state.update(current => ({ ...current, [key]: data }))
    }

    public setState(partialState: Partial<T>) {
        this.state.update(current => ({ ...current, ...partialState }))
    }

    public select<K extends keyof T>(key: K): Signal<T[K]> {
        return computed(() => this.state()[key])
    }



}
