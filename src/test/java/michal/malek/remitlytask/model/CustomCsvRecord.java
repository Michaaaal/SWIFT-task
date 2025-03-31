package michal.malek.remitlytask.model;

import com.opencsv.bean.CsvBindByName;

/**
 * Class not related with project domain
 * serves for testing CsvService
 */
public class CustomCsvRecord {
    @CsvBindByName(column = "ID")
    private int id;

    @CsvBindByName(column = "NAME")
    private String name;

    @CsvBindByName(column = "VALUE")
    private double value;

    public CustomCsvRecord() {
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public double getValue() {
        return value;
    }
    public void setValue(double value) {
        this.value = value;
    }
}