package net.doepner.resources;

import net.doepner.file.MediaType;
import net.doepner.file.PathHelper;
import net.doepner.file.PathType;
import net.doepner.lang.Language;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;

/**
 * Finds a resource on the file system
 */
public final class FileFinder implements ResourceStore {

    private final PathHelper helper;

    public FileFinder(PathHelper helper) {
        this.helper = helper;
    }

    @Override
    public URL find(MediaType mediaType, String name, Language language,
                    String category) {
        final Path dir = storageDir(mediaType, language, category);
        return url(dir, name, mediaType);
    }

    @Override
    public Path storageDir(MediaType mediaType, Language language,
                           String category) {
        final Path mediaDir = directory(mediaType);
        final Path languageDir = mediaDir.resolve(toDirName(language));
        return languageDir.resolve(toDirName(category));
    }

    private static String toDirName(String category) {
        return category != null ? category : "_";
    }

    private static String toDirName(Language language) {
        return language != null ? language.code() : "_";
    }

    private Path directory(MediaType mediaType) {
        final String dirName = mediaType.groupingName();
        return helper.findOrCreate(dirName, PathType.DIRECTORY);
    }

    private URL url(Path dir, String name, MediaType mediaType) {
        final Path file = helper.findInDir(dir, name, mediaType);
        if (file != null) {
            try {
                return file.toUri().toURL();
            } catch (MalformedURLException e) {
                throw new IllegalStateException(e);
            }
        } else {
            return null;
        }
    }
}
