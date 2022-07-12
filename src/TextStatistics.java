import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

public class TextStatistics {
    private final Locale inputLocale;
    private final Locale outputLocale;

    public TextStatistics(Locale inputLocale, Locale outputLocale) {
        this.inputLocale = inputLocale;
        this.outputLocale = outputLocale;
    }

    public TextStatistics(String inputLanguageTag, String outputLanguageTag) throws TextStatisticsException {
        this.inputLocale = getLocale(inputLanguageTag);
        this.outputLocale = getLocale(outputLanguageTag);

        if (!(outputLocale.getLanguage().equals("ru") || outputLocale.getLanguage().equals("en"))) {
            throw new TextStatisticsException("Invalid output locale. Supported only RU and EN locales");
        }

    }

    private static Locale getLocale(String tag) {
        String[] strings = tag.split("_");
        return new Locale.Builder().setLanguageTag(strings[0]).setRegion(strings[1]).build();
    }

    public static void main(String[] args) {
        if (args == null || args.length != 4 ||
                Arrays.stream(args).anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("Invalid input format. Arguments must be not null and them must be 4");
        }


        try {
            new TextStatistics(args[0], args[1]).calculateStatistics(args[2], args[3]);
        } catch (TextStatisticsException e) {
            System.err.println(e.getMessage());
        }
    }


    public Path setInputFile(String fileName) throws TextStatisticsException {
        try {
            return Paths.get(fileName);
        } catch (InvalidPathException e) {
            throw new TextStatisticsException("Invalid path to input file" + e.getMessage());
        }
    }

    public Path setOutputFileAndMakeDirectory(String fileName) throws TextStatisticsException {
        try {
            Path outputFile = Paths.get(fileName);
            Path parent = outputFile.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            return outputFile;
        } catch (InvalidPathException e) {
            throw new TextStatisticsException("Invalid path to output file" + e.getMessage());
        } catch (IOException e) {
            throw new TextStatisticsException("Can't create not-exists output directory " + e.getMessage());
        }
    }


    public void calculateStatistics(String inputFile, String outputFile) throws TextStatisticsException {
        Path fileInput = setInputFile(inputFile);
        Path fileOutput = setOutputFileAndMakeDirectory(outputFile);
        ResourceBundle resourceBundle = ResourceBundle.getBundle("ResourseBundle", outputLocale);
        final String text;
        try {
            text = Files.readString(fileInput);
        } catch (IOException e) {
            throw new TextStatisticsException("Something wrong with input file " + e.getMessage());
        }

        SentenceStatistics sentenceStatistics = new SentenceStatistics(inputLocale, outputLocale);
        sentenceStatistics.getStatistic(text);
        WordStatistics wordStatistics = new WordStatistics(inputLocale, outputLocale);
        wordStatistics.getStatistic(text);
        NumberStatistics numberStatistics = new NumberStatistics(inputLocale, outputLocale);
        numberStatistics.getStatistic(text);
        CurrencyStatistics currencyStatistics = new CurrencyStatistics(inputLocale, outputLocale);
        currencyStatistics.getStatistic(text);
        DateStatistics dateStatistics = new DateStatistics(inputLocale, outputLocale);
        dateStatistics.getStatistic(text);

        final String analyzedFile = resourceBundle.getString("analyzedFile");
        final String statFormat = resourceBundle.getString("statFormat");
        final String summaryStatistics = resourceBundle.getString("summaryStatistics");
        final String formString = resourceBundle.getString("formString");
        final String cntSentence = resourceBundle.getString("cntSentence");
        final String cntWord = resourceBundle.getString("cntWord");
        final String cntMoney = resourceBundle.getString("cntMoney");
        final String cntDate = resourceBundle.getString("cntDate");
        final String cntNumber = resourceBundle.getString("cntNumber");
        final String commonCnt = String.join("", MessageFormat.format(formString, summaryStatistics) + System.lineSeparator(),
                AbstractStatistic.getStringByFormat(statFormat, cntSentence, sentenceStatistics.cnt),
                AbstractStatistic.getStringByFormat(statFormat, cntWord, wordStatistics.cnt),
                AbstractStatistic.getStringByFormat(statFormat, cntNumber, numberStatistics.cnt),
                AbstractStatistic.getStringByFormat(statFormat, cntMoney, currencyStatistics.cnt),
                AbstractStatistic.getStringByFormat(statFormat, cntDate, dateStatistics.cnt)
        );



        try (final BufferedWriter bufferedWriter = Files.newBufferedWriter(fileOutput)) {
            bufferedWriter.write(String.join("",
                    MessageFormat.format(analyzedFile, inputFile) + System.lineSeparator(),
                    commonCnt,
                    sentenceStatistics.getString(),
                    wordStatistics.getString(),
                    numberStatistics.getString(),
                    currencyStatistics.getString(),
                    dateStatistics.getString()
                    ));
        } catch (IOException e) {
            throw new TextStatisticsException("Something wrong with output file " + e.getMessage());
        }
    }

}
