package fr.lesbg.Protein;

public class ProteinLinksBuilder {

        private ProteinData source;
        private ProteinData neighbor;
        private ProteinData neighborOfNeighbor;
        private String jaccardSourceNeighbor;
        private String jaccardNeighborNeighborOfNeighbor;

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

        public ProteinLinksBuilder setJaccardSourceNeighbor(String jaccardSourceNeighbor) {
            this.jaccardSourceNeighbor = jaccardSourceNeighbor;
            return this;
        }

        public ProteinLinksBuilder setJaccardNeighborNeighborOfNeighbor(String jaccardNeighborNeighborOfNeighbor) {
            this.jaccardNeighborNeighborOfNeighbor = jaccardNeighborNeighborOfNeighbor;
            return this;
        }

        public ProteinLinks createProteinLinks() {
            return new ProteinLinks(source, neighbor, neighborOfNeighbor, jaccardSourceNeighbor, jaccardNeighborNeighborOfNeighbor);
        }
}
