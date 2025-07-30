package config;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.testng.annotations.DataProvider;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class CSVReader {
    @DataProvider(name = "CSVReader")

    public static Object[][] readCSV() throws Exception {
        List<Object[]> data = new ArrayList<>();
        FileReader reader = new FileReader("./src/test/resources/users.csv");
        CSVParser csvParser = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader);
        for (CSVRecord record : csvParser) {
            data.add(new Object[]{
                    record.get("firstName"),
                    record.get("lastName"),
                    record.get("email"),
                    record.get("password"),
                    record.get("phoneNumber"),
                    record.get("address")
            });
        }
        return data.toArray(new Object[0][]);
    }

}
