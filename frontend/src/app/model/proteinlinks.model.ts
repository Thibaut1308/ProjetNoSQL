import {ProteinModel} from "./protein.model";

export class ProteinlinksModel {
    source: ProteinModel;
    neighbor: ProteinModel;
    neighborOfNeighbor: ProteinModel;

    constructor(source: ProteinModel, neighbor: ProteinModel, neighborOfNeighbor: ProteinModel) {
        this.source = source;
        this.neighbor = neighbor;
        this.neighborOfNeighbor = neighborOfNeighbor;
    }
}
