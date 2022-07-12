import java.util.Locale;

public class CurrencyStatistics extends NumberDataStatistics {

    CurrencyStatistics(final Locale inputLocale, final Locale outputLocale) {
        super(inputLocale, outputLocale);
    }

    public void getStatistic(final String text) {
        getNumberStatistic(text, this::parseCurrency);
    }

    public String getString() {
        return getString("Money");
    }

}
