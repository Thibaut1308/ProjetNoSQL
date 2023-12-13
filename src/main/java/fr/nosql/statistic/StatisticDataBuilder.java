package fr.nosql.statistic;

public class StatisticDataBuilder {

    private String type;
    private String value;

    public StatisticDataBuilder setType(String type) {
        this.type = type;
        return this;
    }

    public StatisticDataBuilder setValue(String value) {
        this.value = value;
        return this;
    }

    public StatisticData createStatisticData() {
        return new StatisticData(type, value);
    }
}
