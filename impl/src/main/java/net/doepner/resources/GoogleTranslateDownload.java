package net.doepner.resources;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Path;

import net.doepner.lang.LanguageProvider;

/**
 * Downloads spoken word from Goggle Translate site,
 * with language-specific pronunciation
 */
public class GoogleTranslateDownload implements ResourceFinder {

    private static final String USER_AGENT = "Mozilla/5.0 (X11; Linux i686; rv:21.0) " +
        "Gecko/20100101 Firefox/21.0 Iceweasel/21.0";

    private final LanguageProvider languageProvider;
    private final Path cacheBaseDir;

    public GoogleTranslateDownload(LanguageProvider languageProvider, Path cacheBaseDir) {
        this.languageProvider = languageProvider;
        this.cacheBaseDir = cacheBaseDir;
    }

    @Override
    public URL find(String name) {
        try {
            return getUrl(name);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private URL getUrl(String name) throws IOException {
        final URL url = getDownloadUrl(name);
        final URLConnection c = url.openConnection();
        c.setRequestProperty("User-Agent", USER_AGENT);

        final ReadableByteChannel rbc = Channels.newChannel(c.getInputStream());
        final Path cacheDir = cacheBaseDir.resolve(languageProvider.getLanguage().getCode());
        final File file = cacheDir.resolve(name + '.' + "mp3").toFile();

        try (final FileOutputStream fos = new FileOutputStream(file)) {
            final FileChannel channel = fos.getChannel();
            channel.transferFrom(rbc, 0, Long.MAX_VALUE);
        }
        return file.toURI().toURL();
    }

    private URL getDownloadUrl(String name) throws MalformedURLException {
        return new URL("http://translate.google.com/translate_tts?ie=UTF-8" +
            "&tl=" + languageProvider.getLanguage().getCode() +
            "&q=" + name.toLowerCase());
    }
}
