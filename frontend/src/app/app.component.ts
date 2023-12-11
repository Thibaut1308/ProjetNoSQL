import { Component } from '@angular/core';
import {NodeModel} from "./model/node.model";
import {LinkModel} from "./model/link.model";
import {ProteinModel} from "./model/protein.model";
import {ProteinService} from "./services/protein.service";
import {ProteinlinksModel} from "./model/proteinlinks.model";
import {Subject} from "rxjs";
import {ColaForceDirectedLayout} from "@swimlane/ngx-graph";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  nodes: NodeModel[] = [];
  links: LinkModel[] = [];
  inputValue: string = '';

  options: string[] = ["Entry","EntryName","ProteinNames"];
  selectedOption: string = "";

  dataSourceProteins: ProteinModel[] = [];
  nodeNeighbour: NodeModel[] = [];
  linkNeighbour: LinkModel[] = [];

  update$: Subject<any> = new Subject();
  colaForceDirectedLayout: ColaForceDirectedLayout = new ColaForceDirectedLayout();

  constructor(private proteinService: ProteinService) {
  }

  public updateInput($event: KeyboardEvent) {
    this.inputValue = ($event.target as HTMLInputElement).value;
  }

  public generateNodesForNeighborsOfNeighbors(proteinLinks: ProteinlinksModel[]) {
    this.nodes = [];
    this.links = [];
    this.nodes.push(new NodeModel(this.dataSourceProteins[0].entry, this.dataSourceProteins[0].entryName));
    for (let proteinLink of proteinLinks) {
      let nodeNeighbour = new NodeModel(proteinLink.neighbor.entry, proteinLink.neighbor.entryName);
      if (!this.nodeNeighbour.find(node => node.id === nodeNeighbour.id)) {
        this.nodeNeighbour.push(nodeNeighbour);
      }
      let nodeNeighbourOfNeighbour = new NodeModel(proteinLink.neighborOfNeighbor.entry, proteinLink.neighborOfNeighbor.entryName);
      if (!this.nodeNeighbour.find(node => node.id === nodeNeighbourOfNeighbour.id)) {
        this.nodeNeighbour.push(nodeNeighbourOfNeighbour);
      }
      this.linkNeighbour.push(new LinkModel(this.dataSourceProteins[0].entry + proteinLink.neighbor.entry, this.dataSourceProteins[0].entry, proteinLink.neighbor.entry, proteinLink.jaccardSourceNeighbor));
      this.linkNeighbour.push(new LinkModel(proteinLink.neighbor.entry + proteinLink.neighborOfNeighbor.entry, proteinLink.neighbor.entry, proteinLink.neighborOfNeighbor.entry, proteinLink.jaccardNeighborNeighborOfNeighbor));
    }
    console.log(this.nodeNeighbour);
    console.log(this.linkNeighbour);
  }

  public reloadWithEntry() {
    this.proteinService.getProteinFromEntry(this.inputValue).subscribe(data => {
      this.dataSourceProteins = [];
      this.dataSourceProteins.push(data);
      this.getNeighborsAndNeighborsOfNeighbors();
    });
  }

  public reloadWithEntryName() {
    this.proteinService.getProteinFromEntryName(this.inputValue).subscribe(data => {
      this.dataSourceProteins = [];
      this.dataSourceProteins.push(data);
      this.getNeighborsAndNeighborsOfNeighbors();
    });
  }

  public reloadWithProteinNames() {
    this.proteinService.getProteinFromDescription(this.inputValue).subscribe(data => {
      this.dataSourceProteins = [];
      this.dataSourceProteins.push(data);
      this.getNeighborsAndNeighborsOfNeighbors();
    });
  }

  public getNeighborsAndNeighborsOfNeighbors() {
    this.proteinService.getNeighborsAndNeigborsofNeighborsFromEntry(this.dataSourceProteins[0].entry).subscribe(data => {
      this.generateNodesForNeighborsOfNeighbors(data);
    });
  }

  updateChart(){
    this.update$.next(true);
  }

  public display(node: NodeModel) {
    // find all links where node.id is source
    let links = this.links.filter(link => link.source === node.id);
    if (links.length === 0) {
      let linksNeighbor = this.linkNeighbour.filter(link => link.source === node.id);
      let nodesNeighbor : NodeModel[] = [];
      for (let linkNeighbor of linksNeighbor) {
        let nodeNeighbor = this.nodeNeighbour.find(node => node.id === linkNeighbor.target);
        if (nodeNeighbor && !nodesNeighbor.find(node => node.id === nodeNeighbor?.id) && !this.nodes.find(node => node.id === nodeNeighbor?.id)) {
          nodesNeighbor.push(nodeNeighbor);
        }
      }
      this.nodes.push(...nodesNeighbor);
      this.links.push(...linksNeighbor);
    } else { // remove all links and neighbor nodes
      let linksNeighbor = this.links.filter(link => link.source === node.id);
      let nodesNeighbor : NodeModel[] = [];
      for (let linkNeighbor of linksNeighbor) {
        let nodeNeighbor = this.nodes.find(node => node.id === linkNeighbor.target);
        if (nodeNeighbor) {
          if (this.links.filter(node => node.target === nodeNeighbor?.id).length < 2) {
            nodesNeighbor.push(nodeNeighbor);
          }
        }
      }
      this.nodes = this.nodes.filter(node => !nodesNeighbor.find(nodeNeighbor => nodeNeighbor.id === node.id));
      this.links = this.links.filter(link => !linksNeighbor.find(linkNeighbor => linkNeighbor.id === link.id));
    }
    this.updateChart();
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
  }
}
