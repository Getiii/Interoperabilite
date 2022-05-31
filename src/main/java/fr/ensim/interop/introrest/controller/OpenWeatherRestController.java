package fr.ensim.interop.introrest.controller;
import fr.ensim.interop.introrest.model.joke.Jokes;
import fr.ensim.interop.introrest.model.telegram.ApiResponseTelegram;
import fr.ensim.interop.introrest.model.telegram.Message;
import fr.ensim.interop.introrest.model.telegram.MessageSend;
import fr.ensim.interop.introrest.model.weather.City;
import fr.ensim.interop.introrest.model.weather.OpenWeather;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class OpenWeatherRestController {

	@Autowired
	private MessageRestController messageRestController;
	@Value("${open.weather.api.url}")
	private String openweatherUrl;
	@Value("${open.weather.api.token}")
	private String openweatherToken;


	@GetMapping(value = "/meteo", params = {"ville"})
	public ResponseEntity<OpenWeather> meteo(
			@RequestParam("ville") String ville){

		RestTemplate restTemplate = new RestTemplate();

		ResponseEntity<City[]> responseEntity = restTemplate.getForEntity(openweatherUrl + "geo/1.0/direct?q={ville}&limit=3"
						+ "&appid=" + openweatherToken,
				City[].class, ville);

		City[] cities = responseEntity.getBody();
		City city = cities[0];

		System.out.println(openweatherUrl + "data/2.5/weather?lat=" + city.getLat() +
						"&lon=" + city.getLon() + "&appid=" + openweatherToken);

		OpenWeather openWeather = restTemplate.getForObject(openweatherUrl + "data/2.5/weather?lat={lat}"
																	+ "&lon={longitude}&appid=" + openweatherToken,
															OpenWeather.class, city.getLat(), city.getLon());

		Message message = new Message();
		message.setText("Météo à : " + openWeather.name + " :\n"+ openWeather.getWeather().get(0).getDescription() +
				"\nTempérature : " + openWeather.getMain().getTemp());

		ResponseEntity<ApiResponseTelegram> apiResponseTelegram = messageRestController.sendMessage(message);

		System.out.println(apiResponseTelegram);
		return ResponseEntity.ok().body(openWeather);
	}


}
