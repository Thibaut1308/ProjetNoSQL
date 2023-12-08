import { Component } from '@angular/core';
import {NodeModel} from "./model/node.model";
import {LinkModel} from "./model/link.model";
import {ProteinModel} from "./model/protein.model";
import {ProteinService} from "./services/protein.service";
import {FormGroup} from "@angular/forms";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  protected readonly ProteinModel = ProteinModel;
  nodes: NodeModel[] = [];
  links: LinkModel[] = [];
  inputValue: string = '';
  dataSourceProtein: ProteinModel = ProteinModel.EMPTY_PROTEIN;
  dataSourceNeighbours: ProteinModel[] = [];
  dataSourceNeighboursOfNeighbours: Map<ProteinModel, ProteinModel[]> = new Map<ProteinModel, ProteinModel[]>();
  options: string[] = ["Entry","EntryName","ProteinNames"];
  selectedOption: string = "";

  constructor(private proteinService: ProteinService) {
    this.nodes.push(new NodeModel("n1", "Node 1"));
    this.nodes.push(new NodeModel("n2", "Node 2"));
    this.nodes.push(new NodeModel("n3", "Node 3"));
    this.nodes.push(new NodeModel("n4", "Node 4"));
    this.nodes.push(new NodeModel("n5", "Node 5"));

    this.links.push(new LinkModel("l1", "n1", "n2", "Link 1"));
    this.links.push(new LinkModel("l2", "n1", "n3", "Link 2"));
    this.links.push(new LinkModel("l3", "n2", "n3", "Link 3"));
    this.links.push(new LinkModel("l4", "n2", "n4", "Link 4"));
    this.links.push(new LinkModel("l5", "n3", "n4", "Link 5"));
    this.links.push(new LinkModel("l6", "n3", "n5", "Link 6"));
    this.links.push(new LinkModel("l7", "n5", "n1", "Link 7"));

  }

  public updateInput($event: KeyboardEvent) {
    this.inputValue = ($event.target as HTMLInputElement).value;
  }

  public reloadWithEntry() {
    this.proteinService.getProteinFromEntry(this.inputValue).subscribe(data => {
      this.dataSourceProtein = data;
      this.dataSourceNeighbours = [];
      this.dataSourceNeighboursOfNeighbours = new Map<ProteinModel, ProteinModel[]>();
    });
  }

  public reloadWithEntryName() {
    this.proteinService.getProteinFromEntryName(this.inputValue).subscribe(data => {
      this.dataSourceProtein = data;
      this.dataSourceNeighbours = [];
      this.dataSourceNeighboursOfNeighbours = new Map<ProteinModel, ProteinModel[]>();
    });
  }

  public reloadWithProteinNames() {
    this.proteinService.getProteinFromDescription(this.inputValue).subscribe(data => {
      this.dataSourceProtein = data;
      this.dataSourceNeighbours = [];
      this.dataSourceNeighboursOfNeighbours = new Map<ProteinModel, ProteinModel[]>();
    });
  }

  public getNeighbors() {
    this.proteinService.getNeighboursFromEntry(this.dataSourceProtein.entry).subscribe(data => {
      this.dataSourceNeighbours = data;
    });
  }

  public getNeighborsAndNeighborsOfNeighbors() {
    this.proteinService.getNeighborsAndNeigborsofNeighborsFromEntry(this.dataSourceProtein.entry).subscribe(data => {
      this.dataSourceNeighboursOfNeighbours = data;
    });
  }

  public display(node: NodeModel ) {
    console.log(node.id);
    console.log(node.label);
  }

  public search() {
    switch (this.selectedOption) {
      case "Entry":
        this.reloadWithEntry();
        break;
      case "EntryName": {
        this.reloadWithEntryName();
        break;
      }
      case "ProteinNames": {
        this.reloadWithProteinNames()
      }
    }

    console.log(this.selectedOption);
  }
}
