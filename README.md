# recommendator

## Tech stack

- Java 17
- Spring Framework (with MVC web framework)
- Spring Boot
- Spring Cloud
- JUnit
- Docker

## About
 
This is a microservice web application for recommending anime to watch according to popular social cataloging websites. Right now it supports:

- [My Anime List](https://myanimelist.net/) (with its [v2 API](https://myanimelist.net/apiconfig/references/api/v2))

- [Shikimori](https://shikimori.one/) (with its [v1](https://shikimori.one/api/doc/1.0) and [v2](https://shikimori.one/api/doc/2.0) APIs)

- [AniList](https://anilist.co/) (with its [GraphQL API](https://github.com/AniList/ApiV2-GraphQL-Docs))

**Note:** this is a **unfinished pet project** and right now it may be problematic to use (as you can see in [usage](#usage)). Thus the project can change significantly.

## What does it do?

This application goes through user's anime entries and counts similar anime. It returns sorted recommended animes' list.

## Usage

The project isn't deployed anywhere so the only way to use application right now is to clone this repository and run it manually.

To use MAL you need MAL API's client code to make requests otherwise it will be blocked. Using Shikimori and AniList should be just fine though.

If you do have MAL API's client code, you need to create "mal security code.yml" in [resource directory](mal/src/main/resources/) with this structure:

```yaml
clientIdHeader: X-MAL-CLIENT-ID
clientId: /enter your client code here/
```

## TODO

- Support of AniDB.

- Account system that would allow collecting recommendations not only from 50 titles with highest score but from all user anime list.

- Errors handling.

- Frontend.

- Deploying.

## Occured problems

- [n + 1 problem](https://restfulapi.net/rest-api-n-1-problem/) when using MAL and Shikimori API. AniList (and its GraphQL API) is just fine because it is possible to request all data in a single query.

- The solution was limiting amount of titles to look recommendations for. So titles will be computed not for all list but for 50 titles with highest score.

- Even so computing process can take up to few minutes.

- All APIs have pretty strict rate limiting (that aggravates MAL and Shikimori performance problem). Thus (at least without auto caching) only Anilist can be theoretically used in production (as it can process up to 90 users per minute).
