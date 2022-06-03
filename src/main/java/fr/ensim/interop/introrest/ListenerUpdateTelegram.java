package fr.ensim.interop.introrest;

import fr.ensim.interop.introrest.controller.JokeRestController;
import fr.ensim.interop.introrest.controller.MessageRestController;
import fr.ensim.interop.introrest.controller.OpenWeatherRestController;
import fr.ensim.interop.introrest.model.joke.Joke;
import fr.ensim.interop.introrest.model.telegram.*;
import fr.ensim.interop.introrest.model.weather.InfoOpenWeatherList;
import fr.ensim.interop.introrest.model.weather.OpenWeather;
import fr.ensim.interop.introrest.model.weather.OpenWeatherList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
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
		idMessage = messageRestController.getUpdate().getResult().get(0).getMessage().getMessageId();
		
		// Operation de pooling pour capter les evenements Telegram

		// Java timer
		TimerTask task = new TimerTask() {
			public void run() {
				ApiResponseUpdateTelegram apiResponseTelegram = messageRestController.getUpdate();
				Message messageRecup = apiResponseTelegram.getResult().get(0).getMessage();

				String[] tokens = messageRecup.getText().split(" ",2);

				if(idMessage != messageRecup.getMessageId())
					if (messageRecup.getText().toLowerCase().contains("météo") || messageRecup.getText().toLowerCase().contains("weather")) {
						if (tokens[1].toLowerCase().contains("semaine")){
							String ville = tokens[1].subSequence(0,tokens[1].lastIndexOf("")-7).toString();
							try {
								ResponseEntity<OpenWeatherList> responseEntity = openWeatherRestController.meteoSemaine(ville, true);

								System.out.println("abc" + responseEntity.toString());

								Message messageAEnvoyer = new Message();

								OpenWeatherList openWeatherList = responseEntity.getBody();

								String meteo = "La météo de la semaine de : " + ville + " : \n";

								for (int i = 0; i < openWeatherList.getList().size(); i+=8) {
									InfoOpenWeatherList list = openWeatherList.getList().get(i);
									//Timestamp ts = new Timestamp(list.getDt());
									meteo += "Date : ";
									//meteo+= new Date(ts.getTime());
									meteo += list.getDt_txt();
									meteo+= "\n" + list.getWeather().get(0).getDescription() + "\n";
									meteo+= "\n" + Math.round(list.getMain().getTemp()) + "°C\n";
									meteo+= "---------------------\n";
								}
								messageAEnvoyer.setText(meteo);

								messageRestController.sendMessage(messageAEnvoyer);


							} catch (UnsupportedEncodingException e) {
								e.printStackTrace();
							}
						}else{
							if (tokens.length > 1)
								try {
									OpenWeatherList openWeather = openWeatherRestController.meteoSemaine(tokens[1], false).getBody();

									Message messageAEnvoyer = new Message();
									messageAEnvoyer.setText("Météo à : " + tokens[1] + " :\n" + openWeather.getList().get(0).getWeather().get(0).getDescription() +
											"\nTempérature : " + Math.round(openWeather.getList().get(0).getMain().getTemp()) + "°C");

									messageRestController.sendMessage(messageAEnvoyer);

								} catch (UnsupportedEncodingException e) {
									e.printStackTrace();
								}
							else {
								try {
									OpenWeatherList openWeather = openWeatherRestController.meteoSemaine("Le Mans", false).getBody();

									Message messageAEnvoyer = new Message();
									messageAEnvoyer.setText("Météo à : " + tokens[1] + " :\n" + openWeather.getList().get(0).getWeather().get(0).getDescription() +
											"\nTempérature : " + Math.round(openWeather.getList().get(0).getMain().getTemp()) + "°C");

									messageRestController.sendMessage(messageAEnvoyer);
								} catch (UnsupportedEncodingException e) {
									e.printStackTrace();
								}
							}
						}
					}else if (messageRecup.getText().toLowerCase().contains("blague") || messageRecup.getText().toLowerCase().contains("joke")) {
						if (tokens.length >= 2) {
							if (tokens[1].toLowerCase().contains("nulle")){
								Joke jokePick = jokeRestController.joke(2).getBody();
								Message messageAEnvoyer = new Message();
								messageAEnvoyer.setText(jokePick.getBlague());
								messageRestController.sendMessage(messageAEnvoyer);

							}else if (tokens[1].toLowerCase().contains("drole")) {
								Joke jokePick = jokeRestController.joke(1).getBody();
								Message messageAEnvoyer = new Message();
								messageAEnvoyer.setText(jokePick.getBlague());
								messageRestController.sendMessage(messageAEnvoyer);
							}
						} else{
							Joke jokePick = jokeRestController.joke().getBody();
							Message messageAEnvoyer = new Message();
							messageAEnvoyer.setText(jokePick.getBlague());
							messageRestController.sendMessage(messageAEnvoyer);
						}
					}else
						messageRestController.getCommandeDisponible();

				idMessage = messageRecup.getMessageId();
			}
		};

		long delay = 5000;
		new Timer().schedule(task, 0, delay);



	}
}
