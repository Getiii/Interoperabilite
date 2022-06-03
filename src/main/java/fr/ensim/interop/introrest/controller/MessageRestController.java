package fr.ensim.interop.introrest.controller;

import fr.ensim.interop.introrest.model.telegram.ApiResponseTelegram;
import fr.ensim.interop.introrest.model.telegram.ApiResponseUpdateTelegram;
import fr.ensim.interop.introrest.model.telegram.Message;
import fr.ensim.interop.introrest.model.telegram.MessageSend;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class MessageRestController {

	@Value("${telegram.api.url}")
	private String telegramUrl;
	@Value("${telegram.bot.id}")
	private String botId;
	@Value("${telegram.chat.id}")
	private Long chatId;

	
	//Opérations sur la ressource Message

	@PostMapping("/message")
	public ResponseEntity<ApiResponseTelegram> sendMessage(@RequestBody Message message){

		RestTemplate restTemplate = new RestTemplate();

		if (!StringUtils.hasText(message.getText())){
			return ResponseEntity.badRequest().build();
		}

		MessageSend messageSend = new MessageSend(message.getText());

		ApiResponseTelegram responseTelegram = restTemplate.postForObject(telegramUrl + botId + "/sendMessage",
				messageSend, ApiResponseTelegram.class);

		System.out.println(ResponseEntity.ok(responseTelegram));

		return ResponseEntity.ok(responseTelegram);

	}

	@GetMapping("/update")
	public ApiResponseUpdateTelegram getUpdate(){

		RestTemplate restTemplate = new RestTemplate();
		ApiResponseUpdateTelegram responseTelegram = restTemplate.getForObject(telegramUrl + botId + "/getUpdates?offset=-1", ApiResponseUpdateTelegram.class);
		return responseTelegram;
	}

	@GetMapping("/commandes")
	public void getCommandeDisponible() {
		RestTemplate restTemplate = new RestTemplate();

		String command = "La commande que vous avez saisie n'est pas correct.\n" +
				"Veuillez utiliser une des commandes suivantes :\n" +
				"\tmétéo/weather nom de la ville : pour avoir la météo du jour de la ville.\n" +
				"\tblague pour n'importe quelle type de blague.\n" +
				"\tblague nulle pour une blague noté en dessous de 5.\n" +
				"\tblague drole pour une blague noté en dessus de 6.";

		MessageSend messageSend = new MessageSend(command);

		ApiResponseTelegram responseTelegram = restTemplate.postForObject(telegramUrl + botId + "/sendMessage",
				messageSend, ApiResponseTelegram.class);

		System.out.println(ResponseEntity.ok(responseTelegram));

	}
}
