# GitHub Repo Scores

Spring Boot Application that provides a list of GitHub repositories based on a user's query and scores them according to the stars, forks and the time since the last update. Requests are served and processed asynchronously, so that we can handle the limit of GitHub API. We fetch the top 50 repositories from GitHub, that match the criteria.

To start the application you need to create and use your own GitHub token and set it in the environment variables.

Endpoint `POST /requests`: 

Creates a new GitHub Repo Scores request and return a requestId, that the user can use to check the results.
Sample response:
```
{
  "requestId": "18e47fb4-70b5-4e52-91ef-0d15ca238721"
}
```

Endpoint `GET /requests/{request-id}`: 

Gets the results for a GitHub Repo Scores request.
Sample response:
```
{
  "requestId": "18e47fb4-70b5-4e52-91ef-0d15ca238721",
  "processed": true,
  "processedTimestamp": "2025-06-13T09:03:10Z",
  "queryParams": {
    "created": "2025-01-01",
    "language": "Java"
  },
  "repositories": [
    {
      "id": 927240332,
      "full_name": "hamitmizrak/ibb_ecodation_javacore",
      "language": "Java",
      "stargazers_count": 147,
      "forks_count": 8,
      "created_at": "2025-02-04T16:29:29Z",
      "updated_at": "2025-05-23T09:42:27Z",
      "score": 0.05015595832749105
    },
    ...
    }
  ]
}
```
