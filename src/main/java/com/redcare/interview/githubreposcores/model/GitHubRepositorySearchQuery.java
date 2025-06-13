package com.redcare.interview.githubreposcores.model;

import jakarta.annotation.Nullable;
import java.time.LocalDate;

public record GitHubRepositorySearchQuery(@Nullable LocalDate created, @Nullable String language) {}
