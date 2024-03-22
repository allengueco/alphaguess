export type SubmitResult = {
  error: 'INVALID_WORD' | 'ALREADY_GUESSED' | 'NONE',
  guesses: {
    after: string[],
    before: string[]
  }
  isGameOver: boolean,
  lastSubmissionTimestamp: string
}
