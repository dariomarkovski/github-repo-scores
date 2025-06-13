package com.redcare.interview.githubreposcores;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.redcare.interview.githubreposcores.component.GitHubRepoScoresRequestProcessorComponent;
import com.redcare.interview.githubreposcores.model.dto.GetGitHubRepoScoresResponseBodyDto;
import com.redcare.interview.githubreposcores.model.dto.PostGitHubRepoScoresRequestBodyDto;
import com.redcare.interview.githubreposcores.model.entity.GitHubRepoScoresRequestEntity;
import com.redcare.interview.githubreposcores.repository.GitHubRepoScoresRequestRepository;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@DirtiesContext
class GithubRepoScoresApplicationTests {

  private static final String CREATE_GITHUB_REPO_SCORES_REQUEST_ENDPOINT = "/scores";
  private static final String RETRIEVE_GITHUB_REPO_SCORES_REQUEST_ENDPOINT = "/scores/{request-id}";

  @Autowired protected MockMvc mvc;
  @Autowired protected ObjectMapper objectMapper;
  @Autowired private GitHubRepoScoresRequestRepository gitHubRepoScoresRequestRepository;
  // so we don't throw exceptions in the scheduler
  @MockitoBean private GitHubRepoScoresRequestProcessorComponent processorComponent;

  @Test
  void test_createGitHubRepoScoresRequest() throws Exception {
    PostGitHubRepoScoresRequestBodyDto dto =
        new PostGitHubRepoScoresRequestBodyDto(LocalDate.parse("2025-05-01"), "Java");
    String content = objectMapper.writeValueAsString(dto);
    mvc.perform(
            post(CREATE_GITHUB_REPO_SCORES_REQUEST_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.requestId").isString());

    List<GitHubRepoScoresRequestEntity> requests = gitHubRepoScoresRequestRepository.findAll();
    assertThat(requests).hasSize(1);
  }

  @Test
  void test_retrieveRepositoryScores() throws Exception {
    GitHubRepoScoresRequestEntity entityInDb =
        GitHubRepoScoresRequestEntity.builder()
            .processed(false)
            .created(LocalDate.parse("2025-05-01"))
            .build();
    entityInDb = gitHubRepoScoresRequestRepository.save(entityInDb);

    String responseContent =
        mvc.perform(get(RETRIEVE_GITHUB_REPO_SCORES_REQUEST_ENDPOINT, entityInDb.getId()))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    GetGitHubRepoScoresResponseBodyDto dto =
        objectMapper.readValue(responseContent, GetGitHubRepoScoresResponseBodyDto.class);

    assertThat(dto.requestId()).isEqualTo(entityInDb.getId());
    assertThat(dto.processed()).isEqualTo(false);
    assertThat(dto.repositories()).isEqualTo(null);
  }
}
