/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.to.common.base;

import br.uff.labtempo.osiris.to.common.data.InfoTo;
import br.uff.labtempo.osiris.to.common.definitions.State;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 *
 * @author Felipe
 */
public abstract class ToBaseInfo {

    private final String id;
    private final String state;
    private final Map<String, String> info;

    //helper attributes
    private transient State helperState;
    private transient List<? extends InfoTo> helperInfoToList;

    public ToBaseInfo(String id, State state) {
        this.id = id;
        this.state = state.toString();
        this.info = new HashMap<>();

        this.helperState = state;
    }

    public String getId() {
        return id;
    }

    public State getState() {
        if (helperState == null) {
            helperState = Enum.valueOf(State.class, state);
        }
        return helperState;
    }

    protected void addInfo(Map<String, String> infos) {
        info.putAll(infos);
    }

    protected void addInfo(String infoKeyName, String infoDescription) {
        info.put(infoKeyName, infoDescription);
    }

    protected Map<String, String> getInfo() {
        return info;
    }

    public List<? extends InfoTo> getInfoTo() {
        if (helperInfoToList != null) {
            return helperInfoToList;
        }
        List<InfoTo> infosTo = new ArrayList<>();
        for (Map.Entry<String, String> entrySet : info.entrySet()) {
            String name = entrySet.getKey();
            String description = entrySet.getValue();
            AbsInfoTo infoTo = new AbsInfoTo(name, description);
            infosTo.add(infoTo);
        }
        helperInfoToList = infosTo;
        return infosTo;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + Objects.hashCode(this.state);
        hash = 79 * hash + Objects.hashCode(this.info);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ToBaseInfo other = (ToBaseInfo) obj;
        if (!Objects.equals(this.state, other.state)) {
            return false;
        }
        if (!Objects.equals(this.info, other.info)) {
            return false;
        }
        return true;
    }

    private final class AbsInfoTo extends InfoTo {

        public AbsInfoTo(String name, String description) {
            super(name, description);
        }
    }

}
