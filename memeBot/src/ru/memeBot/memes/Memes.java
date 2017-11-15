package ru.memeBot.memes;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Pattern;

public class Memes {
	//more stuff here later
	private String pattern;
	private Map<String, List<String>> phrases;
	private List<String> noRepeat;
	private Random rnd;
	public Memes(String pattern, Map<String, List<String>> phrases,List<String> noRepeat) {
		this.pattern = pattern;
		this.phrases = phrases;
		this.noRepeat = noRepeat;
		rnd.setSeed(System.currentTimeMillis());
	}
	
	public String generateMeme() {
		String meme = "";
		meme = pattern;
		for(String key:phrases.keySet()) {
			
			while(meme.contains(key)) {
				String replacement = randomFromArray(phrases.get(key));
				
				while(noRepeat.contains(key) && meme.contains(replacement)) {
					replacement = randomFromArray(phrases.get(key));
				}
				
				meme.replaceFirst(Pattern.quote(key), replacement);
			}
			
		}
		return meme;
	}
	
	public String randomFromArray(List<String> list) {
		
		return list.get(rnd.nextInt(list.size()));
	}
}
