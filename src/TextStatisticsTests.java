import org.junit.*;
import org.junit.internal.TextListener;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.*;

@RunWith(JUnit4.class)
public class TextStatisticsTests {

    public static void main(String[] args) {
        JUnitCore junit = new JUnitCore();
        junit.addListener(new TextListener(System.out));
        Result result = junit.run(TextStatisticsTests.class);

        System.exit(result.wasSuccessful() ? 0 : 1);
    }

    @Test
    public void checkUSMoney() {
        final String text = "180,80 ₽ 10 ₽ £13.3 100 $10.2 $101.20 $10.2";
        CurrencyStatistics currencyStatistics = new CurrencyStatistics(Locale.US, Locale.ENGLISH);
        currencyStatistics.getStatistic(text);
        Assert.assertEquals(currencyStatistics.cnt, 3);
        Assert.assertEquals(currencyStatistics.differentCnt, 2);
        Assert.assertEquals(currencyStatistics.maxValue, 101.2);
        Assert.assertEquals(currencyStatistics.minValue, 10.2);
    }

    @Test
    public void checkWords() {
        List<String> words = new ArrayList<>();
        words.add("Lol");
        words.add("kek");
        words.add("Golang");
        words.addAll(Collections.nCopies(20, "amogus"));
        final String test = String.join(" ", words);
        WordStatistics wordStatistics = new WordStatistics(Locale.ENGLISH, Locale.ENGLISH);
        wordStatistics.getStatistic(test);
        Assert.assertEquals(wordStatistics.cnt, 23);
        Assert.assertEquals(wordStatistics.differentCnt, 4);
        Assert.assertEquals(wordStatistics.minValue, "amogus");
        Assert.assertEquals(wordStatistics.maxValue, "Lol");
    }

    @Test
    public void checkNumber() {
        final String text = "190,5 13423.2 2 232432 2" +
                "180,80 ₽ 10 ₽ £13.3 100 $10.2 $101.20 $10.2" +
                "02/06/2022 02/06/2022 02/07/22 abobus cheburek - Monday, 2 June 2021 Lol. \n MMM sjdkvskd \n Jun 2, 2022.";
        NumberStatistics numberStatistics = new NumberStatistics(Locale.ENGLISH, Locale.ENGLISH);
        numberStatistics.getStatistic(text);
        Assert.assertEquals(numberStatistics.cnt, 11);
        Assert.assertEquals(numberStatistics.differentCnt, 10);
        Assert.assertEquals(numberStatistics.minValue, 2.0);
        Assert.assertEquals(numberStatistics.maxValue, 232432.0);
    }

    @Test
    public void checkData() {
        final String text = "02/06/2022 02/06/2022 02/07/22 abobus cheburek - \n MMM sjdkvskd \n Jun 2, 2022.";
        DateStatistics dateStatistics = new DateStatistics(Locale.ENGLISH, Locale.ENGLISH);
        dateStatistics.getStatistic(text);
        Assert.assertEquals(dateStatistics.cnt, 4);
        Assert.assertEquals(dateStatistics.differentCnt, 3);
        Assert.assertEquals(dateStatistics.minValue, new Date("02/06/2022"));
        Assert.assertEquals(dateStatistics.maxValue, new Date("06/02/2022"));
    }

    static final String test_0 = "In my opinion, the most important invention has been light bulbs. " +
            "They were invented in 1879 by Thomas Edison and now nearly all of us use them / the improved versions of those. " +
            "The first light bulb could work for 1500 hours without burning out. Today, it is difficult to find a corner in the world where there is no electricity." +
            " As a result of  the invention of the electric light bulb, a revolution in the field of energy started. It led to new energy breakthroughs  - from power plants to home appliances. " +
            "So it seems to me that the light bulb is a really important invention, perhaps one of the most important ever.";


    @Test
    public void checkSentence() {
        SentenceStatistics sentenceStatistics = new SentenceStatistics(Locale.ENGLISH, Locale.ENGLISH);
        sentenceStatistics.getStatistic(test_0);
        Assert.assertEquals(sentenceStatistics.cnt, 7);
        Assert.assertEquals(sentenceStatistics.differentCnt, 7);
        Assert.assertEquals(sentenceStatistics.getMinValue().length(), 102);
        Assert.assertEquals(sentenceStatistics.getMaxValue().length(), 83);
        Assert.assertEquals(sentenceStatistics.getMaxLength().length(), 111);
        Assert.assertEquals(sentenceStatistics.getMinLength().length(), 65);
    }

    @Test
    public void checkWord() {
        WordStatistics wordStatistics = new WordStatistics(Locale.ENGLISH, Locale.ENGLISH);
        wordStatistics.getStatistic(test_0);
        Assert.assertEquals(wordStatistics.cnt, 110);
        Assert.assertEquals(wordStatistics.differentCnt, 76);
        Assert.assertEquals(wordStatistics.getMinValue(), "a");
        Assert.assertEquals(wordStatistics.getMaxValue(), "world");
        Assert.assertEquals(wordStatistics.getMaxLength(), "breakthroughs");
        Assert.assertEquals(wordStatistics.getMinLength(), "a");
    }
}
