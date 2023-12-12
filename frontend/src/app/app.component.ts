import {AfterViewInit, Component, ViewChild} from '@angular/core';
import {NodeModel} from "./model/node.model";
import {LinkModel} from "./model/link.model";
import {ProteinModel} from "./model/protein.model";
import {ProteinService} from "./services/protein.service";
import {ProteinlinksModel} from "./model/proteinlinks.model";
import {Subject} from "rxjs";
import {ColaForceDirectedLayout, DagreClusterLayout, DagreLayout, Layout} from "@swimlane/ngx-graph";
import {MatSnackBar} from "@angular/material/snack-bar";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  nodes: NodeModel[] = [];
  links: LinkModel[] = [];
  inputValue: string = '';
  inputValueMongoDB: string = '';
  options: string[] = ["Entry","EntryName","ProteinNames"];
  selectedOption: string = "";
  dataSourceProteins: ProteinModel[] = [];
  nodeNeighbour: NodeModel[] = [];
  linkNeighbour: LinkModel[] = [];
  update$: Subject<any> = new Subject();
  colarLayout: ColaForceDirectedLayout = new ColaForceDirectedLayout();
  degreLayout: DagreLayout = new DagreLayout();
  layout: Layout;
  optionForMongo: string[] = ["Entry","EntryName","Protein Names","InterPro", "Sequence", "EC Number", "Gene Ontology"];
  selectedOptionMongoDB: string= "";
  displayedColumn: string[] = ['entry', 'entryName', 'proteinNames','interPro','sequence','ecNumber','geneOntology']
  proteinList: ProteinModel[] = [];
  searching: boolean = false;

  constructor(private proteinService: ProteinService,
              private _snackBar: MatSnackBar) {
    this.layout = this.colarLayout;
  }

  public updateInput($event: KeyboardEvent) {
    this.inputValue = ($event.target as HTMLInputElement).value;
  }

  public generateNodesForNeighborsOfNeighbors(proteinLinks: ProteinlinksModel[]) {
    this.nodes = [];
    this.links = [];
    this.nodes.push(new NodeModel(this.dataSourceProteins[0].entry, this.dataSourceProteins[0].entryName, this.dataSourceProteins[0]));
    for (let proteinLink of proteinLinks) {
      let nodeNeighbour = new NodeModel(proteinLink.neighbor.entry, proteinLink.neighbor.entryName, proteinLink.neighbor);
      if (!this.nodeNeighbour.find(node => node.id === nodeNeighbour.id)) {
        this.nodeNeighbour.push(nodeNeighbour);
      }
      let nodeNeighbourOfNeighbour = new NodeModel(proteinLink.neighborOfNeighbor.entry, proteinLink.neighborOfNeighbor.entryName, proteinLink.neighborOfNeighbor);
      if (!this.nodeNeighbour.find(node => node.id === nodeNeighbourOfNeighbour.id)) {
        this.nodeNeighbour.push(nodeNeighbourOfNeighbour);
      }
      if (!this.linkNeighbour.find(link => link.source === this.dataSourceProteins[0].entry && link.target === proteinLink.neighbor.entry)) {
        this.linkNeighbour.push(new LinkModel(this.dataSourceProteins[0].entry + proteinLink.neighbor.entry, this.dataSourceProteins[0].entry, proteinLink.neighbor.entry, proteinLink.jaccardSourceNeighbor));
      }
      if (!this.linkNeighbour.find(link => link.source === proteinLink.neighbor.entry && link.target === proteinLink.neighborOfNeighbor.entry)) {
        this.linkNeighbour.push(new LinkModel(proteinLink.neighbor.entry + proteinLink.neighborOfNeighbor.entry, proteinLink.neighbor.entry, proteinLink.neighborOfNeighbor.entry, proteinLink.jaccardNeighborNeighborOfNeighbor));
      }
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

  public display(nodeModel: NodeModel) {
    let nodeToChange = this.nodes.find(node => node.id === nodeModel.id);
    if (nodeToChange) {
      nodeModel = nodeToChange;
    }
    if (! nodeModel.expanded) {
      let linksOfNode = this.linkNeighbour.filter(link => link.source === nodeModel.id);
      for (let link of linksOfNode) {
        let nodeNeighbour = this.nodeNeighbour.find(node => node.id === link.target);
        if (nodeNeighbour && !this.nodes.find(node => node.id === nodeNeighbour?.id)) {
          this.nodes.push(nodeNeighbour);
        }
        this.links.push(link);
      }
      nodeModel.expanded = true;
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
      case "EntryName":
        this.getProteinByEntryName();
        break;
      case "EC Number":
        this.getProteinByDescription();
        break;
      case "Sequence":
        this.getProteinBySequence();
        break;
      case "InterPro":
        this.getProteinByInterPro();
        break;
      case "Protein Names":
        this.getProteinByProteinNames();
        break;
    }
  }

  public updateInputMongoDB($event: KeyboardEvent) {
    this.inputValueMongoDB = ($event.target as HTMLInputElement).value;
  }

  public getProteinByEntryId() {
    this.proteinList = [];
    let temp: ProteinModel[] = [];
    this.searching = true;
    this.proteinService.getProteinByEntryId(this.inputValueMongoDB).subscribe(data => {
      console.log(data);
      temp.push(data);
      this.proteinList = temp;
      this.searching = false;
    },error => {
      this.displayErrorSnackbar()
      this.searching = false;
    });
  }

  public getProteinByGO() {
    this.proteinList = [];
    this.searching = true;
    this.proteinService.getProteinByGO(this.inputValueMongoDB).subscribe(data => {
      console.log(data);
      this.proteinList = data;
      this.searching = false;
    },error => {
      this.displayErrorSnackbar()
      this.searching = false;
    })
  }

  public getProteinBySequence() {
    this.proteinList = [];
    this.searching = true;
    this.proteinService.getProteinBySequence(this.inputValueMongoDB).subscribe(data => {
      console.log(data);
      this.proteinList = data;
      this.searching = false;
    },error => {
      this.displayErrorSnackbar()
      this.searching = false;
    })
  }

  public getProteinByEntryName() {
    this.proteinList = [];
    let temp: ProteinModel[] = [];
    this.searching = true;
    this.proteinService.getProteinByEntryName(this.inputValueMongoDB).subscribe(data => {
      console.log(data);
      temp.push(data);
      this.proteinList = temp;
      this.searching = false;
    },error => {
      this.displayErrorSnackbar()
      this.searching = false;
    })
  }

  public getProteinByDescription() {
    this.proteinList = [];
    this.searching = true;
    this.proteinService.getProteinByDescription(this.inputValueMongoDB).subscribe(data => {
      console.log(data);
      this.proteinList = data;
      this.searching = false;
    },error => {
      this.displayErrorSnackbar()
      this.searching = false;
    })
  }

  public getProteinByInterPro() {
    this.proteinList = [];
    this.searching = true;
    this.proteinService.getProteinByInterPro(this.inputValueMongoDB).subscribe(data => {
      console.log(data);
      this.proteinList = data;
      this.searching = false;
    },error => {
      this.displayErrorSnackbar()
      this.searching = false;
    })
  }

  public getProteinByProteinNames() {
    this.proteinList = [];
    this.searching = true;
    this.proteinService.getProteinByProteinNames(this.inputValueMongoDB).subscribe(data => {
      console.log(data);
      this.proteinList = data;
      this.searching = false;
    },error => {
      this.displayErrorSnackbar()
      this.searching = false;
    })
  }

  async copyToClipboard(value: string) {
    await navigator.clipboard.writeText(value);
    this._snackBar.open("Copier dans le presse-papier","Fermer",{duration:1000})
  }

  displayErrorSnackbar() {
    this._snackBar.open("Protéine non trouvée？","Fermer",{duration:1000})
  }

  switchLayout($event: any) {
    if ($event.currentTarget.checked) {
      this.layout = this.degreLayout;
    } else {
      this.layout = this.colarLayout;
    }
  }
}
