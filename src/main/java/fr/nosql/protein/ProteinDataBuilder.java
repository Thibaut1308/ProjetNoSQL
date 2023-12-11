package fr.nosql.protein;

public class ProteinDataBuilder {

    private String entry;
    private String entryName;
    private String proteinNames;
    private String interPro;
    private String sequence;
    private String ecNumber;
    private String geneOntology;

    public ProteinDataBuilder setEntry(String entry) {
        this.entry = entry;
        return this;
    }

    public ProteinDataBuilder setEntryName(String entryName) {
        this.entryName = entryName;
        return this;
    }

    public ProteinDataBuilder setProteinNames(String proteinNames) {
        this.proteinNames = proteinNames;
        return this;
    }

    public ProteinDataBuilder setInterPro(String interPro) {
        this.interPro = interPro;
        return this;
    }

    public ProteinDataBuilder setSequence(String sequence) {
        this.sequence = sequence;
        return this;
    }

    public ProteinDataBuilder setEcNumber(String ecNumber) {
        this.ecNumber = ecNumber;
        return this;
    }

    public ProteinDataBuilder setGeneOntology(String geneOntology) {
        this.geneOntology = geneOntology;
        return this;
    }

    public ProteinData createProteinData() {
        return new ProteinData(entry, entryName, proteinNames, interPro, sequence, ecNumber, geneOntology);
    }
}
