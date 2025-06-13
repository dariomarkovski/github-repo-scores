package com.redcare.interview.githubreposcores.model.dto;

import java.time.LocalDate;

/** DTO Object for our REST Controller, method: createGitHubRepoScoresRequest. */
public record PostGitHubRepoScoresRequestBodyDto(LocalDate created, String language) {}
