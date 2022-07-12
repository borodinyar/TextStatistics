import java.text.BreakIterator;
import java.util.Locale;

public class SentenceStatistics extends StringDataStatistics {

    SentenceStatistics(final Locale inputLocale, final Locale outputLocale) {
        super(inputLocale, outputLocale);
    }

    public void getStatistic(final String text) {
        getStringStatistic(text, BreakIterator.getSentenceInstance(locale));
    }

    public String getString() {
        return getString("Sentence");
    }
}
