package ru.memeBot.utils;

import java.util.List;
import java.util.Map;

public class Config {
	private String botUsername;
	private String botToken;
	private Map<String,List<String>> phrases;
	public Config(String botUsername, String botToken, Map<String, List<String>> phrases) {
		this.setBotUsername(botUsername);
		this.setBotToken(botToken);
		this.setPhrases(phrases);
	}
	public String getBotUsername() {
		return botUsername;
	}
	public void setBotUsername(String botUsername) {
		this.botUsername = botUsername;
	}
	public String getBotToken() {
		return botToken;
	}
	public void setBotToken(String botToken) {
		this.botToken = botToken;
	}
	public Map<String,List<String>> getPhrases() {
		return phrases;
	}
	public void setPhrases(Map<String,List<String>> phrases) {
		this.phrases = phrases;
	}
	
}
