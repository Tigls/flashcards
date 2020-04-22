package com.tigls.flashcards;

import java.util.*;

public class Main {

    public static void main(String[] args) {
        var exit = 0;
        var scanner = new Scanner(System.in);
        var storage = new Storage(scanner);
        var flashCards = new Flashcards(storage);

        var argsParsed = parseArgs(args);
        if (!argsParsed.isEmpty()) {
            for (String key : argsParsed.keySet()) {
                if ("-import".equals(key)) {
                    flashCards.importCardsFunc(argsParsed.get(key));
                }
            }
        }

        while (exit == 0) {
            storage.printOut("Input the action (add, remove, import, export, ask, exit, log, hardest card, reset stats):");
            var action = storage.readLine();
            switch (action) {
                case "add":
                    flashCards.add();
                    break;
                case "remove":
                    flashCards.remove();
                    break;
                case "import":
                    flashCards.importCards();
                    break;
                case "export":
                    flashCards.exportCards();
                    break;
                case "ask":
                    flashCards.ask();
                    break;
                case "exit":
                    flashCards.exit(argsParsed);
                    exit = 1;
                    break;
                case "log":
                    flashCards.log();
                    break;
                case "hardest card":
                    flashCards.getHardestCard();
                    break;
                case "reset stats":
                    flashCards.resetStats();
                    break;
                default:
                    storage.printOut("Select correct action");
            }
            storage.printOut("");
        }
    }

    public static Map<String, String> parseArgs(String args[]) {
        Map<String, String> result = new HashMap<>();
        if (args.length < 2) {
            return result;
        } else if (args.length < 3) {
            result.put(args[0], args[1]);
            return result;
        } else {
            result.put(args[0], args[1]);
            result.put(args[2], args[3]);
            return result;
        }
    }
}


