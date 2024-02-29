package org.allengueco.dto;

import java.util.List;

/**
 *
 */
public record SubmissionSummary(List<String> before, List<String> after,

                                String guess, boolean isGameOver) {
}
