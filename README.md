# betaguess (WIP)

A simple [alphaguess](https://alphaguess.com) clone built using Spring, Angular, and Redis.

## Requirements

- Maven
- Docker
- Java (21+)

## Setup

Assuming there is a running Docker Engine in your machine, run the application locally using this command:

```shell
mvn spring-boot:test-run -pl backend
```

This starts up the Spring Boot Application with a configured local development environment, which bootstraps a Redis
container through Docker.

## Usage

Submit your guesses using a `POST` request:

```shell
curl -i \
 -H 'Content-Type: application/json' \
 -d '{"guess": "bark"}' \
 http://localhost:8080/api/submit
```

If the guess is valid, then the server will reply back with a `SESSION` cookie in the `Set-Cookie` header:

```
HTTP/1.1 200
Set-Cookie: SESSION=<SESSION_COOKIE>; Max-Age=3600; Expires=Thu, 28 Mar 2024 22:08:57 GMT; Path=/api; HttpOnly; SameSite=Lax
Content-Type: application/json
Transfer-Encoding: chunked
Date: Thu, 28 Mar 2024 21:08:57 GMT

{"guesses":{"before":[],"after":["bark"]},"error":"NONE","lastSubmissionTimestamp":"2024-03-28T21:08:57.295570700Z","isGameOver":false}
```

Any subsequent guesses must contain the `SESSION` cookie.

```shell
curl -i \
 -H 'Content-Type: application/json' \
 -d '{"guess": "example"}' \
 -b 'SESSION=<SESSION_COOKIE>' \
 http://localhost:8080/api/submit
```

Response:

```
HTTP/1.1 200
Content-Type: application/json
Transfer-Encoding: chunked
Date: Thu, 28 Mar 2024 21:15:47 GMT

{"guesses":{"before":[],"after":["bark","example"]},"error":"NONE","lastSubmissionTimestamp":"2024-03-28T21:15:47.672291100Z","isGameOver":false}
```

## API Reference

#### Submit Guess

```http request
POST /api/submit
```

