# Projet-DevOps-DerrazNour

Auteur : Nour DERRAZ

Petit projet Java qui sert de support à une chaîne CI/CD complète : Git et GitHub pour le code, GitHub Actions pour l'intégration continue, Jenkins pour le build et le déploiement, Docker pour l'exécution, et Slack pour les notifications.

L'application elle-même est volontairement simple : elle affiche un message.

```
Bonjour et bon courage dans votre projet en DevOps
```

## Structure du projet

```
Projet-DevOps-DerrazNour/
├── .github/workflows/ci.yml        # workflow GitHub Actions
├── src/
│   ├── main/java/com/devops/App.java
│   └── test/java/com/devops/AppTest.java
├── .dockerignore
├── .gitignore
├── Dockerfile
├── docker-compose.yml
├── Jenkinsfile
├── pom.xml
└── README.md
```

## Ce qu'il faut pour l'utiliser

- Java 17
- Maven 3.9 ou plus
- Git
- Docker Desktop (seulement pour la partie Docker)

## Lancer le projet

### Avec Maven

```bash
mvn clean package
java -jar target/pipeline-demo-1.0-SNAPSHOT.jar
```

`mvn clean package` compile le code, lance le test JUnit, puis crée le jar dans `target/`.

### Avec Docker

```bash
docker build -t pipeline-demo:latest .
docker run --rm pipeline-demo:latest
```

Ou avec docker compose :

```bash
docker compose up --build
docker compose down
```

Le conteneur s'arrête tout de suite après avoir affiché le message (statut `Exited (0)`). C'est normal : l'application n'est pas un serveur.

Le `Dockerfile` fonctionne en deux étapes : une image `maven:3.9-eclipse-temurin-17` compile et teste le projet, puis une image `eclipse-temurin:17-jre`, plus légère, ne garde que le jar pour l'exécuter.

## Organisation Git

- `main` : la version stable, c'est celle que Jenkins surveille.
- `dev` : la branche de travail. Les modifications y sont faites par petits commits, puis intégrées dans `main` avec une Pull Request.

## GitHub Actions

Le workflow `.github/workflows/ci.yml` se déclenche à chaque push sur `main` ou `dev`, et à chaque Pull Request vers `main`. Il fait :

1. récupérer le code ;
2. installer Java 17 ;
3. compiler et tester avec `mvn -B clean package` ;
4. exécuter le jar (le message apparaît dans les logs) ;
5. construire l'image Docker.

Les résultats sont visibles dans l'onglet **Actions** du dépôt.

## Jenkins

Le projet Jenkins s'appelle `PipeLine-DerrazNour`. Il lit le `Jenkinsfile` du dépôt (Pipeline script from SCM, branche `*/main`) et vérifie GitHub toutes les 2 minutes (Poll SCM).

Les étapes du pipeline :

| Étape | Ce qu'elle fait |
| --- | --- |
| Checkout | récupère le code depuis GitHub |
| Build | `mvn clean compile` |
| Test | `mvn test`, avec le rapport JUnit publié dans Jenkins |
| Run | crée le jar et l'exécute pour afficher le message |
| Archive | archive le jar généré |
| Docker Build | construit l'image `pipeline-demo` (tags : numéro de build et `latest`) |
| Deploy | copie le jar dans `deploy/` et lance `docker compose` |
| Notify Slack | envoie un message SUCCES dans Slack |

Si une étape échoue, les suivantes ne sont pas exécutées. En particulier, si les tests échouent, rien n'est déployé et seul un message ECHEC est envoyé sur Slack.

### Configuration Jenkins nécessaire

- Outil Maven nommé exactement `Maven3` (Manage Jenkins, Tools), avec installation automatique.
- Un credential de type Secret text, avec l'ID `slack-webhook`, qui contient l'URL du webhook Slack.
- Docker accessible depuis la machine Jenkins.

Il existe aussi une vue Jenkins nommée `PipeLine` (filtre par expression régulière `.*PipeLine.*`) qui ne montre que ce projet.

## Slack

Les notifications passent par un Incoming Webhook (application `Jenkins-CI`, canal `#tous-jenkins-ci`). L'URL du webhook est stockée uniquement dans Jenkins, jamais dans le code ni dans GitHub.

## Problèmes rencontrés

- **Jenkins cherche `master`** : mettre `*/main` dans le Branch Specifier.
- **`mvn: command not found`** : l'outil Maven doit s'appeler exactement `Maven3`.
- **`no main manifest attribute`** : vérifier que `mainClass` vaut `com.devops.App` dans le `pom.xml` et que `App.java` déclare `package com.devops;`.
- **Docker injoignable** : lancer Docker Desktop et attendre que le moteur soit démarré avant de lancer `docker build`.
