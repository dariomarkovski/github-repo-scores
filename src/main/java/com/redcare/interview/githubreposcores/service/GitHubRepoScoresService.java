package com.redcare.interview.githubreposcores.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.redcare.interview.githubreposcores.model.GitHubRepositorySearchResult.GitHubRepositorySearchResultItem;
import com.redcare.interview.githubreposcores.model.dto.GetGitHubRepoScoresResponseBodyDto;
import com.redcare.interview.githubreposcores.model.dto.PostGitHubRepoScoresRequestBodyDto;
import com.redcare.interview.githubreposcores.model.dto.PostGitHubRepoScoresResponseBodyDto;
import com.redcare.interview.githubreposcores.model.entity.GitHubRepoScoresRequestEntity;
import com.redcare.interview.githubreposcores.repository.GitHubRepoScoresRequestRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class GitHubRepoScoresService {

  private final GitHubRepoScoresRequestRepository gitHubRepoScoresRequestRepository;
  private final ObjectMapper objectMapper;

  public PostGitHubRepoScoresResponseBodyDto createNewScoresRequest(
      PostGitHubRepoScoresRequestBodyDto requestBodyDto) {
    Optional<GitHubRepoScoresRequestEntity> optionalRequest = gitHubRepoScoresRequestRepository.findByCreatedAndLanguage(
        requestBodyDto.created(), requestBodyDto.language());
    GitHubRepoScoresRequestEntity request;
    if (optionalRequest.isPresent()) {
      request = optionalRequest.get();
    } else {
      GitHubRepoScoresRequestEntity newRequest = GitHubRepoScoresRequestEntity.builder()
          .created(requestBodyDto.created()).language(requestBodyDto.language()).build();
      request = gitHubRepoScoresRequestRepository.save(newRequest);
    }
    return new PostGitHubRepoScoresResponseBodyDto(request.getId());
  }

  public GetGitHubRepoScoresResponseBodyDto getRequest(UUID requestId)
      throws JsonProcessingException {
    Optional<GitHubRepoScoresRequestEntity> optionalRequest = gitHubRepoScoresRequestRepository.findById(
        requestId);
    if (optionalRequest.isPresent()) {
      GitHubRepoScoresRequestEntity request = optionalRequest.get();
      List<GitHubRepositorySearchResultItem> repositories = objectMapper.readValue(
          request.getSearchResult(),
          objectMapper.getTypeFactory().constructCollectionType(
              List.class, GitHubRepositorySearchResultItem.class));
      return new GetGitHubRepoScoresResponseBodyDto(requestId, request.isProcessed(),
          request.getProcessedTimestamp(), repositories);
    }
    return null;
  }

}
