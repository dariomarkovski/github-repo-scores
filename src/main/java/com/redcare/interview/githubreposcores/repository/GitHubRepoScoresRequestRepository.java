package com.redcare.interview.githubreposcores.repository;

import com.redcare.interview.githubreposcores.model.entity.GitHubRepoScoresRequestEntity;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/** Repository for ${@link GitHubRepoScoresRequestEntity}. */
public interface GitHubRepoScoresRequestRepository
    extends JpaRepository<GitHubRepoScoresRequestEntity, UUID> {

  Optional<GitHubRepoScoresRequestEntity> findByCreatedAndLanguage(
      LocalDate created, String language);

  Optional<GitHubRepoScoresRequestEntity> findFirstByProcessedIs(boolean processed);

  List<GitHubRepoScoresRequestEntity> findAllByProcessedTimestampBefore(
      Instant processedTimestampToCompare);
}
