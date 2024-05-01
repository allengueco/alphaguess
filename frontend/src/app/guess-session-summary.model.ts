export type GameSessionSummary = {
    guesses: {
        after: string[],
        before: string[]
    }
    isGameOver: boolean,
    lastSubmissionTimestamp: string,
    hints: {
        letters: string,
        index: number
    }
}
