package net.doepner.ui;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.Image;
import java.util.Iterator;
import java.util.function.Function;

import static javax.swing.JOptionPane.CLOSED_OPTION;
import static javax.swing.JOptionPane.DEFAULT_OPTION;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.QUESTION_MESSAGE;

/**
 * Prompts user to select an email recipient
 */
public final class SwingEmailDialog implements EmailDialog {

    private static final String NO_CHOICE = null;

    private final Function<String, Iterable<Image>> imageCollector;

    private final JPanel inputPanel;
    private final JTextField subjectField;

    public SwingEmailDialog(Function<String, Iterable<Image>> imageCollector) {
        this.imageCollector = imageCollector;

        subjectField = new JTextField("Typepad message");

        inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(new JLabel("Subject: "), BorderLayout.WEST);
        inputPanel.add(subjectField, BorderLayout.CENTER);
    }

    @Override
    public String chooseRecipient(String[] recipients) {
        if (recipients == null || recipients.length == 0) {
            JOptionPane.showMessageDialog(null, "No email recipients configured",
                    "Error", ERROR_MESSAGE);
            return NO_CHOICE;
        }

        final Icon[] options = new Icon[recipients.length];
        for (int i = 0; i < options.length; i++) {
            options[i] = createIcon(recipients[i]);
        }

        final int choice = JOptionPane.showOptionDialog(null,
                inputPanel, "Send email ...",
                DEFAULT_OPTION, QUESTION_MESSAGE, null,
                options, options[0]);

        return choice == CLOSED_OPTION ? NO_CHOICE : recipients[choice];
    }

    private Icon createIcon(String name) {
        final Iterator<Image> images = imageCollector.apply(name).iterator();
        if (images.hasNext()) {
            return new ImageIcon(images.next());
        } else {
            return new ImageIcon();
        }
    }

    @Override
    public String getSubject() {
        return subjectField.getText();
    }
}
