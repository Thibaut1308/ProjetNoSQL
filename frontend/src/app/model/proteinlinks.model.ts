import {ProteinModel} from "./protein.model";

export class ProteinlinksModel {
    source: ProteinModel;
    neighbor: ProteinModel;
    neighborOfNeighbor: ProteinModel;
    jaccardSourceNeighbor: string;
    jaccardNeighborNeighborOfNeighbor: string;

    constructor(source: ProteinModel, neighbor: ProteinModel, neighborOfNeighbor: ProteinModel, jaccardSourceNeighbor: string, jaccardNeighborNeighborOfNeighbor: string) {
        this.source = source;
        this.neighbor = neighbor;
        this.neighborOfNeighbor = neighborOfNeighbor;
        this.jaccardSourceNeighbor = jaccardSourceNeighbor;
        this.jaccardNeighborNeighborOfNeighbor = jaccardNeighborNeighborOfNeighbor;
    }
}
