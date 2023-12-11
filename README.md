# ProjetNoSQL

## Présentation du projet

Le but de ce projet est de permettre la gestion d'un grand nombre de protéines en utilisant deux bases de données NoSQL.
Une base de données Neo4J a été utilisée pour permettre la gestion des relations entre les protéines et la visualisation des protéines similaires.
Une base de données MongoDB a été utilisée pour permettre la recherche de protéines à partir de différentes caractéristiques.


## Installation

### Prérequis

- Java 17
- Maven 4.0.0
- npm >= 7.22.0

### Installation

- Cloner le projet
- Lancer la commande `mvn clean install` à la racine du projet
- Lancer la commande `npm install` dans le dossier `frontend`
- Lancer la commande `npm run ng serve` dans le dossier `frontend`
- Lancer la commande `mvn spring-boot:run` à la racine du projet
- Aller sur `localhost:4200` dans un navigateur