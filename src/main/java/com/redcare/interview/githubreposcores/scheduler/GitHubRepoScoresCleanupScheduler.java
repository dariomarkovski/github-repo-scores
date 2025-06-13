package com.redcare.interview.githubreposcores.scheduler;

import com.redcare.interview.githubreposcores.model.entity.GitHubRepoScoresRequestEntity;
import com.redcare.interview.githubreposcores.repository.GitHubRepoScoresRequestRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/** Scheduler, that cleans up requests older than 5 minutes. Executes every minute. */
@Service
@RequiredArgsConstructor
@Slf4j
public class GitHubRepoScoresCleanupScheduler {

  private final GitHubRepoScoresRequestRepository gitHubRepoScoresRequestRepository;

  @Scheduled(fixedDelay = 60_000)
  public void scheduled() {
    Instant processedTimestampToCompare = Instant.now().minus(5, ChronoUnit.MINUTES);
    List<GitHubRepoScoresRequestEntity> cleanupList =
        gitHubRepoScoresRequestRepository.findAllByProcessedTimestampBefore(
            processedTimestampToCompare);
    gitHubRepoScoresRequestRepository.deleteAll(cleanupList);
  }
}
