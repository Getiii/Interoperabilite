package fr.ensim.interop.introrest.controller;


import fr.ensim.interop.introrest.model.joke.Jokes;
import fr.ensim.interop.introrest.model.telegram.ApiResponseTelegram;
import fr.ensim.interop.introrest.model.telegram.Chat;
import fr.ensim.interop.introrest.model.telegram.Message;
import fr.ensim.interop.introrest.model.telegram.MessageSend;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


@RestController
public class JokeRestController {

    private Jokes jokes = Jokes.getInstance();

    @Value("${telegram.api.url}")
    private String telegramUrl;
    @Value("${telegram.bot.id}")
    private String botId;


    @GetMapping(value = "/joke")
    public ResponseEntity<ApiResponseTelegram> joke(){

        RestTemplate restTemplate = new RestTemplate();
        jokes = Jokes.getInstance();

        MessageSend messageSend = new MessageSend(jokes.getRandom().getBlague());

        System.out.println(messageSend.chat_id);

        ApiResponseTelegram response = restTemplate.postForObject(telegramUrl + botId + "/sendMessage",
                messageSend, ApiResponseTelegram.class);

        System.out.println(ResponseEntity.ok(response));

        return ResponseEntity.ok(response);

    }
}
