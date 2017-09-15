package com.thuff.follower.models;

import java.util.ArrayList;
import java.util.List;

public class GithubUser {
	
	private String name;
	private List<GithubUser> followers;
	
	public GithubUser(){
		setFollowers(new ArrayList<>());
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<GithubUser> getFollowers() {
		return followers;
	}

	public void setFollowers(List<GithubUser> followers) {
		this.followers = followers;
	}
	
	public void addFollower(GithubUser user){
		followers.add(user);
	}
	
	
	
}
