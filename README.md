# Jeux Java Swing — Maze, Snake, Tennis, Sudoku

Un projet pédagogique de mini‑jeux en Java/Swing. Il regroupe quatre jeux indépendants (Labyrinthe, Snake, Tennis/Pong, Sudoku 5x5) pour pratiquer la création d’interfaces graphiques, la gestion des événements clavier, les boucles de jeu avec `javax.swing.Timer`, le dessin 2D et la logique de collision.

## Aperçu
- Interface graphique avec `JFrame`, `JPanel`, `JMenuBar`, `JButton`, `JLabel`.
- Gestion des entrées clavier via `KeyListener` et des boucles de jeu via `Timer`.
- Rendu 2D avec `Graphics` (rectangles, ovales, lignes, grille).
- Logique de jeu: collisions, score, niveaux, obstacles, validation d’entrées.

## Jeux inclus
- Labyrinthe (`MazeGameGUI`): déplacement sur une grille, murs aléatoires, objectif vert, compteur de coups et menu de contrôle.
  - Création de la fenêtre et barre de menu: `games/MazeGameGUI.java:14`–`games/MazeGameGUI.java:58`.
  - Génération du labyrinthe et objectif: `games/MazeGameGUI.java:87`–`games/MazeGameGUI.java:98`.
  - Gestion des touches et victoire: `games/MazeGameGUI.java:135`–`games/MazeGameGUI.java:166`.

- Snake (`SnakeGame`): serpent qui grandit, score, niveaux 1→3, obstacles dynamiques, vitesse adaptée et écran de démarrage/fin.
  - Initialisation et boucle de jeu: `games/SnakeGame.java:59`–`games/SnakeGame.java:71`, `games/SnakeGame.java:248`–`games/SnakeGame.java:256`.
  - Obstacles par niveau: `games/SnakeGame.java:73`–`games/SnakeGame.java:91`.
  - Progression et vitesse: `games/SnakeGame.java:167`–`games/SnakeGame.java:175`.

- Tennis/Pong (`TennisGame`): raquette joueur vs IA, niveaux avec obstacles, score jusqu’à 5, timer ~60 FPS.
  - Initialisation UI/objets: `games/TennisGame.java:41`–`games/TennisGame.java:74`.
  - Collisions et score: `games/TennisGame.java:127`–`games/TennisGame.java:164`.
  - IA et progression de niveau: `games/TennisGame.java:194`–`games/TennisGame.java:207`, `games/TennisGame.java:166`–`games/TennisGame.java:192`.

- Sudoku 5x5 (`SudokuGame`): saisie contrôlée, génération de grille, validation par ligne/colonne et messages d’erreur.
  - Création de la grille et écouteurs: `games/SudokuGame.java:33`–`games/SudokuGame.java:45`, `games/SudokuGame.java:75`–`games/SudokuGame.java:92`.
  - Validation d’entrée et contrôle: `games/SudokuGame.java:94`–`games/SudokuGame.java:132`.

## Ce que ce projet fait apprendre
- Java Swing: structure d’une application GUI (`JFrame`, `JPanel`, `JMenuBar`, composants et layout).
- Événements: `KeyListener`, `ActionListener`, `DocumentListener` pour réagir aux interactions.
- Boucles de jeu: `javax.swing.Timer` pour animer et mettre à jour l’état du jeu.
- Dessin 2D: utilisation de `Graphics` pour dessiner sprites, grilles, HUD et effets.
- Logique de jeu: collisions, gestion du score, niveaux, obstacles, conditions de victoire/défaite.
- Organisation du code: classes par jeu, méthodes dédiées (initialisation, update, rendu, entrées).

## Prérequis
- Java JDK 8+ (recommandé: JDK 17).
- Un IDE (Eclipse, IntelliJ IDEA, VS Code avec extension Java). Le projet inclut des fichiers Eclipse (`.project`, `.classpath`).

## Lancement
- Exécuter le `main` de chaque jeu:
  - Labyrinthe: `games/MazeGameGUI.java:172`–`games/MazeGameGUI.java:175`.
  - Snake: `games/SnakeGame.java:289`–`games/SnakeGame.java:297`.
  - Tennis: `games/TennisGame.java:303`–`games/TennisGame.java:311`.
  - Sudoku: `games/SudokuGame.java:134`–`games/SudokuGame.java:136`.

### Via Eclipse
- Importer en « Projet existant » puis exécuter la classe souhaitée par clic droit → Run As → Java Application.

### Via ligne de commande (Windows)
```powershell
javac games\MazeGameGUI.java && java games.MazeGameGUI
javac games\SnakeGame.java && java games.SnakeGame
javac games\TennisGame.java && java games.TennisGame
javac games\SudokuGame.java && java games.SudokuGame
```

## Points d’amélioration possibles
- Génération de labyrinthe toujours solvable (DFS/backtracking) au lieu de murs aléatoires.
- Menu principal unique (remplacer `games/menu_game.java` et ses `Thread.sleep`) pour naviguer entre jeux.
- Fichier `.gitignore` pour exclure `*.class`, `.settings/`, `.project`, `.classpath`, `*.zip`.
- Persistance du meilleur score et écran d’aide/règles par jeu.
- Ajustements d’IA (Tennis) et patterns d’obstacles plus variés.

## Auteur
ANIS ZAMOUM
Projet éducatif visant à consolider les bases de Java/Swing, la programmation événementielle et la logique de jeux 2D.

