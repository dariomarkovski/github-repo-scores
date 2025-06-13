package com.redcare.interview.githubreposcores.model.dto;

import com.redcare.interview.githubreposcores.model.GitHubRepositorySearchResult.GitHubRepositorySearchResultItem;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record GetGitHubRepoScoresResponseBodyDto(
    UUID requestId,
    boolean processed,
    Instant processedTimestamp,
    List<GitHubRepositorySearchResultItem> repositories) {}
