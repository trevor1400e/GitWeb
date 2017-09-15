package com.thuff.follower.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.thuff.follower.models.GithubUser;

@RestController
public class FollowersController {

	@Value("client_id")
	private String clientId;
	
	@Value("client_secret")
	private String clientSecret;


	@RequestMapping("/followers/{username}")
	public GithubUser getFollowers(@PathVariable String username) {
		//Create the client to make requests
		RestTemplate restTemplate = new RestTemplate();
		
		//Get the J
		String result = restTemplate.getForObject(getURL(username), String.class);

		GithubUser user = new GithubUser();
		user.setName(username);
		
		
		Gson gson = new Gson();

		JsonArray body = gson.fromJson(result, JsonArray.class);

		for (int i = 0; i < 5 && i < body.size(); i++) {
			// Create object for follower of user
			
			GithubUser follower = new GithubUser();
			
			//Gets json element -> converts to object -> get login element -> convert to string 
			follower.setName(body.get(i).getAsJsonObject().get("login").getAsString());
			// Add the follower to the user object
			user.addFollower(follower);

			// Get followers of the follower
			result = restTemplate.getForObject(getURL(follower.getName()), String.class);

			// Parse Result
			JsonArray body2 = gson.fromJson(result, JsonArray.class);

			// Loop through to get followers of follower's followers
			for (int j = 0; j < 5 && j < body2.size(); j++) {
				GithubUser follower1 = new GithubUser();
				follower1.setName(body2.get(j).getAsJsonObject().get("login").getAsString());
				
				follower.addFollower(follower1);

				result = restTemplate.getForObject(getURL(follower1.getName()), String.class);
				JsonArray body3 = gson.fromJson(result, JsonArray.class);

				//additional layer test
				for (int k = 0; k < 5 && k < body3.size(); k++) {
					GithubUser follower2 = new GithubUser();
					follower2.setName(body3.get(k).getAsJsonObject().get("login").getAsString());
					
					follower1.addFollower(follower2);

				}

			}

		}

		return user;
	}

	private String getURL(String user) {
		return "https://api.github.com/users/" + user + "/followers?client_id=" + clientId + "&client_secret="
				+ clientSecret;
	}

}
