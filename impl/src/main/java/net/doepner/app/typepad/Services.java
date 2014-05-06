package net.doepner.app.typepad;

import net.doepner.file.PathHelper;
import net.doepner.file.StdPathHelper;
import net.doepner.file.TextBuffers;
import net.doepner.file.TextFiles;
import net.doepner.lang.LanguageChanger;
import net.doepner.log.Log;
import net.doepner.speech.AudioFileSpeaker;
import net.doepner.speech.ESpeaker;
import net.doepner.speech.SelectableSpeaker;
import net.doepner.speech.Speaker;
import net.doepner.ui.Images;
import net.doepner.ui.images.ImageHelper;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import static net.doepner.log.Log.Level.error;

/**
 * Application services
 */
public class Services implements IServices {

    private final SelectableSpeaker speaker;
    private final TextBuffers buffers;
    private final Images images;

    private final Log log;

    Services(IContext context) {
        this.log = context.getLog(getClass());

        final PathHelper pathHelper = new StdPathHelper(
                context.getAppName(),
                context.getHomeDirectory(),
                context);

        speaker = createSpeaker(context, pathHelper);
        images = new ImageHelper(pathHelper);
        buffers = new TextFiles(pathHelper);
    }

    private SelectableSpeaker createSpeaker(IContext context,
                                            PathHelper pathHelper) {

        final List<Speaker> speakers = new LinkedList<>();
        final LanguageChanger languageChanger = context.getLanguageChanger();

        speakers.add(new AudioFileSpeaker(pathHelper, languageChanger, context));

        try {
            speakers.add(new ESpeaker(languageChanger));
        } catch (IOException e) {
            log.$(error, e);
        }
        return new SelectableSpeaker(speakers);
    }

    @Override
    public Speaker getSpeaker() {
        return speaker;
    }

    @Override
    public void switchSpeaker() {
        speaker.nextSpeaker();
    }

    @Override
    public Images getImages() {
        return images;
    }

    @Override
    public void loadBuffer(IModel model) {
        model.setText(buffers.load(model.getCurrentBuffer()).trim());
    }

    @Override
    public void saveBuffer(IModel model) {
        buffers.save(model.getText().trim(), model.getCurrentBuffer());
    }
}
