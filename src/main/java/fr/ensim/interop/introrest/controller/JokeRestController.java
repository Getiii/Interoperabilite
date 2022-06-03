package fr.ensim.interop.introrest.controller;


import fr.ensim.interop.introrest.model.joke.Joke;
import fr.ensim.interop.introrest.model.joke.Jokes;
import fr.ensim.interop.introrest.model.telegram.ApiResponseTelegram;
import fr.ensim.interop.introrest.model.telegram.MessageSend;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;


@RestController
public class JokeRestController {

    private Jokes jokes = Jokes.getInstance();

    @Value("${telegram.api.url}")
    private String telegramUrl;
    @Value("${telegram.bot.id}")
    private String botId;


    @GetMapping(value = "/joke")
    public ResponseEntity<Joke> joke(){

        jokes = Jokes.getInstance();
        Joke jokePick = jokes.getRandom();

        return ResponseEntity.ok(jokePick);

    }

    public ResponseEntity<Joke> joke(int niveau){

        RestTemplate restTemplate = new RestTemplate();
        jokes = Jokes.getInstance();


        System.out.println(niveau);
        ArrayList<Joke> listBlague = new ArrayList<>();

        for (Map.Entry mapentry : jokes.value().entrySet()) {
            if (niveau == 2){
                if (((Joke)mapentry.getValue()).getNote() < 5)
                    listBlague.add((Joke)mapentry.getValue());
            }else if (niveau == 1){
                if (((Joke)mapentry.getValue()).getNote() > 5)
                    listBlague.add((Joke)mapentry.getValue());
            }
        }


        Collections.shuffle(listBlague);
        Joke jokePick = listBlague.get(0);

        return ResponseEntity.ok(jokePick);
    }


}
