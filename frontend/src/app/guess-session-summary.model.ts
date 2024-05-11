export type GameSessionSummary = {
  guesses: {
    after: string[];
    before: string[];
  };
  isGameOver: boolean;
  startTime: string;
};
