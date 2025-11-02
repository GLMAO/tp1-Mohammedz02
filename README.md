[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/t19xNtmg)

IASD-23




# Horloge avec Compteurs à Rebours

## Description

Cette application Java implémente un système d'horloge avec compteurs à rebours utilisant le pattern Observer. Le projet comprend :

- **TimerService** : Service de gestion du temps qui notifie les observateurs à chaque changement
- **Horloge** : Client console qui affiche l'heure en temps réel
- **HorlogeGUI** : Interface graphique Swing avec :
  - Affichage de l'heure en temps réel (format HH:MM:SS.T)
  - Gestion de plusieurs compteurs à rebours simultanés
  - Possibilité d'ajouter/supprimer des compteurs à rebours

## Fonctionnalités

- Affichage de l'heure actuelle en temps réel (mise à jour toutes les 100ms)
- Compteurs à rebours personnalisables (heures, minutes, secondes)
- Interface graphique moderne avec affichage digital
- Pattern Observer pour la communication entre le service et les clients

## Bugs Corrigés

### Bug 1 : Envoi de la mauvaise valeur dans `minutesChanged()`
**Fichier** : `DummyTimeServiceImpl.java` (ligne 127)

**Problème** : La méthode `minutesChanged()` envoyait la valeur de `secondes` au lieu de `minutes` aux listeners.

```java
// AVANT (incorrect)
l.propertyChange(TimerChangeListener.MINUTE_PROP, oldValue, secondes);

// APRÈS (corrigé)
l.propertyChange(TimerChangeListener.MINUTE_PROP, oldValue, minutes);
```

**Impact** : Les observateurs recevaient des notifications incorrectes lors du changement de minutes.

### Bug 2 : Envoi de la mauvaise valeur dans `heuresChanged()`
**Fichier** : `DummyTimeServiceImpl.java` (ligne 144)

**Problème** : La méthode `heuresChanged()` envoyait la valeur de `secondes` au lieu de `heures` aux listeners.

```java
// AVANT (incorrect)
l.propertyChange(TimerChangeListener.HEURE_PROP, oldValue, secondes);

// APRÈS (corrigé)
l.propertyChange(TimerChangeListener.HEURE_PROP, oldValue, heures);
```

**Impact** : Les observateurs recevaient des notifications incorrectes lors du changement d'heures.

### Amélioration : Gestion des listeners
**Fichier** : `DummyTimeServiceImpl.java` (lignes 61-70)

**Problème** : Les méthodes `addTimeChangeListener()` et `removeTimeChangeListener()` n'avaient pas de vérifications.

**Amélioration** : Ajout de vérifications de nullité et de duplication pour éviter les erreurs et les doublons.

```java
@Override
public void addTimeChangeListener(TimerChangeListener pl) {
    if (pl != null && !listeners.contains(pl)) {
        listeners.add(pl);
    }
}
```

## Étapes d'Implémentation

### Étape 1 : Correction des bugs dans DummyTimeServiceImpl
- Correction des deux bugs dans les méthodes `minutesChanged()` et `heuresChanged()`
- Amélioration de la gestion des listeners avec vérifications

### Étape 2 : Implémentation complète de la classe Horloge
- Implémentation de l'interface `TimerChangeListener`
- Modification du constructeur pour accepter `TimerService` et s'enregistrer automatiquement
- Amélioration du formatage de l'heure avec `String.format("%02d:%02d:%02d.%d")`
- Implémentation de `propertyChange()` pour les mises à jour en temps réel
- Ajout de la méthode `disconnect()` pour le nettoyage

### Étape 3 : Création de l'interface graphique HorlogeGUI
- Création d'une fenêtre Swing avec affichage de l'heure
- Implémentation du pattern Observer pour les mises à jour en temps réel
- Affichage digital avec police monospace et couleur verte sur fond noir
- Gestion thread-safe avec `SwingUtilities.invokeLater()`

### Étape 4 : Ajout des compteurs à rebours
- Création d'une classe interne `CountdownTimer` pour gérer les compteurs
- Ajout d'un panneau scrollable pour afficher plusieurs compteurs
- Interface pour ajouter des compteurs avec spinners (H/M/S)
- Bouton "Supprimer" pour chaque compteur
- Mise à jour en temps réel synchronisée avec l'horloge principale
- Affichage "TERMINÉ!" en rouge quand un compteur atteint zéro

### Étape 5 : Intégration dans App.java
- Création de l'instance `TimerService`
- Création de l'horloge console
- Création et lancement de l'interface graphique
- Configuration du thread principal pour maintenir l'application active

## Compilation et Exécution

```bash
cd tp-gl-master
mvn clean compile
mvn package
java -cp launcher/target/launcher-0.0.1-jar-with-dependencies.jar org.emp.gl.core.launcher.App
```

## Auteur

HARCHOUCHE Mohammed Amine - IASD-23
