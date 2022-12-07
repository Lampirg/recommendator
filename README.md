# recommendator
 
This is web application for computing anime titles to watch according to user anime list. Right now it supports:

- [My Anime List](https://myanimelist.net/) (with it [v2](https://myanimelist.net/apiconfig/references/api/v2) API)

- [Shikimori](https://shikimori.one/) (with it [v1](https://shikimori.one/api/doc/1.0) and [v2](https://shikimori.one/api/doc/2.0) APIs)

- [AniList](https://anilist.co/) (with it [GraphQL](https://github.com/AniList/ApiV2-GraphQL-Docs) API)

**Note**: it is **highly recommended** to use anilist if possible because it's collect full user list and still has the highest speed.

*Note:* this is a **study unfinished project** and right now it may be problematic to use (as you can see in [usage](#usage)). Thus the project can change significantly.

## What does it do?

This application fetches inserted in url user's nickname then collect an array of anime titles that user would like to watch according to his anime list. It does not modify user list in any way.

## Usage

The project isn't deployed anywhere so the only way to run application right now is to clone this repository and run it manually. But application need MAL API's client code to make requests otherwise it will be blocked. But if you cloned repository and you do have MAL API's client code, you need to create "mal security code.yml" in resource directory with this structure:

```yaml
clientIdHeader: X-MAL-CLIENT-ID
clientId: /enter your client code here/
```

After that you can run java application and make HTTP requests.

Using Shikimori endpoint should be just fine though.

Send GET request to "/recommend/\*db name\*/{username}" and the responce will be an array of pairs title|numOfRecommendations. Keep in mind that Shikimori API is case sensitive to nicknames (unlike MAL API).

\*db name\* variants:

- mal

- shiki

- anilist

## TODO

- Support of AniDB.

- Account system that would allow collecting recommendations not only from 50 titles with highest score but from all user anime list.

- Errors handling.

- Frontend.

- Bug fixing.

## Occured problems

- [n + 1 problem](https://restfulapi.net/rest-api-n-1-problem/) when using MAL and Shikimori API. AniList (and his GraphQL API) is just fine because it is possible to request all data in a single query.

- The solution was limiting amount of titles to look recommendations for. So titles will be computed not for all list but for 50 titles with highest score.

- Even so computing process can take up to few minutes.
