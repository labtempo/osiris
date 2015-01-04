/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.sensornet.model.state;

import br.uff.labtempo.osiris.to.common.definitions.State;

/**
 *
 * @author Felipe
 */
public enum ModelState {

    NEW() {
                @Override
                public State getState() {
                    return State.NEW;
                }

                @Override
                public void deactivate(Model model) {
                    change(model, ModelState.INACTIVE);
                }

                @Override
                public void reactivate(Model model) {
                    throw new RuntimeException("Cannot reactive a new item, it already active!");
                }

                @Override
                public void update(Model model) {
                    change(model, ModelState.UPDATED);
                }

                @Override
                public void malfunction(Model model) {
                    change(model, ModelState.MALFUNCTION);
                }

            },
    INACTIVE() {
                @Override
                public State getState() {
                    return State.INACTIVE;
                }

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

                @Override
                public void malfunction(Model model) {
                    throw new RuntimeException("Cannot set to malfunction for a disabled item!");
                }
            },
    UPDATED() {
                @Override
                public State getState() {
                    return State.UPDATED;
                }

                @Override
                public void deactivate(Model model) {
                    change(model, ModelState.INACTIVE);
                }

                @Override
                public void reactivate(Model model) {
                    throw new RuntimeException("Cannot reactive a new item, it already active!");
                }

                @Override
                public void update(Model model) {
                    change(model, ModelState.UPDATED);
                }

                @Override
                public void malfunction(Model model) {
                    change(model, ModelState.MALFUNCTION);
                }
            },
    REACTIVATED() {
                @Override
                public State getState() {
                    return State.REACTIVATED;
                }

                @Override
                public void deactivate(Model model) {
                    change(model, ModelState.INACTIVE);
                }

                @Override
                public void reactivate(Model model) {
                    throw new RuntimeException("Cannot reactive a active item!");
                }

                @Override
                public void update(Model model) {
                    change(model, ModelState.UPDATED);
                }

                @Override
                public void malfunction(Model model) {
                    change(model, ModelState.MALFUNCTION);
                }
            },
    MALFUNCTION() {
                @Override
                public State getState() {
                    return State.MALFUNCTION;
                }

                @Override
                public void deactivate(Model model) {
                    change(model, ModelState.INACTIVE);
                }

                @Override
                public void reactivate(Model model) {
                    throw new RuntimeException("Cannot reactive a active item!");
                }

                @Override
                public void update(Model model) {
                }

                @Override
                public void malfunction(Model model) {
                    change(model, ModelState.MALFUNCTION);
                }
            };

    public abstract void deactivate(Model model);

    public abstract void reactivate(Model model);

    public abstract void update(Model model);

    public abstract void malfunction(Model model);

    public abstract State getState();

    protected void change(Model model, ModelState state) {
        model.state = state;
        model.updateDate();
    }
}
