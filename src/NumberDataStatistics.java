import java.text.BreakIterator;
import java.text.ParsePosition;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.function.BiFunction;

public class NumberDataStatistics extends AbstractStatistic<Number> {
    private double averageValue;

    NumberDataStatistics(final Locale inputLocale, final Locale outputLocale) {
        super(inputLocale, outputLocale);
        averageValue = 0;
    }

    public void getNumberStatistic(final String text, BiFunction<String, ParsePosition, Number> function) {
        final Set<Number> uniqueElements = new HashSet<>();
        double summ = 0;

        final BreakIterator breakIterator = BreakIterator.getWordInstance(locale);
        breakIterator.setText(text);
        for (
                int begin = breakIterator.first(), end = breakIterator.next(), parsePosition = 0;
                end != BreakIterator.DONE;
                begin = end, end = breakIterator.next()
        ) {
            if (begin < parsePosition) {
                continue;
            }

            ParsePosition position = new ParsePosition(begin);

            Number current = function.apply(text, position);
            parsePosition = position.getIndex();

            if (current == null) {
                continue;
            }

            cnt++;
            uniqueElements.add(current);
            double number = current.doubleValue();
            summ += number;
            if (maxValue == null || minValue == null) {
                maxValue = minValue = current;
            } else {
                maxValue = Double.max(number, maxValue.doubleValue());
                minValue = Double.min(number, minValue.doubleValue());
            }

        }

        differentCnt = uniqueElements.size();
        averageValue = summ / cnt;
    }

    public double getAverageValue() {
        return averageValue;
    }

    public void setAverageValue(double averageValue) {
        this.averageValue = averageValue;
    }

    public String getString(final String suffix) {
        final String statFormat = resourceBundle.getString("statFormat");
        final String average = resourceBundle.getString("averageNumber");
        return String.join("",
                getCommonString(suffix),
                getStringByFormat(statFormat, average, averageValue));
    }

}
