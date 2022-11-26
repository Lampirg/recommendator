# recommendator
 
This is web application for computing anime titles to watch according to user anime list. Right now it supports My Anime List API.

*Note:* this is a **study unfinished project** and right now it is problematic to use (as you can see in [usage](#usage)). Thus the project can change significantly.

## Usage

The project isn't deployed anywhere so the only way to run application right now is to clone this repository and run it manually. But application need MAL API's client code to make requests otherwise it will be blocked. But if you cloned repository and you do have MAL API's client code, you need to create "mal security code.yml" in resource directory with this structure:

``clientIdHeader: X-MAL-CLIENT-ID``

``clientId: *enter your client code here*``

After that you can run java application and make HTTP requests.

Send GET request to "/recommend/{username}" and the responce will be an array of pairs title|numOfRecommendations.

## Occured problems

- MAL API allows to get user anime list with one HTTP request and this is very convenient. But responce body of responce for such request contains only basic information about anime title and array of reccomendation isn't included in that response. So to get it, it is requried to make GET request for every anime title in user list and this is very slow. Things get worse when My Anime List server forbids making more queries due to big amount of sended requests (adding delay between requests helps but not completely).

- The solution was limiting amount of titles to look recommendations for. So titles will be computed not for all list but for 50 titles with highest score.

- Even so, computing process can take up to few minutes.
