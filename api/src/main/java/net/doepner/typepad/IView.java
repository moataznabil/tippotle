package net.doepner.typepad;

import java.awt.Image;

import net.doepner.lang.Language;
import net.doepner.ui.Editor;
import net.doepner.ui.IAction;

/**
 * Application view interface
 */
public interface IView {

    void show();

    void showWordImages(Iterable<Image> images);

    void showCharImages(Iterable<Image> images);

    void setActions(IAction... actions);

    void setLanguage(Language language);

    Editor getEditor();
}
