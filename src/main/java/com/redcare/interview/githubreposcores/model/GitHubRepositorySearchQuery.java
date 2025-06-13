package com.redcare.interview.githubreposcores.model;

import jakarta.annotation.Nullable;
import java.time.LocalDate;

/** Record that contains the information about the request query params. */
public record GitHubRepositorySearchQuery(@Nullable LocalDate created, @Nullable String language) {}
