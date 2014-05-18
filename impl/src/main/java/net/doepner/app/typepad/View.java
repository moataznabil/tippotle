package net.doepner.app.typepad;

import net.doepner.app.typepad.action.ActionDescriptions;
import net.doepner.app.typepad.ui.SwingFrame;
import net.doepner.i18n.L10n;
import net.doepner.lang.Language;
import net.doepner.log.LogProvider;
import net.doepner.ui.Editor;
import net.doepner.ui.EmailDialog;
import net.doepner.ui.IAction;
import net.doepner.ui.ImageContainer;
import net.doepner.ui.Images;
import net.doepner.ui.SwingAction;
import net.doepner.ui.SwingEditor;
import net.doepner.ui.SwingEmailDialog;
import net.doepner.ui.UiAction;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

public class View implements IView {

    private final SwingFrame frame;

    private final Collection<UiAction> uiActions = new LinkedList<>();

    private final L10n<IAction, String> actionDescr = new ActionDescriptions();
    private final LogProvider logProvider;
    private final EmailDialog emailDialog;

    public View(String appName, LogProvider logProvider, Images images) {
        this.logProvider = logProvider;

        final SwingEditor editor = new SwingEditor(
                new Font("serif", Font.PLAIN, 40)
        );

        final Dimension frameSize = new Dimension(800, 600);
        final Dimension imageSize = new Dimension(100, 100);

        frame = new SwingFrame(appName, editor, imageSize, frameSize);

        emailDialog = new SwingEmailDialog(frame, images);
    }

    @Override
    public void setActions(IAction... actions) {
        int i = 0;
        for (IAction action : actions) {
            final UiAction uiAction = new SwingAction(action, actionDescr, logProvider);
            frame.addAction(uiAction, i);
            uiActions.add(uiAction);
            i++;
        }
        frame.addOtherToolbarComponents();
    }

    @Override
    public void setLanguage(Language language) {
        for (UiAction uiAction : uiActions) {
            uiAction.setLanguage(language);
        }
    }

    @Override
    public Editor getEditor() {
        return frame.getEditor();
    }

    @Override
    public EmailDialog getEmailDialog() {
        return emailDialog;
    }

    @Override
    public void show() {
        frame.show();
    }

    @Override
    public void showWordImages(Iterable<Image> images) {
        setImages(images, frame.getWordImagePanels());
    }

    @Override
    public void showCharImages(Iterable<Image> images) {
        setImages(images, frame.getCharImagePanels());
    }

    private static void setImages(Iterable<Image> images,
                                  Iterable<? extends ImageContainer> panels) {
        final Iterator<Image> imageIter = images.iterator();
        for (ImageContainer panel : panels) {
            panel.setImage(imageIter.hasNext() ? imageIter.next() : null);
        }
    }
}
