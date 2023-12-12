import {ProteinModel} from "./protein.model";

export class NodeModel {
  id: string;
  label: string;
  protein: ProteinModel;
  expanded: boolean = false;

  constructor(id: string, label: string, protein: ProteinModel) {
    this.id = id;
    this.label = label;
    this.protein = protein;
  }
}
