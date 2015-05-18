package net.doepner.ui;

import java.awt.Image;

/**
 * Images container (e.g. a panel containing multiple image panels)
 */
public interface ImagesContainer {

    void setImages(Iterable<Image> images);

}
