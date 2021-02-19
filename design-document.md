# Projet conception logicielle
> Équipe : 
> - Alexandre FROEHLICH
> - Guillaume LEINEN
> - Jean-Noël CLINK
> - Ayrwan GUILLERMO
> - Erwan AUBRY

## Introduction au projet

Nous représentons *EnStar.com*, une entreprise de conception d'outils de communication par texte.

Nous allons devoir concevoir une app desktop : *EnstarDesktop* qui permet à chaque utilisateur de communiquer avec les autres sur le réseau. Chaque utilisateur est identifié.

## Composition des équipes

**Team Backend**

-   Alexandre FROEHLICH
-   Erwan AUBRY

**Team Frontend**

*   Guillaume LEINEN
*   Jean-Noël CLINK
*   Ayrwan GUILLERMO

## Points à implémenter

### FrontEnd Utilisateur

-   [ ] Identification (login, password)
-   [ ] DM (messages privés entre utilisateurs)
-   [ ] Groupes de discussion (plusieurs utilisateurs)
    -   [ ] Création de groupes
    -   [ ] Messagerie de groupe
-   [ ] Rechercher un utilisateur
-   [ ] Vérifier si un utilisateur est en ligne

### FrontEnd Administrateur

-   [ ] Identification identique à l'utilisateur
-   [ ] Gestion des utilisateurs
    -   [ ] Création des utilisateurs
    -   [ ] Importation depuis un fichier (bdd, csv, …)
-   [ ] Avoir les utilisateurs en cours d'échange (?)

### Backend

-   [ ] Serveur TCP concurrent
-   [ ] Motif sujet-observateur
-   [ ] Motif délégation
-   [ ] Motif stratégie
-   [ ] Motif composant-composite

### Bonus

-   [ ] Discussions par sujet
-   [ ] Historique chiffré (AES, XOR, …)
-   [ ] Historique des conversations (avec un cache local)

## Méthode

L'interface sera développée avec [JavaFX](https://openjfx.io/). Les tests unitaires seront réalisés avec [JUnit](https://junit.org/junit5/). La documentation sera générée avec [JavaDoc](https://fr.wikipedia.org/wiki/Javadoc). Le diagramme de classe sera réalisé avec [draw.io](https://app.diagrams.net/).

Pour respecter les contraintes du projet, une [release sera effectuée sur Github](https://github.com/Projet-Java-ENSTA-Bretagne/Projet-Conception-Logicielle/releases) toutes les semaines, l'archive ainsi crée sera envoyée sur moodle.

### A rendre chaque semaine

-   [ ] User-stories : sprint en cours avec revue courte
-   [ ] Photo des cartes CRC
-   [ ] Résultat des tests unitaires : fichier XML exporté par JUnit
-   [ ] Un exécutable de l'application
-   [ ] Diagramme de classe
-   [ ] Documentation