# recommendator
 
This is web application for computing anime titles to watch according to user anime list. Right now it supports My Anime List and Shikimori.

*Note:* this is a **study unfinished project** and right now it may be problematic to use (as you can see in [usage](#usage)). Thus the project can change significantly.

## What does it do?

This application fetches inserted in url user's nickname then collect an array of anime titles that user would like to watch according to his anime list.

## Usage

The project isn't deployed anywhere so the only way to run application right now is to clone this repository and run it manually. But application need MAL API's client code to make requests otherwise it will be blocked. But if you cloned repository and you do have MAL API's client code, you need to create "mal security code.yml" in resource directory with this structure:

``clientIdHeader: X-MAL-CLIENT-ID``

``clientId: *enter your client code here*``

After that you can run java application and make HTTP requests.

Using Shikimori endpoint should be just fine though.

Send GET request to "/recommend/\*db name\*/{username}" and the responce will be an array of pairs title|numOfRecommendations. Keep in mind that Shikimori API is case sensitive to nicknames (unlike MAL API).

\*db name\* variants:

- mal

- shiki

## TODO

- Support of AniList and AniDB.

- Account system that would allow collecting recommendations not only from 50 titles with highest score but from all user anime list.

## Occured problems

- MAL API allows to get user anime list with one HTTP request and this is very convenient. But responce body of responce for such request contains only basic information about anime title and array of reccomendation isn't included in that response. So to get it, it is requried to make GET request for every anime title in user list and this is very slow. Things get worse when My Anime List server forbids making more queries due to big amount of sended requests (adding delay between requests helps but not completely).

- The solution was limiting amount of titles to look recommendations for. So titles will be computed not for all list but for 50 titles with highest score.

- Even so, computing process can take up to few minutes.
