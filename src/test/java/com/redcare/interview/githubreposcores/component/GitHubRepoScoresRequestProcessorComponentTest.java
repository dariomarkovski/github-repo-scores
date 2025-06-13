package com.redcare.interview.githubreposcores.component;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.redcare.interview.githubreposcores.httpservice.GitHubRepositorySearchHttpService;
import com.redcare.interview.githubreposcores.model.GitHubRepositorySearchResult;
import com.redcare.interview.githubreposcores.model.GitHubRepositorySearchResult.GitHubRepositorySearchResultItem;
import com.redcare.interview.githubreposcores.model.entity.GitHubRepoScoresRequestEntity;
import com.redcare.interview.githubreposcores.repository.GitHubRepoScoresRequestRepository;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GitHubRepoScoresRequestProcessorComponentTest {

  @Mock private GitHubRepoScoresRequestRepository repository;
  @Mock private GitHubRepositorySearchHttpService searchHttpService;
  @Mock private GithubRepoScoresCalculatorComponent calculatorComponent;
  @Spy private ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());;

  @InjectMocks private GitHubRepoScoresRequestProcessorComponent processor;

  @Test
  void processGitHubRepoScoresRequest_shouldProcessAndSave() throws JsonProcessingException {
    var now = Instant.now();
    var repo1 =
        new GitHubRepositorySearchResultItem(
            1,
            "repo1",
            "java",
            100,
            50,
            now.minus(10, ChronoUnit.DAYS),
            now.minus(60, ChronoUnit.MINUTES),
            0.5);
    var repo2 =
        new GitHubRepositorySearchResultItem(
            1,
            "repo2",
            "java",
            200,
            30,
            now.minus(10, ChronoUnit.DAYS),
            now.minus(120, ChronoUnit.MINUTES),
            1);
    List<GitHubRepositorySearchResultItem> items = new ArrayList<>();
    items.add(repo1);
    items.add(repo2);
    GitHubRepositorySearchResult result = new GitHubRepositorySearchResult(2, false, items);
    when(searchHttpService.searchRepositories(any())).thenReturn(result);

    GitHubRepoScoresRequestEntity requestEntity =
        GitHubRepoScoresRequestEntity.builder()
            .created(LocalDate.parse("2025-05-01"))
            .language("java")
            .processed(false)
            .build();
    processor.processGitHubRepoScoresRequest(requestEntity);

    assertThat(requestEntity.isProcessed()).isTrue();
    assertThat(requestEntity.getProcessedTimestamp()).isNotNull();
    assertThat(requestEntity.getRepositories()).isNotEmpty();

    verify(calculatorComponent).calculateRepositoryScore(items);
  }
}
