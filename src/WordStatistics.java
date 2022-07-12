import java.text.BreakIterator;
import java.util.Locale;

public class WordStatistics extends StringDataStatistics {

    WordStatistics(final Locale inputLocale, final Locale outputLocale) {
        super(inputLocale, outputLocale);
    }

    public void getStatistic(final String text) {
        getStringStatistic(text, BreakIterator.getWordInstance(locale));
    }

    public String getString() {
        return getString("Word");
    }
}
