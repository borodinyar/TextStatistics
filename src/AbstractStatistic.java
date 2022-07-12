import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.*;
import java.util.stream.Collectors;

public abstract class AbstractStatistic<T> {
    protected NumberFormat inputFormat;
    protected NumberFormat outputFormat;
    protected Locale locale;
    protected ResourceBundle resourceBundle;
    protected int cnt;
    protected int differentCnt;
    protected T minValue;
    protected T maxValue;

    private final List<DateFormat> dateFormats;
    private final NumberFormat numberFormat;
    private final NumberFormat currencyFormat;
    private static final List<Integer> formats = List.of(DateFormat.FULL,
            DateFormat.LONG,
            DateFormat.MEDIUM,
            DateFormat.SHORT);

    AbstractStatistic(final Locale inputLocale, final Locale outputLocale) {
        this.inputFormat = NumberFormat.getNumberInstance(inputLocale);
        this.outputFormat = NumberFormat.getNumberInstance(outputLocale);
        this.locale = inputLocale;
        resourceBundle = ResourceBundle.getBundle("ResourseBundle", outputLocale);

        this.numberFormat = NumberFormat.getNumberInstance(locale);
        this.currencyFormat = NumberFormat.getCurrencyInstance(locale);
        this.dateFormats = formats.stream()
                .map(style -> DateFormat.getDateInstance(style, inputLocale))
                .collect(Collectors.toList());

        cnt = 0;
        differentCnt = 0;
        minValue = null;
        maxValue = null;
    }

    public void setDifferentCnt(int differentCnt) {
        this.differentCnt = differentCnt;
    }

    public Number parseNumber(final String text, ParsePosition position) {
        if (parseDate(text, position) == null && parseCurrency(text, position) == null) {
            return numberFormat.parse(text, position);
        }

        return null;
    }

    public Number parseCurrency(final String text, ParsePosition position) {
        return currencyFormat.parse(text, position);
    }

    public Date parseDate(final String text, ParsePosition position) {
        for (final DateFormat dateFormat : dateFormats) {
            final Date date = dateFormat.parse(text, position);
            if (date != null) {
                return date;
            }
        }
        return null;
    }

    public static String getStringByFormat(final String format, final Object... objects) {
        if (Arrays.stream(objects).anyMatch(Objects::isNull)) {
            return "";
        }
        return "\t" + MessageFormat.format(format, objects) + System.lineSeparator();
    }

    public String getCommonString(final String type) {

        String statFormat = resourceBundle.getString("statFormat");
        if (type.equals("Date")) {
            statFormat = resourceBundle.getString("dataFormat");
        } else if (type.equals("Money")) {
            statFormat = resourceBundle.getString("moneyFormat");
        }

        final String lengthFormat = resourceBundle.getString("lengthFormat");
        final String statUniqueFormat =  resourceBundle.getString("statUniqueFormat");
        final String different =  resourceBundle.getString("different");
        final String statistic =  resourceBundle.getString("statistic" + type);
        final String cntVal = resourceBundle.getString("cnt" + type);
        final String min = resourceBundle.getString("min" + type);
        final String max = resourceBundle.getString("max" + type);


        return String.join("",
                statistic + System.lineSeparator(),
                getStringByFormat(statUniqueFormat, cntVal, cnt, differentCnt, different),
                getStringByFormat(statFormat, min, minValue),
                getStringByFormat(statFormat, max, maxValue));

    }

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    public T getMinValue() {
        return minValue;
    }

    public void setMinValue(T minValue) {
        this.minValue = minValue;
    }

    public T getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(T maxValue) {
        this.maxValue = maxValue;
    }

    public int getDifferentCnt() {
        return differentCnt;
    }
}
