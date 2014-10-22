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
                    change(model, ModelState.INATIVE);
                }

                @Override
                public void reactivate(Model model) {
                    throw new RuntimeException("Cannot reactive a new item, it already active!");
                }

                @Override
                public void update(Model model) {
                    change(model, ModelState.UPDATED);
                }

            },
    INATIVE() {
                @Override
                public void deactivate(Model model) {
                    throw new RuntimeException("Cannot deactivate a disabled item!");
                }

                @Override
                public void reactivate(Model model) {
                    change(model, ModelState.REACTIVATED);
                }

                @Override
                public void update(Model model) {
                    throw new RuntimeException("Cannot update a disabled item!");
                }
            },
    UPDATED() {
                @Override
                public void deactivate(Model model) {
                    change(model, ModelState.INATIVE);
                }

                @Override
                public void reactivate(Model model) {
                    throw new RuntimeException("Cannot reactive a new item, it already active!");
                }

                @Override
                public void update(Model model) {
                    switch (model.state) {
                        case INATIVE:
                            change(model, ModelState.REACTIVATED);
                            break;
                        case NEW:
                        case REACTIVATED:
                        case UPDATED:
                            change(model, ModelState.UPDATED);
                            break;
                    }
                }
            },
    REACTIVATED() {
                @Override
                public void deactivate(Model model) {
                    change(model, ModelState.INATIVE);
                }

                @Override
                public void reactivate(Model model) {
                    throw new RuntimeException("Cannot reactive a active item!");
                }

                @Override
                public void update(Model model) {
                    change(model, ModelState.UPDATED);
                }
            };

    public abstract void deactivate(Model model);

    public abstract void reactivate(Model model);

    public abstract void update(Model model);

    protected void change(Model model, ModelState state) {
        model.state = state;
        model.updateDate();
    }
}
