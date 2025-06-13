package com.redcare.interview.githubreposcores;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Spring Boot Application that provides a list of GitHub repositories based on a user's query and
 * scores them according to the stars, forks and the time since the last update. Requests are served
 * and processed asynchronously, so that we can handle the limit of GitHub API.
 */
@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
public class GithubRepoScoresApplication {

  public static void main(String[] args) {
    SpringApplication.run(GithubRepoScoresApplication.class, args);
  }
}
