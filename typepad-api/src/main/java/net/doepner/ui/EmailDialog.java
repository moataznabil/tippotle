package net.doepner.ui;

/**
 * Email dialog (to select recipient and enter subject)
 */
public interface EmailDialog {

    String chooseRecipient(String[] recipients);

    String getSubject();
}
