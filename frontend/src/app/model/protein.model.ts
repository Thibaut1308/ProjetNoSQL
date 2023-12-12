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
}
