package com.redcare.interview.githubreposcores.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.redcare.interview.githubreposcores.model.GitHubRepositorySearchResult.GitHubRepositorySearchResultItem;
import com.redcare.interview.githubreposcores.model.dto.GetGitHubRepoScoresResponseBodyDto;
import com.redcare.interview.githubreposcores.model.dto.PostGitHubRepoScoresRequestBodyDto;
import com.redcare.interview.githubreposcores.model.dto.PostGitHubRepoScoresResponseBodyDto;
import com.redcare.interview.githubreposcores.model.entity.GitHubRepoScoresRequestEntity;
import com.redcare.interview.githubreposcores.repository.GitHubRepoScoresRequestRepository;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/** A service to serve our REST controller. */
@Service
@RequiredArgsConstructor
@Slf4j
public class GitHubRepoScoresService {

  private final GitHubRepoScoresRequestRepository gitHubRepoScoresRequestRepository;
  private final ObjectMapper objectMapper;

  public PostGitHubRepoScoresResponseBodyDto createNewScoresRequest(
      PostGitHubRepoScoresRequestBodyDto requestBodyDto) {
    LocalDate created = requestBodyDto != null ? requestBodyDto.created() : null;
    String language = requestBodyDto != null ? requestBodyDto.language() : null;
    Optional<GitHubRepoScoresRequestEntity> optionalRequest =
        gitHubRepoScoresRequestRepository.findByCreatedAndLanguage(created, language);
    GitHubRepoScoresRequestEntity request;
    if (optionalRequest.isPresent()) {
      request = optionalRequest.get();
    } else {
      GitHubRepoScoresRequestEntity newRequest =
          GitHubRepoScoresRequestEntity.builder().created(created).language(language).build();
      request = gitHubRepoScoresRequestRepository.save(newRequest);
    }
    return new PostGitHubRepoScoresResponseBodyDto(request.getId());
  }

  public GetGitHubRepoScoresResponseBodyDto getRequest(UUID requestId)
      throws JsonProcessingException {
    Optional<GitHubRepoScoresRequestEntity> optionalRequest =
        gitHubRepoScoresRequestRepository.findById(requestId);
    if (optionalRequest.isPresent()) {
      GitHubRepoScoresRequestEntity request = optionalRequest.get();
      List<GitHubRepositorySearchResultItem> repositories = null;
      if (request.getRepositories() != null) {
        repositories =
            objectMapper.readValue(
                request.getRepositories(),
                objectMapper
                    .getTypeFactory()
                    .constructCollectionType(List.class, GitHubRepositorySearchResultItem.class));
      }

      String createdParam = request.getCreated() != null ? request.getCreated().toString() : null;
      String languageParam = request.getLanguage();
      Map<String, String> queryParams = new HashMap<>();
      queryParams.put("created", createdParam);
      queryParams.put("language", languageParam);

      return new GetGitHubRepoScoresResponseBodyDto(
          requestId,
          request.isProcessed(),
          request.getProcessedTimestamp(),
          queryParams,
          repositories);
    }
    return null;
  }
}
