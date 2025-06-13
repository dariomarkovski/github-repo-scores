package com.redcare.interview.githubreposcores.config;

import com.redcare.interview.githubreposcores.httpservice.GitHubRepositorySearchHttpService;
import com.redcare.interview.githubreposcores.model.GitHubRepositorySearchQuery;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpRequestValues;
import org.springframework.web.service.invoker.HttpServiceArgumentResolver;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

/**
 * Configuration that sets up the ${@link GitHubRepositorySearchHttpService} bean so that we can
 * execute requests to the GitHub API. It uses the token from the application.properties, which
 * comes from environment variables.
 */
@Configuration
@Slf4j
public class GitHubRepositorySearchRestClientConfig {

  public static String GITHUB_API_BASE_URL = "https://api.github.com";

  @Value("${github.token}")
  public String githubToken;

  @Bean
  public GitHubRepositorySearchHttpService gitHubRepositorySearchHttpService() {
    RestClient restClient =
        RestClient.builder()
            .baseUrl(GITHUB_API_BASE_URL)
            .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + githubToken)
            .build();
    RestClientAdapter adapter = RestClientAdapter.create(restClient);
    HttpServiceProxyFactory factory =
        HttpServiceProxyFactory.builderFor(adapter)
            .customArgumentResolver(new GitHubRepositorySearchQueryArgumentResolver())
            .build();
    return factory.createClient(GitHubRepositorySearchHttpService.class);
  }

  public static class GitHubRepositorySearchQueryArgumentResolver
      implements HttpServiceArgumentResolver {

    @Override
    public boolean resolve(
        Object argument, MethodParameter parameter, HttpRequestValues.Builder requestValues) {
      if (parameter.getParameterType().equals(GitHubRepositorySearchQuery.class)) {
        GitHubRepositorySearchQuery search = (GitHubRepositorySearchQuery) argument;

        List<String> params = new ArrayList<>();
        if (search.created() != null) {
          String createdParam = "created:>=" + search.created();
          params.add(createdParam);
        }
        if (search.language() != null) {
          String languageParam = "language:" + search.language();
          params.add(languageParam);
        }

        String joinedParams = String.join(" ", params);
        String finalParams = joinedParams.isEmpty() ? "Q" : joinedParams;

        requestValues.addRequestParameter("q", finalParams);
        requestValues.addRequestParameter("per_page", "50");
        return true;
      }
      return false;
    }
  }
}
