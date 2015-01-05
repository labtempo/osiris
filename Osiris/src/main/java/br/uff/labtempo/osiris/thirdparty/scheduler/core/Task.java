/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.thirdparty.scheduler.core;

import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Felipe
 */
class Task extends TimerTask {

    private TaskCallback callback;

    public Task(TaskCallback callback) {
        this.callback = callback;
    }

    @Override
    public void run() {
        callback.callback();
    }

}
