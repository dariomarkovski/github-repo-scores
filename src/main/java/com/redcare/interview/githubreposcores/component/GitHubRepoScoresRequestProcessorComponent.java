package com.redcare.interview.githubreposcores.component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.redcare.interview.githubreposcores.httpservice.GitHubRepositorySearchHttpService;
import com.redcare.interview.githubreposcores.model.GitHubRepositorySearchQuery;
import com.redcare.interview.githubreposcores.model.GitHubRepositorySearchResult;
import com.redcare.interview.githubreposcores.model.GitHubRepositorySearchResult.GitHubRepositorySearchResultItem;
import com.redcare.interview.githubreposcores.model.entity.GitHubRepoScoresRequestEntity;
import com.redcare.interview.githubreposcores.repository.GitHubRepoScoresRequestRepository;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class GitHubRepoScoresRequestProcessorComponent {

  private final GitHubRepoScoresRequestRepository gitHubRepoScoresRequestRepository;
  private final GitHubRepositorySearchHttpService gitHubRepositorySearchHttpService;
  private final GithubRepoScoresCalculatorComponent githubRepoScoresCalculatorComponent;
  private final ObjectMapper objectMapper;

  public void processGitHubRepoScoresRequest(GitHubRepoScoresRequestEntity unprocessedRequest)
      throws JsonProcessingException {
    GitHubRepositorySearchQuery query =
        new GitHubRepositorySearchQuery(
            unprocessedRequest.getCreated(), unprocessedRequest.getLanguage());
    GitHubRepositorySearchResult gitHubRepositorySearchResult =
        gitHubRepositorySearchHttpService.searchRepositories(query);
    List<GitHubRepositorySearchResultItem> repositories = gitHubRepositorySearchResult.getItems();

    githubRepoScoresCalculatorComponent.calculateRepositoryScore(repositories);
    repositories.sort(Comparator.comparingDouble(GitHubRepositorySearchResultItem::getScore));

    unprocessedRequest.setRepositories(objectMapper.writeValueAsString(repositories));
    unprocessedRequest.setProcessed(true);
    unprocessedRequest.setProcessedTimestamp(Instant.now());
    gitHubRepoScoresRequestRepository.save(unprocessedRequest);
  }
}
