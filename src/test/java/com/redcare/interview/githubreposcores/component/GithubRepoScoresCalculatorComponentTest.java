package com.redcare.interview.githubreposcores.component;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

import com.redcare.interview.githubreposcores.model.GitHubRepositorySearchResult.GitHubRepositorySearchResultItem;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GithubRepoScoresCalculatorComponentTest {

  @InjectMocks private GithubRepoScoresCalculatorComponent calculator;

  @Test
  void test_calculateRepositoryScore() {
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
            0);
    var repo2 =
        new GitHubRepositorySearchResultItem(
            2,
            "repo2",
            "java",
            200,
            30,
            now.minus(10, ChronoUnit.DAYS),
            now.minus(120, ChronoUnit.MINUTES),
            0);
    var repo3 =
        new GitHubRepositorySearchResultItem(
            3,
            "repo3",
            "java",
            150,
            70,
            now.minus(10, ChronoUnit.DAYS),
            now.minus(30, ChronoUnit.MINUTES),
            0);
    List<GitHubRepositorySearchResultItem> repos = List.of(repo1, repo2, repo3);

    calculator.calculateRepositoryScore(repos);

    assertThat(repo1.getScore()).isBetween(0.0, 1.0);
    assertThat(repo2.getScore()).isBetween(0.0, 1.0);
    assertThat(repo3.getScore()).isBetween(0.0, 1.0);
  }

  @Test
  void test_calculateRepositoryScore_emptyList() {
    assertThatNoException().isThrownBy(() -> calculator.calculateRepositoryScore(List.of()));
  }

  @Test
  void test_calculateRepositoryScore_sameValues() {
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
            0);
    var repo2 =
        new GitHubRepositorySearchResultItem(
            2,
            "repo2",
            "java",
            100,
            50,
            now.minus(10, ChronoUnit.DAYS),
            now.minus(60, ChronoUnit.MINUTES),
            0);
    List<GitHubRepositorySearchResultItem> repos = List.of(repo1, repo2);

    calculator.calculateRepositoryScore(repos);

    assertThat(repo1.getScore()).isEqualTo(repo2.getScore());
  }
}
