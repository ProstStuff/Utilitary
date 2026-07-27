package dev.proststuff.utilitary.api.v1.util;

import java.util.ArrayList;
import java.util.List;

public class ActionHistory {
    protected final List<Action> actions = new ArrayList<>();
    protected int index = -1;

    public ActionHistory() {}

    public void perform(Action action) {
        while (actions.size() > index + 1) {
            actions.removeLast();
        }

        action.redo().run();

        actions.add(action);
        index++;
    }

    public boolean undo() {
        if (!canUndo()) return false;
        actions.get(index).undo().run();
        index--;

        return true;
    }

    public boolean redo() {
        if (!canRedo()) return false;
        index++;
        actions.get(index).redo().run();

        return true;
    }

    public boolean canUndo() {
        return index >= 0;
    }

    public boolean canRedo() {
        return index + 1 < actions.size();
    }

    public void clear() {
        actions.clear();
        index = -1;
    }

    public int size() {
        return actions.size();
    }

    public int index() {
        return index;
    }

    public record Action(Runnable redo, Runnable undo) {
    }
}