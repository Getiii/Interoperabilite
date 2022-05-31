package fr.ensim.interop.introrest.model.joke;

import java.util.concurrent.atomic.AtomicInteger;

public class Joke {

    private Integer id;
    private String titre;
    private String blague;
    private int note;

    public Joke(Integer id, String titre, String blague, int note) {
        this.id = id;
        this.titre = titre;
        this.blague = blague;
        this.note = note;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getBlague() {
        return blague;
    }

    public void setBlague(String blague) {
        this.blague = blague;
    }

    public int getNote() {
        return note;
    }

    public void setNote(int note) {
        this.note = note;
    }
}
