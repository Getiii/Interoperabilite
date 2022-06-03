package fr.ensim.interop.introrest.controller;
import fr.ensim.interop.introrest.model.telegram.ApiResponseTelegram;
import fr.ensim.interop.introrest.model.telegram.Message;
import fr.ensim.interop.introrest.model.weather.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;


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
			@RequestParam("ville") String ville) throws UnsupportedEncodingException {

		RestTemplate restTemplate = new RestTemplate();

		ResponseEntity<City[]> responseEntity = restTemplate.getForEntity(openweatherUrl + "geo/1.0/direct?q={ville}&limit=3"
						+ "&appid=" + openweatherToken,
				City[].class, ville);

		System.out.println(openweatherUrl + "geo/1.0/direct?q="+ URLEncoder.encode(ville, "UTF-8") + "&limit=3&appid=" + openweatherToken);

		City[] cities = responseEntity.getBody();
		if (cities.length > 0) {
			City city = cities[0];

			System.out.println(openweatherUrl + "data/2.5/weather?lat=" + city.getLat() +
					"&lon=" + city.getLon() + "&appid=" + openweatherToken);

			OpenWeather openWeather = restTemplate.getForObject(openweatherUrl + "data/2.5/weather?lat={lat}"
							+ "&lon={longitude}&appid=" + openweatherToken + "&lang=fr",
					OpenWeather.class, city.getLat(), city.getLon());

			return ResponseEntity.ok().body(openWeather);
		}else{
			Message message = new Message();
			message.setText("La ville que vous avez entrée n'est pas reconnu par openWeatherMap.");

			ResponseEntity<ApiResponseTelegram> apiResponseTelegram = messageRestController.sendMessage(message);

			System.out.println(apiResponseTelegram);
			return ResponseEntity.ok().body(null);
		}
	}

	@GetMapping(value = "/meteoSemaine", params = {"ville","semaine"} )
	public ResponseEntity<OpenWeatherList> meteoSemaine(@RequestParam("ville") String ville, @RequestParam("semaine") Boolean semaine) throws UnsupportedEncodingException {

		RestTemplate restTemplate = new RestTemplate();

		ResponseEntity<City[]> responseEntity = restTemplate.getForEntity(openweatherUrl + "geo/1.0/direct?q={ville}&limit=3"
						+ "&appid=" + openweatherToken + "&lang=fr",
				City[].class, ville);

		System.out.println(openweatherUrl + "geo/1.0/direct?q="+ URLEncoder.encode(ville, "UTF-8") + "&limit=3&appid=" + openweatherToken + "&lang=fr");

		City[] cities = responseEntity.getBody();
		City city = cities[0];



		System.out.println(openweatherUrl + "data/2.5/forecast?lat=" + city.getLat() +
				"&lon=" + city.getLon() + "&appid=" + openweatherToken + "&lang=fr");

		OpenWeatherList openWeatherList = restTemplate.getForObject(openweatherUrl + "data/2.5/forecast?lat={lat}&lon={lon}&"
				+ "appid=" + openweatherToken + "&lang=fr", OpenWeatherList.class, city.getLat(), city.getLon());

		System.out.println(openWeatherList.list.get(0).getWeather().get(0).getDescription());

		Iterator itr = openWeatherList.getList().iterator();
		if (!semaine){
			while(itr.hasNext() && openWeatherList.getList().size() > 1){
				itr.next();
				itr.remove();
			}
		}

		return ResponseEntity.ok(openWeatherList);

		}
}
