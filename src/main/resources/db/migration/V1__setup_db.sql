CREATE TABLE github_repo_scores_request
(
    id                  VARCHAR(36) PRIMARY KEY,
    processed           boolean   NOT NULL DEFAULT false,
    processed_timestamp TIMESTAMP,
    created             DATE,
    language            VARCHAR(50),
    repositories        TEXT,
    created_date        TIMESTAMP NOT NULL
);

CREATE INDEX create_language_search_idx ON github_repo_scores_request (created, language);