/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.to.common.base;

import br.uff.labtempo.osiris.to.common.definitions.State;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Felipe
 */
public abstract class CollectorToBase extends ToBaseInfo {

    private final long interval;
    private final String timeUnit;

    //helper attributes
    private transient TimeUnit helperTimeUnit;

    public CollectorToBase(String id, State state, long interval, TimeUnit timeUnit) {
        super(id, state);
        this.interval = interval;
        this.timeUnit = timeUnit.toString();

        this.helperTimeUnit = timeUnit;
    }

    public long getInterval() {
        return interval;
    }

    public TimeUnit getTimeUnit() {
        if (helperTimeUnit == null) {
            helperTimeUnit = Enum.valueOf(TimeUnit.class, timeUnit);
        }
        return helperTimeUnit;
    }
}
