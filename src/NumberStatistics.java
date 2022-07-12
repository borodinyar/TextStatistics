import java.util.Locale;

public class NumberStatistics extends NumberDataStatistics{

    NumberStatistics(final Locale inputLocale, final Locale outputLocale) {
        super(inputLocale, outputLocale);
    }

    public void getStatistic(final String text) {
        getNumberStatistic(text, this::parseNumber);
    }


    public String getString() {
        return getString("Number");
    }

}
