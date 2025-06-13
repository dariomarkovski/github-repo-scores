package com.redcare.interview.githubreposcores.model.dto;

import java.time.LocalDate;

public record PostGitHubRepoScoresRequestBodyDto(LocalDate created, String language) {}
