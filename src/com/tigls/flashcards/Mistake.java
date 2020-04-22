package com.tigls.flashcards;

public class Mistake {
    private final String term;
    private int count;

    Mistake(String term) {
        this.term = term;
        this.count = 1;
    }

    Mistake(String term, int count) {
        this.term = term;
        this.count = count;
    }

    public String getTerm(){
        return term;
    }

    public int getCount(){
        return count;
    }

    public void increaseCount() {
        this.count++;
    }
}
