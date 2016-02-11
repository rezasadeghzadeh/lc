package lightner.sadeqzadeh.lightner.rest;

import java.util.List;

public class PackageWordsResponse {
    private List<PackageFlashcard> flashcards;
    private String  status;

    public List<PackageFlashcard> getFlashcards() {
        return flashcards;
    }

    public void setFlashcards(List<PackageFlashcard> flashcards) {
        this.flashcards = flashcards;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
