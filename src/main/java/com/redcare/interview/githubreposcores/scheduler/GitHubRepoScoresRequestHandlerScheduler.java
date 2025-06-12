package com.redcare.interview.githubreposcores.scheduler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.redcare.interview.githubreposcores.component.GitHubRepositoriesRetrieverComponent;
import com.redcare.interview.githubreposcores.component.GithubRepoScoresCalculatorComponent;
import com.redcare.interview.githubreposcores.model.GitHubRepositorySearchQuery;
import com.redcare.interview.githubreposcores.model.GitHubRepositorySearchResult.GitHubRepositorySearchResultItem;
import com.redcare.interview.githubreposcores.model.entity.GitHubRepoScoresRequestEntity;
import com.redcare.interview.githubreposcores.repository.GitHubRepoScoresRequestRepository;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class GitHubRepoScoresRequestHandlerScheduler {

  private final GitHubRepoScoresRequestRepository gitHubRepoScoresRequestRepository;
  private final GitHubRepositoriesRetrieverComponent gitHubRepositoriesRetrieverComponent;
  private final GithubRepoScoresCalculatorComponent githubRepoScoresCalculatorComponent;
  private final ObjectMapper objectMapper;

  @Scheduled(fixedDelay = 2_000)
  public void scheduled() throws JsonProcessingException {
    Optional<GitHubRepoScoresRequestEntity> optionalUnprocessedRequest = gitHubRepoScoresRequestRepository.findByProcessedIs(
        false);
    if (optionalUnprocessedRequest.isPresent()) {
      GitHubRepoScoresRequestEntity unprocessedRequest = optionalUnprocessedRequest.get();
      List<GitHubRepositorySearchResultItem> searchResult = gitHubRepositoriesRetrieverComponent.retrieveRepositoriesForQuery(
          new GitHubRepositorySearchQuery(unprocessedRequest.getCreated(),
              unprocessedRequest.getLanguage()));

      githubRepoScoresCalculatorComponent.calculateRepositoryScore(searchResult);
      searchResult.sort(Comparator.comparingDouble(GitHubRepositorySearchResultItem::getScore));

      unprocessedRequest.setSearchResult(objectMapper.writeValueAsString(searchResult));
      unprocessedRequest.setProcessed(true);
      unprocessedRequest.setProcessedTimestamp(Instant.now());

      gitHubRepoScoresRequestRepository.save(unprocessedRequest);
    }
  }

}
