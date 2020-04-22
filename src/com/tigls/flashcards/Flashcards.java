package com.tigls.flashcards;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Flashcards {
    private final Map<String, String> map = new LinkedHashMap<>();
    private final Map<String, String> mapReversed = new LinkedHashMap<>();
    private Map<String, Mistake> mistakes = new LinkedHashMap<>();
    private final Storage storage;

    public Flashcards(Storage storage) {
        this.storage = storage;
    }

    public void add() {
        storage.printOut("The card:");
        var term = storage.readLine();
        if (map.containsKey(term)) {
            storage.printOut(String.format("The card \"%s\" already exists.", term));
            return;
        }
        storage.printOut("The definition of the card:");
        var definition = storage.readLine();
        if (map.containsValue(definition)) {
            storage.printOut(String.format("The definition \"%s\" already exists.", definition));
            return;
        }
        map.put(term, definition);
        mapReversed.put(definition, term);
        storage.printOut(String.format("The pair (\"%s\":\"%s\") has been added.", term, definition));
    }

    public void remove() {
        storage.printOut("The card:");
        var term = storage.readLine();
        if (map.containsKey(term)) {
            var def = map.get(term);
            mapReversed.remove(def);
            map.remove(term);
            mistakes.remove(term);
            storage.printOut("The card has been removed.");
        } else {
            storage.printOut(String.format("Can't remove \"%s\": there is no such card.", term));
        }
    }

    public void ask() {
        storage.printOut("How many times to ask?");
        var timesToAsk = Integer.parseInt(storage.readLine());
        while (timesToAsk > 0) {
            var terms = new ArrayList<>(map.keySet());
            var index = getRandomIndex(terms.size());
            var term = terms.get(index);
            var def = map.get(term);
            storage.printOut(String.format("Print the definition of \"%s\":", term));

            var answer = storage.readLine();
            if (def.equals(answer)) {
                String right = "Correct answer.";
                storage.printOut(right);
            } else {
                if (mapReversed.get(answer) != null) {
                    String wrongExtended = "Wrong answer. (The correct one is \"%s\", you've just written the definition of \"%s\" card.)";
                    storage.printOut(String.format(wrongExtended, def, mapReversed.get(answer)));
                } else {
                    String wrong = "Wrong answer. The correct one is \"%s\"";
                    storage.printOut(String.format(wrong, def));
                }
                addMistake(term);
            }
            timesToAsk--;
        }
    }

    private void addMistake(String term) {
        if (mistakes.containsKey(term)) {
            var mistake = mistakes.get(term);
            mistake.increaseCount();
        } else {
            var data = new Mistake(term);
            mistakes.put(term, data);
        }
    }

    public void getHardestCard() {
        var mistakes = this.mistakes.values();
        if (!mistakes.isEmpty()) {
            var hardest = new Mistake("", 0);
            var hardestList = new ArrayList<Mistake>();
            for (Mistake mistake : mistakes) {
                if (mistake.getCount() > hardest.getCount()) {
                    hardest = mistake;
                }
            }
            for (Mistake mistake : mistakes) {
                if (mistake.getCount() == hardest.getCount()) {
                    hardestList.add(mistake);
                }
            }
            if (hardestList.size() == 1) {
                var str = "The hardest card is \"%s\". You have %s errors answering it.";
                var strFormatted = String.format(str, hardest.getTerm(), hardest.getCount());
                storage.printOut(strFormatted);
            } else {
                var cardsStr = new StringBuffer();
                for (Mistake mistake : hardestList) {
                    cardsStr.append(String.format("\"%s\",", mistake.getTerm()));
                }
                cardsStr.deleteCharAt(cardsStr.length()-1);
                var str = "The hardest cards are %s. You have %s errors answering them.";
                var strFormatted = String.format(str, cardsStr, hardest.getCount());
                storage.printOut(strFormatted);
            }
        } else {
            storage.printOut("There are no cards with errors.");
        }
    }

    public void resetStats() {
        this.mistakes = new LinkedHashMap<>();
        storage.printOut("Card statistics has been reset.");
    }

    public void importCards() {
        storage.printOut("File name:");
        var filename = storage.readLine();
        importCardsFunc(filename);
    }

    public void importCardsFunc(String filename) {
        try {
            var data = storage.readFromFile(filename);
            for (String pair : data) {
                var keyValue = pair.split(":");
                if (map.containsKey(keyValue[0])) {
                    mapReversed.remove(map.get(keyValue[0]));
                }
                map.put(keyValue[0], keyValue[1]);
                mapReversed.put(keyValue[1], keyValue[0]);
                mistakes.put(keyValue[0], new Mistake(keyValue[0], Integer.parseInt(keyValue[2])));
            }
            storage.printOut(String.format("%s cards have been loaded.", data.size()));
        } catch (Exception e) {
            storage.printOut("File not found.");
        }
    }

    public void exportCards() {
        storage.printOut("File name:");
        var filename = storage.readLine();
        exportCardsFunc(filename);
    }

    public void exportCardsFunc(String filename) {
        var file = new File(filename);
        if (file.exists()) {
            file.delete();
        }
        var terms = new ArrayList<>(map.keySet());
        try {
            if (terms.isEmpty()) {
                storage.saveToFile(filename, null, null, 0);
            } else {
                for (String term : terms) {
                    storage.saveToFile(filename, term, map.get(term), mistakes.getOrDefault(term, new Mistake("",0)).getCount());
                }
            }
            storage.printOut(String.format("%s cards have been saved", terms.size()));
        } catch (IOException e) {
            storage.printOut("Error");
        }
    }

    public void exit(Map<String, String> argsParsed) {
        storage.printOut("Bye bye!");
        if (!argsParsed.isEmpty()) {
            for (String key : argsParsed.keySet()) {
                if ("-export".equals(key)) {
                    exportCardsFunc(argsParsed.get(key));
                }
            }
        }
    }

    public void log() {
        storage.printOut("File name:");
        var filename = storage.readLine();
        var file = new File(filename);
        if (file.exists()) {
            file.delete();
        }
        try {
            storage.saveLog(filename);
            storage.printOut("The log has been saved.");
        } catch (Exception e) {
            storage.printOut("Error");
        }
    }

    private int getRandomIndex(int len){
        var random = new Random();
        return random.nextInt(len);
    }
}

