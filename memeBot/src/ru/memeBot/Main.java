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

import ru.memeBot.utils.Config;

public class Main {
	
	public static Config config;
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
        MemeBot bot = new MemeBot(config);
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
				if(!writeDefaults(cfg)) {
					System.out.println("Unable to create config file, shuttong down...");
					System.exit(1);
				}
				System.out.println("Successfully created default config file, please, enter valid bot username and bot token and then try again.");
				System.exit(0);
			}
			FileInputStream fis = new FileInputStream(cfg);
			InputStreamReader isr = new InputStreamReader(fis,"UTF-8");
			BufferedReader bufferedReader = new BufferedReader(isr);
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = bufferedReader.readLine()) != null) {
			    sb.append(line);
			}
			String json = sb.toString();
			config = gson.fromJson(json, Config.class);
			bufferedReader.close();
			isr.close();
			fis.close();
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
	
	private static boolean writeDefaults(File file) {
		
		Config cfg = generateDefaultConfig();
		
		try {
			FileWriter fw = new FileWriter(file);
			gson.toJson(cfg, fw);
            fw.close();

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
		return true;
	}
	private static Config generateDefaultConfig() {
		ArrayList<String> qu = new ArrayList<String>() {{
			add("после шести не ем");
			add("добрый вечер");
		}};
		ArrayList<String> does = new ArrayList<String>() {{
			add("говорит");
			add("спрашивает");
			add("а он ему как раз");
		}};
		ArrayList<String> where = new ArrayList<String>() {{
			add("в бар");
			add("в столовку");
		}};
		ArrayList<String> when = new ArrayList<String>() {{
			add("утром");
			add("вечером");
			add("днем");
			add("ночью");
		}};
		ArrayList<String> who = new ArrayList<String>() {{
			add("шляпа");
			add("человек");
		}};
		
		Map<String,List<String>> phrases = new HashMap<>();
		phrases.put("qu", qu);
		phrases.put("does", does);
		phrases.put("where", where);
		phrases.put("when", when);
		phrases.put("who", who);
		
		Config cfg = new Config("Your_Bot_Username", "123456789:abcdefghijklmnopqrstuvwxyzEtcEtcEtc", phrases);
		
		return cfg;
	}

}
