package com.redcare.interview.githubreposcores.model.dto;

import com.redcare.interview.githubreposcores.model.GitHubRepositorySearchResult.GitHubRepositorySearchResultItem;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/** DTO Object for our REST Controller, method: retrieveRepositoryScores. */
public record GetGitHubRepoScoresResponseBodyDto(
    UUID requestId,
    boolean processed,
    Instant processedTimestamp,
    Map<String, String> queryParams,
    List<GitHubRepositorySearchResultItem> repositories) {}
