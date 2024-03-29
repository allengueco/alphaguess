export type GameSessionSummary = {
  error: 'INVALID_WORD' | 'ALREADY_GUESSED' | 'NONE',
  guesses: {
    after: string[],
    before: string[]
  }
  isGameOver: boolean,
  lastSubmissionTimestamp: string
}
