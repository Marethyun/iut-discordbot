# Bot discord de l'IUT

## Introduction

L'utilité de ce bot est d'attribuer les groupes discord aux étudiants et aux professeurs en début d'année.
Le bot possède également des fonctionnalités basiques d'ajout de commandes.

Il est écrit en Java et utilise la bibliothèque [Discord4J](https://github.com/Discord4J/Discord4J) pour assurer la liaison avec l'API Discord.

Pour toute information supplémentaire s'adresser à Ange Bacci <ange.bacci@etu.univ-amu.fr>.

## Installation

Pour faire fonctionner le bot, un JRE en version 8 minimum est nécessaire. Il n'a pas été testé dans les versions suivantes de Java mais il n'a aucune raison de ne pas fonctionner avec celles-ci.

Seul le fichier `jar` est nécessaire. Gardez en tête que des fichiers de configuration vont être créés dans le même répertoire.
Le profil d'utilisation du programme est le suivant:

`java -jar iut-discordbot-1.0.jar [Fichier CSV] [Configuration générique] [Configuration des groupes] [Configuration des messages]`

Les 4 paramètres sont optionels, et accueillent un chemin (relatif ou absolu) vers les fichiers correspondants.
Par défaut, ces différents fichiers sont créés sous ces noms:
* Fichier CSV: `data.csv`
* Configuration générique: `config.yaml`
* Configuration des groupes: `groups.yaml`
* Configuration des messages: `messages.yaml`

Dès le premier lancement, les fichiers manquants sont créés et le programme termine. Le bot est prêt à être configuré.

## Configuration

Les fichiers de configuration utilisent le format YAML.
Les fichiers ainsi créés contiennent une configuration par défaut. Les fichiers de configuration sont commentés et les explications concernant les différents champs y sont exposées.

## Données

Les données lues pour l'attribution des groupes sont au format CSV [RFC4180](https://tools.ietf.org/html/rfc4180).

## Usage

L'attribution des groupes se fait en envoyant un message au bot en privé.

## Ajout de fonctionnalités

Les fonctionnalités métier sont contenues dans:
* Le constructeur de la classe principale `fr.marethyun.App` (pour l'ajout des commandes)
* Les classes filles de l'interface `fr.marethyun.CommandsRegister.CommandExecutor` pour le contenu des commandes

