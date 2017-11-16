package ru.memeBot;

import java.util.ArrayList;
import java.util.Random;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import ru.memeBot.memes.Meme;
import ru.memeBot.utils.Config;

public class MemeBot extends TelegramLongPollingBot{
	private final String botToken;
	private final String botUsername;

	private ArrayList<String> who = new ArrayList<String>();
	private ArrayList<String> when = new ArrayList<String>();
	private ArrayList<String> where = new ArrayList<String>();
	private ArrayList<String> does = new ArrayList<String>();
	private ArrayList<String> qu = new ArrayList<String>();
	private ArrayList<Meme> memes = new ArrayList<Meme>();
	private Random random;

	public MemeBot(Config cfg,ArrayList<Meme> memes) {
		this.botToken = cfg.getBotToken();
		this.botUsername = cfg.getBotUsername();
		this.who = (ArrayList<String>) cfg.getPhrases().get("who");
		this.when = (ArrayList<String>) cfg.getPhrases().get("when");
		this.where = (ArrayList<String>) cfg.getPhrases().get("where");
		this.does = (ArrayList<String>) cfg.getPhrases().get("does");
		this.qu = (ArrayList<String>) cfg.getPhrases().get("qu");
		this.memes = memes;
		random = new Random();
		random.setSeed(System.currentTimeMillis());
	}
	
	
	@Override
	public void onUpdateReceived(Update update) {
		if(update.hasMessage()&&update.getMessage().hasText()&&update.getMessage().getText().toLowerCase().startsWith("/meme")) {
			SendMessage mem = new SendMessage().setChatId(update.getMessage().getChatId()).setReplyToMessageId(update.getMessage().getMessageId());
			String when = randomFromArray(this.when);
			String who1 = randomFromArray(this.who);
			String who2 = randomFromArray(this.who);
			String who3 = randomFromArray(this.who);
			while(who1.toLowerCase().contains(who2.toLowerCase())||who2.toLowerCase().contains(who1.toLowerCase())) {
				who2 = randomFromArray(this.who);
			}
			while(who3.toLowerCase().contains(who1.toLowerCase())||who1.toLowerCase().contains(who3.toLowerCase())||who3.toLowerCase().contains(who2.toLowerCase())||who2.toLowerCase().contains(who3.toLowerCase())) {
				who3 = randomFromArray(this.who);
			}
			if(who2.endsWith("а")) {
				who2 = who2.substring(0, who2.length()-1)+"у";
			}else if(who2.startsWith("бесконечное")){
				
			}else if(who2.endsWith("я")) {
				who2 = who2.substring(0, who2.length()-1)+"ю";
			}else {
				who2 = who2+"а";
			}
			String where = randomFromArray(this.where);
			String does1 = randomFromArray(this.does);
			String does2 = randomFromArray(this.does);
			String says = randomFromArray(this.qu);
			
			while((!says.startsWith("понял"))&&does1.startsWith("спрашивает")) {
				does1 = randomFromArray(this.does);
			}

			while(does2.equalsIgnoreCase(does1)||((!says.startsWith("понял"))&&does2.startsWith("спрашивает"))) {
				does2 = randomFromArray(this.does);
			}
			if(does1.endsWith("как раз")) {
				if(who1.endsWith("а")||who1.endsWith("я")) {
					does1 = does1.replace("ему", "ей");
				}else if(who1.startsWith("бесконечное")) {
					does1 = does1.replace("ему", "им");
				}
				if(who2.endsWith("у")||who1.endsWith("ю")) {
					does1 = does1.replace("он", "она");
				}else if(who2.startsWith("бесконечное")) {
					does1 = does1.replace("он", "они");
				}
				
			}
			String meme;
			if(does1.endsWith("как раз")) {

				meme = "Однажды "+when+", "+who1+" заходит "+where+", видит там "+who2+ ", "+does1 +(
				random.nextBoolean()?"":", а потом заходит "+ who3 +" и "+does2+": \""+says+"\"");
			}else {
				meme = "Однажды "+when+", "+who1+" заходит "+where+", видит там "+who2+ " и "+does1+": \""+says+"\"" ;			
			}
			
			if(random.nextInt(69)==13) {
				meme = "политех сасат";
			}
			Meme rand = memes.get(random.nextInt(memes.size()));;
			String tmeme = rand.generateMeme();
			System.out.println("rdg: "+tmeme);
			
			mem.setText(meme);
			try {
				Thread.sleep(500);
				execute(mem);
				System.out.println("meme: " + meme);
			} catch (TelegramApiException | InterruptedException e) {
				e.printStackTrace();
				System.out.println("unable to send message");
			}
			
			
			//eee boii
		}else if(update.hasMessage()&&update.getMessage().hasText()&&update.getMessage().getText().toLowerCase().startsWith("/eeboi")) {
			
			SendMessage msg = new SendMessage().setChatId(update.getMessage().getChatId()).setReplyToMessageId(update.getMessage().getMessageId()).setText("eee boii");
			try {
				Thread.sleep(500);
				execute(msg);
				System.out.println("eee boi");
			} catch (TelegramApiException | InterruptedException e) {
				e.printStackTrace();
				System.out.println("unable to send eee boi");
			}
		}else if(update.hasMessage()&&update.getMessage().hasText()&&(
				update.getMessage().getText().toLowerCase().contains("есть два стула")||
				update.getMessage().getText().toLowerCase().contains("два стула")
				)) {
			SendMessage msg = new SendMessage().setChatId(update.getMessage().getChatId()).setReplyToMessageId(update.getMessage().getMessageId()).setText("и оба твои");
			try {
				Thread.sleep(500);
				execute(msg);
				System.out.println("two chairs");
			} catch (TelegramApiException | InterruptedException e) {
				e.printStackTrace();
				System.out.println("unable to send two chairs");
			}
		}else if(update.hasMessage()&&update.getMessage().hasText()&&(
				update.getMessage().getText().endsWith(".") ||
				update.getMessage().getText().endsWith(",")
		)) {
			SendMessage msg = new SendMessage().setChatId(update.getMessage().getChatId());
			
			if(update.getMessage().isReply()) {
				msg.setReplyToMessageId(update.getMessage().getReplyToMessage().getMessageId());
			}
			if(update.getMessage().getText().endsWith(",")) {
				msg.setText((random.nextBoolean()?"понял, да?":"понял, нет?"));
			}else {
				msg.setText((random.nextBoolean()?"Понял, да?":"Понял, нет?"));
			}
			if(random.nextInt(69)==13) {
				try {
					Thread.sleep(500);
					execute(msg);
					System.out.println("understood?");
				} catch (TelegramApiException | InterruptedException e) {
					e.printStackTrace();
					System.out.println("unable to send understood?");
				}
			}
		}
	}
	
	public String randomFromArray(ArrayList<String> from) {
		
		return from.get(random.nextInt(from.size()));
	}

	@Override
	public String getBotToken() {
		return botToken;
	}
	
	@Override
	public String getBotUsername() {
		return botUsername;
	}
}
