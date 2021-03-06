package net.doepner.app.tippotle.action;

import net.doepner.speech.Speaker;
import net.doepner.text.TextCoordinates;
import net.doepner.text.WordProvider;
import net.doepner.ui.Action;

import static net.doepner.app.tippotle.action.ActionEnum.SPEAK_WORD;
import static org.guppy4j.BaseUtil.exists;

public final class SpeakWord implements Action {

    private final TextCoordinates textCoordinates;
    private final WordProvider wordProvider;
    private final Speaker speaker;

    public SpeakWord(TextCoordinates textCoordinates,
                     WordProvider wordProvider,
                     Speaker speaker) {
        this.textCoordinates = textCoordinates;
        this.wordProvider = wordProvider;
        this.speaker = speaker;
    }

    @Override
    public void execute() {
        final String word = wordProvider.getWord(textCoordinates.textPosition()).getContent();
        if (exists(word)) {
            speaker.speak(word);
        }
    }

    @Override
    public ActionEnum id() {
        return SPEAK_WORD;
    }

}
