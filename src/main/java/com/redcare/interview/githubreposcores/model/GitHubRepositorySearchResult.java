package com.redcare.interview.githubreposcores.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/** Class, used to map the GitHub API Repository Search response to. */
@Getter
@AllArgsConstructor
public class GitHubRepositorySearchResult {

  @JsonProperty("total_count")
  long totalCount;

  @JsonProperty("incomplete_results")
  boolean incompleteResults;

  @JsonProperty("items")
  List<GitHubRepositorySearchResultItem> items;

  @Getter
  @Setter
  @AllArgsConstructor
  public static class GitHubRepositorySearchResultItem {

    @JsonProperty("id")
    long id;

    @JsonProperty("full_name")
    String fullName;

    @JsonProperty("language")
    String language;

    @JsonProperty("stargazers_count")
    long stars;

    @JsonProperty("forks_count")
    long forks;

    @JsonProperty("created_at")
    Instant createdAt;

    @JsonProperty("updated_at")
    Instant updatedAt;

    @JsonProperty("score")
    double score;
  }
}
