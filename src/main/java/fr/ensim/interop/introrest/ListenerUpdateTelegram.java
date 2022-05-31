package fr.ensim.interop.introrest;

import fr.ensim.interop.introrest.controller.JokeRestController;
import fr.ensim.interop.introrest.controller.MessageRestController;
import fr.ensim.interop.introrest.controller.OpenWeatherRestController;
import fr.ensim.interop.introrest.model.telegram.ApiResponseTelegram;
import fr.ensim.interop.introrest.model.telegram.ApiResponseUpdateTelegram;
import fr.ensim.interop.introrest.model.telegram.Message;
import fr.ensim.interop.introrest.model.telegram.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class ListenerUpdateTelegram implements CommandLineRunner {

	@Autowired
	private JokeRestController jokeRestController;
	@Autowired
	private MessageRestController messageRestController;
	@Autowired
	private OpenWeatherRestController openWeatherRestController;

	private AtomicInteger atomicInteger = new AtomicInteger(0);
	private int idMessage;


	@Override
	public void run(String... args) throws Exception {
		Logger.getLogger("ListenerUpdateTelegram").log(Level.INFO, "Démarage du listener d'updates Telegram...");
		
		// Operation de pooling pour capter les evenements Telegram

		// Java timer
		TimerTask task = new TimerTask() {
			public void run() {
				ApiResponseUpdateTelegram apiResponseTelegram = messageRestController.getUpdate();
				Message m = apiResponseTelegram.getResult().get(0).getMessage();

				if( idMessage != m.getMessageId())
					if (m.getText().toLowerCase().contains("météo"))
						openWeatherRestController.meteo("Lyon");
					else if (m.getText().toLowerCase().contains("blague"))
						jokeRestController.joke();

				idMessage = m.getMessageId();
			}
		};

		long delay = 5000;
		new Timer().schedule(task, 0, delay);



	}
}
