package fr.lesbg.Protein;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProteinLinks {

    @SerializedName("source")
    private ProteinData source;

    @SerializedName("neighbor")
    private ProteinData neighbor;

    @SerializedName("neighborOfNeighbor")
    private ProteinData neighborOfNeighbor;

    public ProteinLinks(ProteinData source, ProteinData neighbor, ProteinData neighborOfNeighbor) {
        this.source = source;
        this.neighbor = neighbor;
        this.neighborOfNeighbor = neighborOfNeighbor;
    }

    @Override
    public String toString() {
        return "ProteinLinks{" +
                "source=" + source +
                ", neighbor=" + neighbor +
                ", neighborOfNeighbor=" + neighborOfNeighbor +
                '}';
    }

    public int hashCode() {
        return source.hashCode() + neighbor.hashCode() + neighborOfNeighbor.hashCode();
    }
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProteinLinks that)) return false;
        return source.equals(that.source) && neighbor.equals(that.neighbor) && neighborOfNeighbor.equals(that.neighborOfNeighbor);
    }
}
