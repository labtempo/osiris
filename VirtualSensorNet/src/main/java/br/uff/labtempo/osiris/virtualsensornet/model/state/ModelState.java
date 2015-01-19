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
package br.uff.labtempo.osiris.virtualsensornet.model.state;

import br.uff.labtempo.osiris.to.common.definitions.State;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
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

    protected void change(Model model, ModelState modelState) {
        model.modelState = modelState;
        model.updateDate();
    }
}
