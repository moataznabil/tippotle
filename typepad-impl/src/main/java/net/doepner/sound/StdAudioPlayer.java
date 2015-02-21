package net.doepner.sound;

import net.doepner.log.Log;
import net.doepner.log.LogProvider;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.net.URL;

import static javax.sound.sampled.AudioSystem.getAudioFileFormat;
import static javax.sound.sampled.AudioSystem.getAudioInputStream;
import static javax.sound.sampled.AudioSystem.isFileTypeSupported;
import static net.doepner.log.Log.Level.error;

/**
 * Default audio player that delegates to the appropriate stream player
 */
public final class StdAudioPlayer implements AudioPlayer {

    private final AudioStreamPlayer directPlayer;
    private final AudioStreamPlayer convertingPlayer;

    private final Log log;

    public StdAudioPlayer(LogProvider logProvider,
                          AudioStreamPlayer directPlayer,
                          AudioStreamPlayer convertingPlayer) {
        this.directPlayer = directPlayer;
        this.convertingPlayer = convertingPlayer;
        log = logProvider.getLog(getClass());
    }

    @Override
    public void play(final URL url) throws UnsupportedAudioFileException, IOException {
        final AudioFileFormat format = getAudioFileFormat(url);

        final AudioStreamPlayer player = isFileTypeSupported(format.getType())
                ? directPlayer : convertingPlayer;

        play(player, url);
    }

    @Override
    public void stopAll() {
        directPlayer.stopAll();
        convertingPlayer.stopAll();
    }

    private void play(AudioStreamPlayer player, URL url) {
        try (AudioInputStream stream = getAudioInputStream(url)) {

            player.play(stream);

        } catch (UnsupportedAudioFileException | LineUnavailableException e) {
            throw new IllegalArgumentException(e);
        } catch (IOException e) {
            log.as(error, e);
        }
    }
}
