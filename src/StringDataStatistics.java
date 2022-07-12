import java.text.BreakIterator;
import java.text.Collator;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class StringDataStatistics extends AbstractStatistic<String> {
    private double averageValue;
    private String minLength;
    private String maxLength;

    StringDataStatistics(final Locale inputLocale, final Locale outputLocale) {
        super(inputLocale, outputLocale);
        averageValue = 0;
        minLength = null;
        maxLength = null;
    }

    public void getStringStatistic(final String text, BreakIterator breakIterator) {
        final Set<String> uniqueElements = new HashSet<>();
        double len = 0;

        breakIterator.setText(text);
        for (
                int begin = breakIterator.first(), end = breakIterator.next();
                end != BreakIterator.DONE;
                begin = end, end = breakIterator.next()
        ) {

            String current = text.substring(begin, end).trim();

            if (current.codePoints().noneMatch(Character::isLetter)) {
                continue;
            }

            cnt++;
            uniqueElements.add(current);
            len += current.length();

            Collator collator = Collator.getInstance(locale);
            collator.setStrength(Collator.IDENTICAL);

            if (maxValue == null || minValue == null) {
                maxValue = minValue = current;
            } else {
                if (collator.compare(current, maxValue) > 0) {
                    maxValue = current;
                }

                if (collator.compare(current, minValue) < 0) {
                    minValue = current;
                }
            }

            if (maxLength == null || minLength == null) {
                maxLength = minLength = current;
            } else {
                if (current.length() > maxLength.length()) {
                    maxLength = current;
                }

                if (current.length() < minLength.length()) {
                    minLength = current;
                }
            }
        }

        differentCnt = uniqueElements.size();
        averageValue = len / cnt;
    }

    public double getAverageValue() {
        return averageValue;
    }

    public void setAverageValue(double averageValue) {
        this.averageValue = averageValue;
    }

    public String getString(final String suffix) {
        final String statFormat = resourceBundle.getString("statFormat");
        final String lengthFormat = resourceBundle.getString("lengthFormat");
        final String minL = resourceBundle.getString("minLength" + suffix);
        final String maxL = resourceBundle.getString("maxLength" + suffix);
        final String average = resourceBundle.getString("average" + suffix);

        int minLenSz = minLength == null ? 0 : minLength.length();
        int maxLenSz = maxLength == null ? 0 : maxLength.length();

        return String.join("",
                getCommonString(suffix),
                getStringByFormat(lengthFormat, minL, minLenSz, minLength),
                getStringByFormat(lengthFormat, maxL, maxLenSz, maxLength),
                getStringByFormat(statFormat, average, averageValue)
        );
    }

    public String getMinLength() {
        return minLength;
    }

    public void setMinLength(String minLength) {
        this.minLength = minLength;
    }

    public String getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(String maxLength) {
        this.maxLength = maxLength;
    }
}
