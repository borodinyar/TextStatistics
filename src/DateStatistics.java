import java.text.BreakIterator;
import java.text.ParsePosition;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class DateStatistics extends AbstractStatistic<Date> {
    private Date averageValue = null;


    DateStatistics(final Locale inputLocale, final Locale outputLocale) {
        super(inputLocale, outputLocale);
    }

    public void getStatistic(final String text) {
        final Set<Date> uniqueElements = new HashSet<>();
        long time = 0;

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
            Date current = parseDate(text, position);

            if (current == null) {
                continue;
            }

            cnt++;
            uniqueElements.add(current);
            time += current.getTime();

            if (maxValue == null || minValue == null) {
                maxValue = minValue = current;
            } else {
                if (current.after(maxValue)) {
                    maxValue = current;
                }

                if (minValue.after(current)) {
                    minValue = current;
                }
            }

            parsePosition = position.getIndex();
        }

        differentCnt = uniqueElements.size();
        averageValue = new Date(time / cnt);

    }

    public Date getAverageValue() {
        return averageValue;
    }

    public void setAverageValue(Date averageValue) {
        this.averageValue = averageValue;
    }

    public String getString() {
        final String dataFormat = resourceBundle.getString("dataFormat");
        final String average = resourceBundle.getString("averageDate");
        final String commonStatistic = getCommonString("Date");
        return String.join("",
                commonStatistic,
                getStringByFormat(dataFormat, average, averageValue));
    }

}
