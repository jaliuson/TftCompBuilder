package com.example;

import com.example.RiotApiHelper.RiotApiHelper;
import com.example.database.entities.User;

import com.example.database.entities.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collection;
import java.util.Map;
import java.util.HashMap;

@SpringBootApplication
@RestController
@EnableJpaRepositories(basePackages = {"com.example.database.entities"})
public class TftCompBuilderApplication {
	@Autowired
	private UserService userService;
	private static Map<String, String> jsonOutput = new HashMap<>();
	private RiotApiHelper riotAPIHelper;


	public static void main(String[] args) {
		SpringApplication.run(TftCompBuilderApplication.class, args);
	}
/*
	@GetMapping("/helloBlank")
	public String sayHello(@RequestParam(value = "myName", defaultValue = "World") String name) {
		return String.format("Hello %s!", name);
	}*/

	@GetMapping("/hello")
	public ResponseEntity<Collection<String>> sayHello(@RequestParam(value = "myName", defaultValue = "World") String name) {
		//return String.format("Hello %s!", name); //http://localhost:8080/hello?myName=jason
		jsonOutput.put("name", name);
		return new ResponseEntity<>(jsonOutput.values(), HttpStatus.OK);
	}

	@GetMapping("/description")
	public ResponseEntity<Collection<String>> getProductDescription(){
		jsonOutput.put("App Description", "Valorant Tracker App!");
		return new ResponseEntity<>(jsonOutput.values(), HttpStatus.OK);
	}

	@GetMapping("/match-history")
	public Collection getMatchHistory(@RequestParam(value = "sumName", defaultValue = "player") String summonerName){
		riotAPIHelper = new RiotApiHelper();
		String puuid = riotAPIHelper.getPuuid(summonerName);
		return (riotAPIHelper.getMatchHistory(puuid));
	}

	@PostMapping("/create-user")
	public User createuser(@RequestParam(value = "userName") String userName, @RequestParam(value = "password") String password, @RequestParam(value="riotId") String riotID){
		User newUser = new User(userName, password, riotID, true);
		userService.saveUser(newUser);
		return newUser;
	}

}

