/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.sensornet.model.state;

import java.util.Calendar;

/**
 *
 * @author Felipe
 */
public enum ModelState {

    NEW() {
                @Override
                public void deactivate(Model model) {
                    model.state = ModelState.INATIVE;
                }

                @Override
                public void reactivate(Model model) {
                    throw new RuntimeException("Cannot reactive a new item, it already active!");
                }

                @Override
                public void update(Model model) {
                    model.state = ModelState.UPDATED;
                }

            },
    INATIVE() {
                @Override
                public void deactivate(Model model) {
                    throw new RuntimeException("Cannot deactivate a disabled item!");
                }

                @Override
                public void reactivate(Model model) {
                    model.state = ModelState.REACTIVATED;
                }

                @Override
                public void update(Model model) {
                    throw new RuntimeException("Cannot update a disabled item!");
                }
            },
    UPDATED() {
                @Override
                public void deactivate(Model model) {
                    model.state = ModelState.INATIVE;
                }

                @Override
                public void reactivate(Model model) {
                    throw new RuntimeException("Cannot reactive a new item, it already active!");
                }

                @Override
                public void update(Model model) {
                    model.state = ModelState.UPDATED;
                }
            },
    REACTIVATED() {
                @Override
                public void deactivate(Model model) {
                    model.state = ModelState.INATIVE;
                }

                @Override
                public void reactivate(Model model) {
                    throw new RuntimeException("Cannot reactive a active item!");
                }

                @Override
                public void update(Model model) {
                    model.state = ModelState.UPDATED;
                }
            };

    public abstract void deactivate(Model model);

    public abstract void reactivate(Model model);

    public abstract void update(Model model);

    private Calendar date;

    ModelState() {
        this.date = Calendar.getInstance();
    }

    public Calendar getDate() {
        return date;
    }
}
