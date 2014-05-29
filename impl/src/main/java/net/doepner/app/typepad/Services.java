package net.doepner.app.typepad;

import net.doepner.file.PathHelper;
import net.doepner.file.PathType;
import net.doepner.file.StdPathHelper;
import net.doepner.file.TextBuffers;
import net.doepner.file.TextFiles;
import net.doepner.lang.LanguageChanger;
import net.doepner.log.Log;
import net.doepner.mail.Emailer;
import net.doepner.mail.NoEmailer;
import net.doepner.mail.SmtpConfig;
import net.doepner.mail.SmtpEmailer;
import net.doepner.resources.CascadingResourceFinder;
import net.doepner.resources.ClasspathFinder;
import net.doepner.resources.FileFinder;
import net.doepner.resources.GoogleTranslateDownload;
import net.doepner.resources.ImageCollector;
import net.doepner.resources.ResourceFinder;
import net.doepner.resources.StdImageCollector;
import net.doepner.resources.StdResourceCollector;
import net.doepner.speech.AudioFileSpeaker;
import net.doepner.speech.ESpeaker;
import net.doepner.speech.ManagedSpeakers;
import net.doepner.speech.Speaker;
import net.doepner.speech.TestableSpeaker;

import java.io.IOException;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;

import static net.doepner.log.Log.Level.error;
import static net.doepner.log.Log.Level.info;
import static net.doepner.log.Log.Level.warn;

/**
 * Application services
 */
public class Services implements IServices {

    private final ManagedSpeakers speaker;
    private final TextBuffers buffers;

    private final ResourceFinder resourceFinder;
    private final ImageCollector imageCollector;

    private final Emailer emailer;

    private final Log log;

    Services(IContext context) {
        this.log = context.getLog(getClass());

        final PathHelper pathHelper = new StdPathHelper(
                context.getAppName(),
                context.getHomeDirectory(),
                context);

        resourceFinder = new CascadingResourceFinder(
                new FileFinder(pathHelper, context),
                new ClasspathFinder(context),
                new GoogleTranslateDownload(context, pathHelper));

        imageCollector = new StdImageCollector(
                new StdResourceCollector(resourceFinder), 10, context);

        speaker = createSpeaker(context, resourceFinder);
        buffers = new TextFiles(pathHelper);
        emailer = createEmailer(context, pathHelper);
    }

    private Emailer createEmailer(IContext context, PathHelper pathHelper) {
        final Path emailConfigFile = pathHelper.findOrCreate(
                context.getEmailConfigFileName(), PathType.FILE);
        try {
            return new SmtpEmailer(new SmtpConfig(emailConfigFile), context);
        } catch (IOException e) {
            log.as(error, e);
            return new NoEmailer(context);
        }
    }

    private ManagedSpeakers createSpeaker(IContext context,
                                          ResourceFinder resourceFinder) {

        final List<Speaker> speakers = new LinkedList<>();
        final LanguageChanger languageChanger = context.getLanguageChanger();

        addIfFunctional(speakers, new AudioFileSpeaker("google-translate",
                context, resourceFinder, languageChanger));

        addIfFunctional(speakers, new ESpeaker(languageChanger, "robbi"));

        return new ManagedSpeakers(speakers);
    }

    private void addIfFunctional(List<Speaker> speakers, TestableSpeaker speaker) {
        try {
            speaker.test();
            speakers.add(speaker);

        } catch (IllegalStateException e) {
            log.as(warn, "Speaker '{}' not functional. Error: {}",
                    speaker.getName(), e.getMessage());
        }
    }

    @Override
    public ResourceFinder getResourceFinder() {
        return resourceFinder;
    }

    @Override
    public ImageCollector getImageCollector() {
        return imageCollector;
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
    public void loadBuffer(IModel model) {
        final String text = buffers.load(model.getCurrentBuffer()).trim();
        model.setText(text);
        log.as(info, "Loaded buffer {}", model.getCurrentBuffer());
    }

    @Override
    public void saveBuffer(IModel model) {
        final String text = model.getText().trim();
        buffers.save(text, model.getCurrentBuffer());
        log.as(info, "Saved buffer {}", model.getCurrentBuffer());
    }

    @Override
    public Emailer getEmailer() {
        return emailer;
    }
}
