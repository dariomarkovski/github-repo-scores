package com.redcare.interview.githubreposcores.component;

import com.redcare.interview.githubreposcores.httpservice.GitHubRepositorySearchHttpService;
import com.redcare.interview.githubreposcores.model.GitHubRepositorySearchQuery;
import com.redcare.interview.githubreposcores.model.GitHubRepositorySearchResult;
import com.redcare.interview.githubreposcores.model.GitHubRepositorySearchResult.GitHubRepositorySearchResultItem;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class GitHubRepositoriesRetrieverComponent {

  public final GitHubRepositorySearchHttpService gitHubRepositorySearchHttpService;

  public List<GitHubRepositorySearchResultItem> retrieveRepositoriesForQuery(
      GitHubRepositorySearchQuery query) {
    GitHubRepositorySearchResult gitHubRepositorySearchResult = gitHubRepositorySearchHttpService.searchRepositories(
        query);
    return gitHubRepositorySearchResult.getItems();
  }

}
