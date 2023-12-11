import { Component } from '@angular/core';
import {NodeModel} from "./model/node.model";
import {LinkModel} from "./model/link.model";
import {ProteinModel} from "./model/protein.model";
import {ProteinService} from "./services/protein.service";
import {ProteinlinksModel} from "./model/proteinlinks.model";

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
  inputValueMongoDB: string = '';
  dataSourceProtein: ProteinModel = ProteinModel.EMPTY_PROTEIN;
  dataSourceNeighbours: ProteinModel[] = [];
  options: string[] = ["Entry","EntryName","ProteinNames"];
  optionForMongo: string[] = ["Entry","EntryName","ProteinNames","InterPro", "Sequence", "EC Number", "Gene Ontology"];
  selectedOption: string = "";
  dataSourceNeighboursOfNeighbours: ProteinlinksModel[] = [];
  selectedOptionMongoDB: string= "";
  displayedColumns: string[] = ['position', 'name', 'weight', 'symbol'];
  displayedColumn: string[] = ['entry', 'entryName', 'proteinNames','interPro','sequence','ecNumber','geneOntology']
  proteinList: ProteinModel[] = [];


  dataSource: PeriodicElement[] = [
    {position: 1, name: 'Hydrogen', weight: 1.0079, symbol: 'H'},
    {position: 2, name: 'Helium', weight: 4.0026, symbol: 'He'},
    {position: 3, name: 'Lithium', weight: 6.941, symbol: 'Li'},
    {position: 4, name: 'Beryllium', weight: 9.0122, symbol: 'Be'},
    {position: 5, name: 'Boron', weight: 10.811, symbol: 'B'},
    {position: 6, name: 'Carbon', weight: 12.0107, symbol: 'C'},
    {position: 7, name: 'Nitrogen', weight: 14.0067, symbol: 'N'},
    {position: 8, name: 'Oxygen', weight: 15.9994, symbol: 'O'},
    {position: 9, name: 'Fluorine', weight: 18.9984, symbol: 'F'},
    {position: 10, name: 'Neon', weight: 20.1797, symbol: 'Ne'},
  ];

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
  }

  public updateInput($event: KeyboardEvent) {
    this.inputValue = ($event.target as HTMLInputElement).value;
  }

  public updateInputMongoDB($event: KeyboardEvent) {
    this.inputValueMongoDB = ($event.target as HTMLInputElement).value;
  }

  public reloadWithEntry() {
    this.proteinService.getProteinFromEntry(this.inputValue).subscribe(data => {
      this.dataSourceProtein = data;
      this.dataSourceNeighbours = [];
      this.dataSourceNeighboursOfNeighbours = [];
    });
  }

  public reloadWithEntryName() {
    this.proteinService.getProteinFromEntryName(this.inputValue).subscribe(data => {
      this.dataSourceProtein = data;
      this.dataSourceNeighbours = [];
      this.dataSourceNeighboursOfNeighbours = [];
    });
  }

  public reloadWithProteinNames() {
    this.proteinService.getProteinFromDescription(this.inputValue).subscribe(data => {
      this.dataSourceProtein = data;
      this.dataSourceNeighbours = [];
      this.dataSourceNeighboursOfNeighbours = [];
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
      console.log(data);
    });
  }

  public getProteinByEntryId() {
    this.proteinList = [];
    this.proteinService.getProteinByEntryId(this.inputValueMongoDB).subscribe(data => {
      console.log(data);
      this.proteinList.push(data)
    });
  }

  public getProteinByGO() {
    this.proteinList = [];
    this.proteinService.getProteinByGO(this.inputValueMongoDB).subscribe(data => {
      console.log(data);
      this.proteinList = data;
    })
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

  public mongoSearch() {
    console.log(this.selectedOptionMongoDB);
    console.log(this.inputValueMongoDB);
    switch (this.selectedOptionMongoDB) {
      case "Entry":
        this.getProteinByEntryId();
        break;
      case "Gene Ontology":
        this.getProteinByGO();
        break;
    }
  }
}

export interface PeriodicElement {
  name: string;
  position: number;
  weight: number;
  symbol: string;
}
