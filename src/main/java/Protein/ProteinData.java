package Protein;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

@Getter
@Setter
public class ProteinData {
    @SerializedName("_id")
    private ObjectId id;
    @SerializedName("Entry")
    private String entry;
    @SerializedName("Entry Name")
    private String entryName;
    @SerializedName("Protein names")
    private String proteinName;
    @SerializedName("InterPro")
    private String interPro;
    @SerializedName("Sequence")
    private String sequence;
    @SerializedName("EC number")
    private String ecNumber;
    @SerializedName("Gene Ontology (GO)")
    private String geneOntology;

}


