# Projet-Conception-Logicielle

## Introduction 
Ce projet consiste en la création d'un service de messagerie distribuée à l'instar de Teams ou discord. 
Il s'articulera autour d'un client et d'un serveur.
Le serveur est en charge de la distribution des groupes, messages et responsable de l'authentification des utilisateurs. 
Le client permet lui à un utilisateur l'envoi de requètes via une IHM, lui permettant de communiquer avec les autres utilisateurs. 


Membre du projet : Alexandre Froehlich, Guillaume Leinen, Jean-Noël Clink, Erwan Aubry, Ayrwan Guillermo
Serveur : Alexandre, Erwan 
Client : Guillaume, Jean-Noël, Ayrwan

## fonctionnalités et patterns implémentés

### fonctionnalités 
Le serveur est capable de : 

Le client peut quand à lui ! 
- permettre à l'utilisateur de s'authentifier par un login et mdp
- acceder aux discussions auquel l'utilisateur est inscrit
- créer une nouvelle discussion ainsi que de modifier les paramètres d'une discussion
- retrouver les messages précédement envoyés par soi-meme ou d'autres utilisateurs

### Patterns 
Pour ce projet nous avons utilisé un certains nombre de patterns de programmations : 
- serveur TCP concurrent : implémenté au niveau du serveur [ a completer ]
- motif sujet-observateur et délégation : ce motif est implémenté au niveau du client. L'envoi de message n'est pas supervisé par 
le controlleur de discussion mais par une classe observateur. Le Controlleur est un observable et un clic sur le bouton "ENTRER" va enclencher 
une notification envoyé via l'API Java à l'oservateur "MessageSender". C'est lui qui se charge de l'envoi de message. On a donc à la fois une classe observatrice
qui n'interragit pas avec l'élément observé, et une classe qui permet une délégation d'une fonctionnalité.  
- motif stratégie : implémenté au niveau du serveur [ a completer ]
- motif composant-composite : implémenté au niveau du serveur [ a completer ]


                           
## Utilisation

## dévellopement futur

