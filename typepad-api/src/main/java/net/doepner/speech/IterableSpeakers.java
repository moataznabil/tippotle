package net.doepner.speech;

/**
 * A set of speakers
 */
public interface IterableSpeakers extends Speaker {

    /**
     * Switches to the next speaker
     */
    void nextSpeaker();

}
