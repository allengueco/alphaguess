import {Injectable} from "@angular/core";

@Injectable({
    providedIn: "root"
})
export class WordService {

    /**
     * Assuming inputs are sanitized
     * @param w1
     * @param w2
     */
    compare(w1: string, w2: string): 'before' | 'after' | 'equal' {
        const c: number = w1.localeCompare(w2);

        if (c > 0) {
            return 'after'
        } else if (c < 0) {
            return 'before'
        } else {
            return 'equal'
        }
    }

}
