/* 
 * Copyright 2015 Felipe Santos <fralph at ic.uff.br>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package br.uff.labtempo.tlauncher.model;

import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class ExpirableCommand implements Command {

    private final ConsoleCommand command;
    private final long timeInMillis;
    private final Timer timer;
    private boolean running;

    public ExpirableCommand(String command, long timeInMillis) {
        this.timeInMillis = timeInMillis;
        this.command = new ConsoleCommand(command);
        this.timer = new Timer("ExpirableCommand", true);
    }

    @Override
    public void abort() {
        if (!running) {
            return;
        }
        command.abort();
    }

    @Override
    public void execute() throws Exception {
        if (running) {
            return;
        }
        TimerTask task = new Task();
        timer.schedule(task, timeInMillis);
        running = true;
        command.execute();
        task.cancel();
        timer.purge();
        running = false;
    }

    private class Task extends TimerTask {

        @Override
        public void run() {
            abort();
        }
    }
}
