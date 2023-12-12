
export class NodeModel {
  id: string;
  label: string;
  expanded: boolean = false;

  constructor(id: string, label: string) {
    this.id = id;
    this.label = label;
  }
}
