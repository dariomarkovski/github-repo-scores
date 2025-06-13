package com.redcare.interview.githubreposcores.httpservice;

import com.redcare.interview.githubreposcores.model.GitHubRepositorySearchQuery;
import com.redcare.interview.githubreposcores.model.GitHubRepositorySearchResult;
import org.springframework.web.service.annotation.GetExchange;

/** The GitHub API. */
public interface GitHubRepositorySearchHttpService {

  @GetExchange("/search/repositories")
  GitHubRepositorySearchResult searchRepositories(GitHubRepositorySearchQuery query);
}
