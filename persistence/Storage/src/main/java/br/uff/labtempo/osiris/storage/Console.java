/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.storage;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Felipe
 */
public class Console {

    private final String PROMPT_NAME = "Storage";
    private final String PROMPT_DELIMITER = "/";
    private final String PROMPT_CLOSURE = ">";
    private Storage storage;
    private String repository;

    public Console(Storage storage) {
        this.storage = storage;
    }

    public void start() {
        Scanner scan = new Scanner(System.in);
        printPrompt();

        boolean loop = true;
        while (loop && scan.hasNextLine()) {

            String command = scan.nextLine();
            synchronized (this) {
                loop = executeCommand(command);
                if (loop) {
                    printPrompt();
                }
            }
        }
        scan.close();
    }

    private void printPrompt() {
        StringBuilder prompt = new StringBuilder(PROMPT_NAME);

        if (repository != null) {
            prompt.append(PROMPT_DELIMITER);
            prompt.append(repository);
        }

        prompt.append(PROMPT_CLOSURE);

        System.out.print(prompt.toString());

    }

    private void printArray(String[] items, String detail) {
        for (String item : items) {
            if (detail.length() > 0) {
                String format = "\t %-40s %s\n";
                System.out.printf(format, item, detail);
            } else {
                System.out.println(item);
            }
        }
    }

    private void printArray(String[] items) {
        printArray(items, "");
    }

    private void printItem(String item) {
        printArray(new String[]{item});
    }

    private boolean executeCommand(String command) {

        String[] parts = command.split(" ");

        try {

            switch (parseCommand(parts[0])) {
                case LS:
                    if (repository != null) {
                        printArray(storage.listRepositoryKeys(repository), "entry");
                    } else {
                        printArray(storage.listRepositories(), "<REP>");
                    }
                    break;
                case CD:
                    if (repository == null) {
                        if (parts[1] != null && parts[1].length() > 0) {
                            repository = storage.getRepository(parts[1]);
                        }
                    } else {
                        if (parts.length == 1 || "".equals(parts[1]) || "..".equals(parts[1])) {
                            repository = null;
                        }
                    }
                    break;
                case CAT:
                    if (repository != null && parts.length > 1 && parts[1] != null) {
                        printItem(storage.getEntryContent(repository, parts[1]));
                    }
                    break;
                case ECHO:
                    if (parts.length > 1) {
                        if (repository != null && command.contains(">>")) {
                            StringBuilder content = new StringBuilder();
                            int i;
                            for (i = 1; i < parts.length; i++) {
                                if (parts[i].contains(">>")) {
                                    break;
                                }
                                content.append(parts[i]);
                            }
                            String body = content.toString();

                            content.setLength(0);

                            for (i++; i < parts.length; i++) {
                                content.append(parts[i]);
                            }

                            String file = content.toString();

                            storage.addEntry(repository, file, body);

                        } else {
                            StringBuilder content = new StringBuilder();
                            for (int i = 1; i < parts.length; i++) {
                                content.append(parts[i]);
                            }
                            printItem(content.toString());
                        }
                    }
                    break;
                case MKDIR:
                    if (repository == null) {
                        if (parts[1] != null && parts[1].length() > 0) {
                            storage.createRepository(parts[1]);
                        }
                    }
                    break;
                case RM:
                    if (parts.length > 1 && parts[1] != null && parts[1].length() > 0) {
                        if (repository == null) {
                            storage.removeRepository(parts[1]);
                        } else {
                            storage.removeEntry(repository, parts[1]);
                        }
                    }
                    break;
                case EXIT:
                    return false;

            }
        } catch (Exception e) {
        }
        return true;
    }

    private Command parseCommand(String command) {
        return Command.valueOf(command.toUpperCase());
    }

    private enum Command {
        EXIT, LS, CD, CAT, MKDIR, ECHO, RM
    }

}
