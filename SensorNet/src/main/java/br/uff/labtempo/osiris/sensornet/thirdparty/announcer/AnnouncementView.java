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
package br.uff.labtempo.osiris.sensornet.thirdparty.announcer;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public enum AnnouncementView {

    NETWORK("Network"),
    SENSOR("Sensor"),
    COLLECTOR("Collector"),
    ITEM_REACTIVATED(" reactivate!"),
    ITEM_DISABLED(" disabled!"),
    ITEM_NEW(" discovered!"),
    ITEM_MALFUNCTION(" is working incorrect or partially!"),
    BROKEN_CONSUMABLE_RULE("Consumable broken some rules"),
    CONSUMABLES_BROKEN("Consumables broken: "),
    RULES_BROKEN("Rules broken: ");

    private AnnouncementView(String content) {
        this.content = content;
    }

    private final String content;

    @Override
    public String toString() {
        return content;
    }

}
