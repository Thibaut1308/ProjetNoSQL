package fr.lesbg.Protein;

public class ProteinLinksBuilder {

        private ProteinData source;
        private ProteinData neighbor;
        private ProteinData neighborOfNeighbor;

        public ProteinLinksBuilder setSource(ProteinData source) {
            this.source = source;
            return this;
        }

        public ProteinLinksBuilder setNeighbor(ProteinData neighbor) {
            this.neighbor = neighbor;
            return this;
        }

        public ProteinLinksBuilder setNeighborOfNeighbor(ProteinData neighborOfNeighbor) {
            this.neighborOfNeighbor = neighborOfNeighbor;
            return this;
        }

        public ProteinLinks createProteinLinks() {
            return new ProteinLinks(source, neighbor, neighborOfNeighbor);
        }
}
