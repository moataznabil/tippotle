package net.doepner.event;

/**
 * Propagates change events to listeners
 */
public interface ChangePropagator<T>
        extends ChangeNotifier<T>, ChangeListener<T> {

    // no additional methods
}
