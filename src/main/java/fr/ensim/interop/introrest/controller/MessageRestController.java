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
		System.out.println(message.getText());

		ApiResponseTelegram responseTelegram = restTemplate.postForObject(telegramUrl + botId + "/sendMessage",
				messageSend, ApiResponseTelegram.class);

		System.out.println(ResponseEntity.ok(responseTelegram));

		return ResponseEntity.ok(responseTelegram);

	}

	public ApiResponseUpdateTelegram getUpdate(){
		RestTemplate restTemplate = new RestTemplate();
		ApiResponseUpdateTelegram responseTelegram = restTemplate.postForObject(telegramUrl + botId + "/getUpdates?offset=-1",
				null, ApiResponseUpdateTelegram.class);
		return responseTelegram;
	}

}
