package com.redcare.interview.githubreposcores.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.redcare.interview.githubreposcores.model.dto.GetGitHubRepoScoresResponseBodyDto;
import com.redcare.interview.githubreposcores.model.dto.PostGitHubRepoScoresRequestBodyDto;
import com.redcare.interview.githubreposcores.model.dto.PostGitHubRepoScoresResponseBodyDto;
import com.redcare.interview.githubreposcores.service.GitHubRepoScoresService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequiredArgsConstructor
@Slf4j
public class GitHubRepoScoresController {

  public final GitHubRepoScoresService gitHubRepoScoresService;

  @PostMapping(
      value = "/scores",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<PostGitHubRepoScoresResponseBodyDto> createGitHubRepoScoresRequest(
      @RequestBody(required = false) PostGitHubRepoScoresRequestBodyDto requestBodyDto) {
    PostGitHubRepoScoresResponseBodyDto requestDto =
        gitHubRepoScoresService.createNewScoresRequest(requestBodyDto);
    return ResponseEntity.ok(requestDto);
  }

  @GetMapping(value = "/scores/{request-id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<GetGitHubRepoScoresResponseBodyDto> retrieveRepositoryScores(
      @PathVariable(name = "request-id") UUID requestId) throws JsonProcessingException {
    GetGitHubRepoScoresResponseBodyDto resultDto = gitHubRepoScoresService.getRequest(requestId);
    if (resultDto == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    return ResponseEntity.ok(resultDto);
  }
}
