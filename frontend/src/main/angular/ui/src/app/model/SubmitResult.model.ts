export type SubmitResult = {
  error: 'INVALID_WORD' | 'ALREADY_GUESSED',
  beforeGuesses: string[]
  afterGuesses: string[]
  isGameOver: boolean,
  isSessionExpired: boolean,
  submissionTimestamp: string
}
