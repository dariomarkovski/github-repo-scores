package com.redcare.interview.githubreposcores.scheduler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.redcare.interview.githubreposcores.component.GitHubRepoScoresRequestProcessorComponent;
import com.redcare.interview.githubreposcores.model.entity.GitHubRepoScoresRequestEntity;
import com.redcare.interview.githubreposcores.repository.GitHubRepoScoresRequestRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/** Scheduler that processes the GitHub Repo Scores requests. Executes every 2 seconds. */
@Service
@RequiredArgsConstructor
@Slf4j
public class GitHubRepoScoresRequestHandlerScheduler {

  private final GitHubRepoScoresRequestRepository gitHubRepoScoresRequestRepository;
  private final GitHubRepoScoresRequestProcessorComponent gitHubRepoScoresRequestProcessorComponent;

  @Scheduled(fixedDelay = 2_000)
  public void scheduled() throws JsonProcessingException {
    Optional<GitHubRepoScoresRequestEntity> optionalUnprocessedRequest =
        gitHubRepoScoresRequestRepository.findByProcessedIs(false);
    if (optionalUnprocessedRequest.isPresent()) {
      GitHubRepoScoresRequestEntity unprocessedRequest = optionalUnprocessedRequest.get();
      gitHubRepoScoresRequestProcessorComponent.processGitHubRepoScoresRequest(unprocessedRequest);
    }
  }
}
