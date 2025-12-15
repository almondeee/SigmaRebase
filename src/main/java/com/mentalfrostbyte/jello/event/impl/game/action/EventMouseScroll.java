package com.mentalfrostbyte.jello.event.impl.game.action;

import com.mentalfrostbyte.jello.event.CancellableEvent;

public class EventMouseScroll extends CancellableEvent {
    private final double scroll;

    public EventMouseScroll(double scroll) {
        this.scroll = scroll;
    }

    public double getScroll() {
        return this.scroll;
    }
}
