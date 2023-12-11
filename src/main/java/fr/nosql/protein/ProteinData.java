package fr.nosql.protein;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class ProteinData {
    @SerializedName("Entry")
    private String entry;
    @SerializedName("Entry Name")
    private String entryName;
    @SerializedName("fr.lesbg.Protein names")
    private String proteinNames;
    @SerializedName("InterPro")
    private String interPro;
    @SerializedName("Sequence")
    private String sequence;
    @SerializedName("EC number")
    private String ecNumber;
    @SerializedName("Gene Ontology (GO)")
    private String geneOntology;

    public ProteinData(String entry, String entryName, String proteinNames, String interPro, String sequence, String ecNumber, String geneOntology) {
        this.entry = entry;
        this.entryName = entryName;
        this.proteinNames = proteinNames;
        this.interPro = interPro;
        this.sequence = sequence;
        this.ecNumber = ecNumber;
        this.geneOntology = geneOntology;
    }

    @Override
    public int hashCode() {
        return Objects.hash(entry, entryName, proteinNames, interPro, sequence, ecNumber, geneOntology);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProteinData that)) return false;
        return Objects.equals(entry, that.entry) && Objects.equals(entryName, that.entryName) && Objects.equals(proteinNames, that.proteinNames) && Objects.equals(interPro, that.interPro) && Objects.equals(sequence, that.sequence) && Objects.equals(ecNumber, that.ecNumber) && Objects.equals(geneOntology, that.geneOntology);
    }

    @Override
    public String toString() {
        return "ProteinData{" +
                "entry='" + entry + '\'' +
                ", entryName='" + entryName + '\'' +
                ", proteinNames='" + proteinNames + '\'' +
                ", interPro='" + interPro + '\'' +
                ", sequence='" + sequence + '\'' +
                ", ecNumber='" + ecNumber + '\'' +
                ", geneOntology='" + geneOntology + '\'' +
                '}';
    }
}


