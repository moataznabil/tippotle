package net.doepner.app.typepad.action;

import net.doepner.i18n.L10n;
import net.doepner.i18n.L10nMapper;
import net.doepner.i18n.L10nRegistry;
import net.doepner.lang.Language;
import net.doepner.ui.ActionId;
import net.doepner.ui.IAction;

import static net.doepner.app.typepad.action.ActionEnum.BIGGER_FONT;
import static net.doepner.app.typepad.action.ActionEnum.EMAIL;
import static net.doepner.app.typepad.action.ActionEnum.SMALLER_FONT;
import static net.doepner.app.typepad.action.ActionEnum.SPEAK_ALL;
import static net.doepner.app.typepad.action.ActionEnum.SPEAK_WORD;
import static net.doepner.app.typepad.action.ActionEnum.SWITCH_BUFFER;
import static net.doepner.app.typepad.action.ActionEnum.SWITCH_LANGUAGE;
import static net.doepner.app.typepad.action.ActionEnum.SWITCH_SPEAKER;
import static net.doepner.lang.LanguageEnum.CANADIAN;
import static net.doepner.lang.LanguageEnum.DEUTSCH;

/**
 * Localized action descriptions
 */
public final class ActionDescriptions implements L10n<IAction, String> {

    private final L10nRegistry<ActionId, String> byId = new L10nMapper<>();

    {
        de(SWITCH_BUFFER, "Textspeicher wechseln");
        de(SWITCH_LANGUAGE, "Sprache wechseln");
        de(SMALLER_FONT, "Schrift verkleinern");
        de(BIGGER_FONT, "Schrift vergrößern");
        de(SPEAK_WORD, "Wort vorlesen");
        de(SWITCH_SPEAKER, "Sprecher wechseln");
        de(EMAIL, "Email versenden");
        de(SPEAK_ALL, "Ganzen Text vorlesen");

        en(SWITCH_BUFFER, "Switch buffer");
        en(SWITCH_LANGUAGE, "Switch language");
        en(SMALLER_FONT, "Decrease font size");
        en(BIGGER_FONT, "Increase font size");
        en(SPEAK_WORD, "Speak word");
        en(SWITCH_SPEAKER, "Switch speaker");
        en(EMAIL, "Send email");
        de(SPEAK_WORD, "Read out the whole text");
    }

    private void en(ActionId key, String value) {
        byId.put(CANADIAN, key, value);
    }

    private void de(ActionId id, String value) {
        byId.put(DEUTSCH, id, value);
    }

    @Override
    public String get(IAction action, Language language) {
        return byId.get(action.id(), language);
    }
}
