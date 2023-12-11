package fr.nosql.protein;

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

    @SerializedName("jaccardSourceNeighbor")
    private String jaccardSourceNeighbor;

    @SerializedName("jaccardNeighborNeighborOfNeighbor")
    private String jaccardNeighborNeighborOfNeighbor;

    public ProteinLinks(ProteinData source, ProteinData neighbor, ProteinData neighborOfNeighbor, String jaccardSourceNeighbor, String jaccardNeighborNeighborOfNeighbor) {
        this.source = source;
        this.neighbor = neighbor;
        this.neighborOfNeighbor = neighborOfNeighbor;
        this.jaccardSourceNeighbor = jaccardSourceNeighbor;
        this.jaccardNeighborNeighborOfNeighbor = jaccardNeighborNeighborOfNeighbor;
    }

    @Override
    public String toString() {
        return "ProteinLinks{" +
                "source=" + source +
                ", neighbor=" + neighbor.getEntryName() +
                ", neighborOfNeighbor=" + neighborOfNeighbor.getEntryName() +
                ", jaccardSourceNeighbor='" + jaccardSourceNeighbor + '\'' +
                ", jaccardNeighborNeighborOfNeighbor='" + jaccardNeighborNeighborOfNeighbor + '\'' +
                '}';
    }

    public int hashCode() {
        return source.hashCode() + neighbor.hashCode() + neighborOfNeighbor.hashCode() + jaccardSourceNeighbor.hashCode() + jaccardNeighborNeighborOfNeighbor.hashCode();
    }
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProteinLinks that)) return false;
        return source.equals(that.source) && neighbor.equals(that.neighbor) && neighborOfNeighbor.equals(that.neighborOfNeighbor) && jaccardSourceNeighbor.equals(that.jaccardSourceNeighbor) && jaccardNeighborNeighborOfNeighbor.equals(that.jaccardNeighborNeighborOfNeighbor);
    }
}
