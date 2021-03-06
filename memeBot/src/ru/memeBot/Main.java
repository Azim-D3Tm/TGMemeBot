package ru.memeBot;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;

import ru.memeBot.memes.Meme;
import ru.memeBot.utils.Config;

public class Main {
	
	private static Config config;
	private static ArrayList<Meme> memes = new ArrayList<Meme>();
	public static Gson gson;
	
	public static void main(String[] args) {
		
		System.out.println("Preparing gson");
		gson = new GsonBuilder().setPrettyPrinting().create();
		System.out.println("Attempting to load configs");
		
		if(loadConfigs()) {
			System.out.println("Successfully loaded configs!\nPreparing the bot");
		}else {
			System.out.println("Fatal error occured while loading configs, shutting down!");
			System.exit(2);
		}
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        MemeBot bot = new MemeBot(config,memes);
        try {
    		System.out.println("Starting the bot");
			telegramBotsApi.registerBot(bot);
		} catch (TelegramApiRequestException e) {
			e.printStackTrace();
			System.out.println("unable to register bot");
		}
		System.out.println("Successfully started the bot!");
	}
	
	private static boolean loadConfigs() {
		try {
			File cfg = new File("config.json");
			if(!cfg.exists()) {
				cfg.createNewFile();
				System.out.println("No config file found, creating a new one...");
				if(!writeDefaults(cfg,generateDefaultConfig())) {
					System.out.println("Unable to create config file, shuttong down...");
					System.exit(1);
				}
				System.out.println("Successfully created default config file, please, enter valid bot username and bot token and then try again.");
				System.exit(0);
			}
			config = gson.fromJson(readFile(cfg), Config.class);
			if(config.needGenerateDefaultConfig()) {
			File dMeme = new File("defaultMeme.json");
			if(!dMeme.exists()) {
					dMeme.createNewFile();
					System.out.println("Creating default meme for examples...");
					if(!writeDefaults(dMeme, new Meme())) {
						System.out.println("Unable to create default meme, shuttong down...");
					}
				}
			}
			System.out.println("Loading memes");
			File dir = new File(".");
	        File[] filesList = dir.listFiles();
			for(File f:filesList) {
				if(f.isFile()) {
					if(f.getName().endsWith(".json")&&!(f.getName().startsWith("config.json")||f.getName().contains("config.json")||f.getName().equals("config.json"))){
						try {
							memes.add(gson.fromJson(readFile(f), Meme.class));
							System.out.println("    Loaded meme from "+f.getName());
						}catch(JsonParseException ex) {
							System.out.println("    Error loading meme from "+f.getName());
						}
					}
				}
			}
			System.out.println("Loaded memes!");
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		System.out.println("Validating config");
		Set<String> keys = config.getPhrases().keySet();
		if(!(keys.contains("qu")&&keys.contains("does")&&keys.contains("when")&&keys.contains("who")&&keys.contains("where"))) {
			System.out.println("Missing or more of must-have word lists! Config is invalid!");
			return false;
		}
		
		for(String key:keys) {
			if(config.getPhrases().get(key).isEmpty()) {
				System.out.println("Word list \""+key+"\" is empty! Config is invalid!");
				return false;
			}
		}
		System.out.println("Config is valid!");
		return true;
	}
	
	private static boolean writeDefaults(File file, Object obj) {
		try {
			FileWriter fw = new FileWriter(file);
			gson.toJson(obj, fw);
            fw.close();

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
		return true;
	}
	
	private static String readFile(File file) throws IOException {
		FileInputStream fis = new FileInputStream(file);
		InputStreamReader isr = new InputStreamReader(fis,"UTF-8");
		BufferedReader bufferedReader = new BufferedReader(isr);
		StringBuilder sb = new StringBuilder();
		String line;
		while ((line = bufferedReader.readLine()) != null) {
		    sb.append(line);
		}
		fis.close();
		isr.close();
		bufferedReader.close();
		String json = sb.toString();
		return json;
	}
	
	
	private static Config generateDefaultConfig() {
		ArrayList<String> qu = new ArrayList<String>() {
			private static final long serialVersionUID = 1L;
		{
			add("после шести не ем");
			add("добрый вечер");
		}};
		ArrayList<String> does = new ArrayList<String>() {
			private static final long serialVersionUID = 1L;
		{
			add("говорит");
			add("спрашивает");
			add("а он ему как раз");
		}};
		ArrayList<String> where = new ArrayList<String>() {
			private static final long serialVersionUID = 1L;
		{
			add("в бар");
			add("в столовку");
		}};
		ArrayList<String> when = new ArrayList<String>() {
			private static final long serialVersionUID = 1L;
		{
			add("утром");
			add("вечером");
			add("днем");
			add("ночью");
		}};
		ArrayList<String> who = new ArrayList<String>() {
			private static final long serialVersionUID = 1L;
		{
			add("шляпа");
			add("человек");
		}};
		
		Map<String,List<String>> phrases = new HashMap<>();
		phrases.put("qu", qu);
		phrases.put("does", does);
		phrases.put("where", where);
		phrases.put("when", when);
		phrases.put("who", who);
		
		Config cfg = new Config("Your_Bot_Username", "123456789:abcdefghijklmnopqrstuvwxyzEtcEtcEtc", phrases, true);
		
		return cfg;
	}
}
