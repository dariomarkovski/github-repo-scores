package com.redcare.interview.githubreposcores.component;

import com.redcare.interview.githubreposcores.model.GitHubRepositorySearchResult.GitHubRepositorySearchResultItem;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Component, that calculates the scores of a list of repositories. The score is calculated relative
 * to the other elements in the list. For each property (stars, forks, time since update) we get the
 * min and max values from the repositories and calculate the score relative to those.
 *
 * <p>The stars score of a repository would be: (stars - minStars) / (maxStars - minStars).
 *
 * <p>For the time since update, we invert the value, since we want the smallest value to be the
 * best score.
 *
 * <p>The weights chosen are 0.55 for stars, 0.35 for forks and 0.1 for recency.
 */
@Component
@Slf4j
public class GithubRepoScoresCalculatorComponent {

  public void calculateRepositoryScore(List<GitHubRepositorySearchResultItem> repositories) {
    MinAndMaxInformation minAndMaxInformation = findMinAndMaxInformation(repositories);
    repositories.forEach(
        repository -> calculateSingleRepositoryScore(repository, minAndMaxInformation));
  }

  private void calculateSingleRepositoryScore(
      GitHubRepositorySearchResultItem repository, MinAndMaxInformation minAndMaxInformation) {
    // calculate stars score
    double starScore =
        calculateSingleRepositoryFieldScore(
            repository.getStars(), minAndMaxInformation.maxStars, minAndMaxInformation.minStars);
    double weightedStarScore = 0.55 * starScore;
    // calculate forks score
    double forksScore =
        calculateSingleRepositoryFieldScore(
            repository.getForks(), minAndMaxInformation.maxForks, minAndMaxInformation.minForks);
    // calculate time score
    double weightedForksScore = 0.35 * forksScore;
    double timeScore =
        1
            - calculateSingleRepositoryFieldScore(
                millisSinceUpdate(
                    repository.getUpdatedAt(), minAndMaxInformation.valueForComparingTimes),
                minAndMaxInformation.maxTime,
                minAndMaxInformation.minTime);
    double weightedTimeScore = 0.1 * timeScore;
    // calculate total score
    double finalScore = weightedStarScore + weightedForksScore + weightedTimeScore;
    repository.setScore(finalScore);
  }

  private double calculateSingleRepositoryFieldScore(long value, long maxValue, long minValue) {
    long delta = maxValue - minValue;
    if (delta == 0) {
      return 0.5;
    }
    return ((double) value - minValue) / delta;
  }

  private MinAndMaxInformation findMinAndMaxInformation(
      List<GitHubRepositorySearchResultItem> repositories) {
    long maxStars = 0;
    long maxForks = 0;
    long maxTime = 0;
    long minStars = Long.MAX_VALUE;
    long minForks = Long.MAX_VALUE;
    long minTime = Long.MAX_VALUE;
    Instant valueForComparingTimes = Instant.now();
    for (GitHubRepositorySearchResultItem repository : repositories) {
      long millisSinceUpdate = millisSinceUpdate(repository.getUpdatedAt(), valueForComparingTimes);
      if (repository.getStars() > maxStars) {
        maxStars = repository.getStars();
      }
      if (repository.getForks() > maxForks) {
        maxForks = repository.getForks();
      }
      if (millisSinceUpdate > maxTime) {
        maxTime = millisSinceUpdate;
      }
      if (repository.getStars() < minStars) {
        minStars = repository.getStars();
      }
      if (repository.getForks() < minForks) {
        minForks = repository.getForks();
      }
      if (millisSinceUpdate < minTime) {
        minTime = millisSinceUpdate;
      }
    }
    return new MinAndMaxInformation(
        maxStars, minStars, maxForks, minForks, maxTime, minTime, valueForComparingTimes);
  }

  private long millisSinceUpdate(Instant updatedAt, Instant compareToValue) {
    return Duration.between(updatedAt, compareToValue).toMillis();
  }

  private record MinAndMaxInformation(
      long maxStars,
      long minStars,
      long maxForks,
      long minForks,
      long maxTime,
      long minTime,
      Instant valueForComparingTimes) {}
}
