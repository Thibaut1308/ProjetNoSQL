package fr.nosql.statistic;


import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatisticData {

    @SerializedName("type")
    private String type;

    @SerializedName("value")
    private String value;

    public StatisticData(String type, String value) {
        this.type = type;
        this.value = value;
    }

    @Override
    public String toString() {
        return "StatisticData{" +
                "type=" + type +
                ", value=" + value +
                '}';
    }

    public int hashCode() {
        return type.hashCode() + value.hashCode();
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StatisticData that)) return false;
        return type.equals(that.type) && value.equals(that.value);
    }

}
