export class ProteinModel {

  public static EMPTY_PROTEIN: ProteinModel = new ProteinModel('', '', '', '', '', '', '');
  entry: string;
  entryName: string;
  proteinNames: string;
  interPro: string;
  sequence: string;
  ecNumber: string;
  geneOntology: string;

  constructor(entry: string, entryName: string, proteinNames: string, interPro: string, sequence: string, ecNumber: string, geneOntology: string) {
    this.entry = entry;
    this.entryName = entryName;
    this.proteinNames = proteinNames;
    this.interPro = interPro;
    this.sequence = sequence;
    this.ecNumber = ecNumber;
    this.geneOntology = geneOntology;
  }

  public showInfos(): string {
    return "Entry: " + this.entry + "\n" +
      "Entry name: " + this.entryName + "\n" +
      "Protein names: " + this.proteinNames + "\n" +
      "InterPro: " + this.interPro + "\n" +
      "Sequence: " + this.sequence + "\n" +
      "EC number: " + this.ecNumber + "\n" +
      "Gene ontology: " + this.geneOntology;
  }
}
