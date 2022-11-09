package com.lampirg.recommendator;

import com.lampirg.recommendator.mal.Recommendation;
import com.lampirg.recommendator.mal.queries.GetAnimeDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@RestController
public class RecommendatorApplication {

	@Autowired
	private RestTemplate restTemplate;

	public static void main(String[] args) {
		SpringApplication.run(RecommendatorApplication.class, args);
	}

	@GetMapping("/test")
	public ResponseEntity<?> hello() {

		String url = "https://api.myanimelist.net/v2/users/Rimisaki/animelist?fields=list_status&data=recommendations&limit=100";
//		String url = "https://api.myanimelist.net/v2/anime/37976?fields=recommendations";
		HttpHeaders headers = new HttpHeaders();
		headers.set("X-MAL-CLIENT-ID", "a4e33f4b0a5b5e9cbdbbcee74debbb3f");
		HttpEntity<String> request = new HttpEntity<>(headers);
		ResponseEntity<String> response = this.restTemplate.exchange(url, HttpMethod.GET, request, String.class);
		if (response.getStatusCode() == HttpStatus.OK)
			return response;
		else
			return ResponseEntity.notFound().build();

	}

}
