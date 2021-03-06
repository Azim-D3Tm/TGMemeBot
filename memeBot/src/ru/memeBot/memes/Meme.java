package ru.memeBot.memes;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Pattern;

import com.google.gson.annotations.Expose;

public class Meme {
	//more stuff here later
	@Expose
	private String pattern;
	@Expose
	private Map<String, List<String>> phrases;
	@Expose
	private List<String> noRepeat;
	@Expose(serialize = false,deserialize = false)
	private Random rnd;
	public Meme(String pattern, Map<String, List<String>> phrases,List<String> noRepeat) throws IllegalArgumentException{
		this.pattern = pattern;
		this.phrases = phrases;
		this.noRepeat = noRepeat;
		for(String nr:noRepeat) {
			if(phrases.get(nr).size()<2) {
				throw new IllegalArgumentException();
			}
			
		}
		
		this.rnd = new Random();
		rnd.setSeed(System.currentTimeMillis());
	}
	
	public Meme() {
		String  dpattern = "Однажды @time@, @name@ зашел в @place@ и увидел @name@";
		List<String> dnoRepeat = Arrays.asList("time","name");
		Map<String,List<String>> dphrases = new HashMap<String,List<String>>();
		List<String> name = Arrays.asList("Cтудент","оно");
		List<String> time = Arrays.asList("время","ночью");
		List<String> place = Arrays.asList("место","бар");
		dphrases.put("time", time);
		dphrases.put("name", name);
		dphrases.put("place", place);
		this.pattern = dpattern;
		this.phrases = dphrases;
		this.noRepeat = dnoRepeat;
		this.rnd = new Random();
		rnd.setSeed(System.currentTimeMillis());
	}

	public String generateMeme() {
		String meme = "";
		meme = pattern;
		for(String key:phrases.keySet()) {
			System.out.println(key+" : ");
			for(String tempstr:phrases.get(key)) {
				System.out.println("    "+tempstr);
			}
			
			
			while(meme.contains("@"+key+"@")) {
				//System.out.println("2");
				String replacement = randomFromArray(phrases.get(key));
				
				while(noRepeat.contains(key) && meme.contains(replacement)) {
					replacement = randomFromArray(phrases.get(key));

					System.out.println("3");
				}
				
				meme = meme.replaceFirst(Pattern.quote("@"+key+"@"), replacement);
			}
			
		}
		return meme;
	}
	
	public String randomFromArray(List<String> list) {
		
		return list.get(rnd.nextInt(list.size()));
	}
}
