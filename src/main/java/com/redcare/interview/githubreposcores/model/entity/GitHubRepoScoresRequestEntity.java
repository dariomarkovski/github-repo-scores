package com.redcare.interview.githubreposcores.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "github_repo_scores_request")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@EntityListeners(AuditingEntityListener.class)
public class GitHubRepoScoresRequestEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @JdbcTypeCode(SqlTypes.VARCHAR)
  @Column(name = "id", nullable = false, updatable = false)
  private UUID id;

  @Column(name = "processed")
  private boolean processed;

  @Column(name = "processed_timestamp")
  private Instant processedTimestamp;

  @Column(name = "created")
  private LocalDate created;

  @Column(name = "language")
  private String language;

  @Column(name = "search_result")
  private String searchResult;

  @CreatedDate
  @Column(name = "created_date")
  private Instant created_date;
}
