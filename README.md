# Projet DevOps - CI/CD Pipeline

> Projet d'intégration continue et de déploiement continu (CI/CD) avec Jenkins, GitHub Actions et Slack

##  Table des matières

- [À propos](#à-propos)
- [Architecture](#architecture)
- [Prérequis](#prérequis)
- [Installation](#installation)
- [Configuration](#configuration)
- [Utilisation](#utilisation)
- [Pipeline CI/CD](#pipeline-cicd)
- [Tests](#tests)
- [Déploiement](#déploiement)
- [Notifications](#notifications)
- [Contribuer](#contribuer)
- [Auteur](#auteur)

---

##  À propos

Ce projet démontre l'implémentation complète d'une chaîne CI/CD moderne utilisant :

- **GitHub Actions** pour l'intégration continue automatique
- **Jenkins** pour le pipeline de build, test et déploiement
- **Maven** pour la gestion des dépendances et du build
- **Slack** pour les notifications en temps réel
- **Java 17** comme langage de développement

###  Objectifs du projet

- ✅ Automatiser le processus de build et de test
- ✅ Mettre en place un workflow Git professionnel (branches, PR, merge)
- ✅ Configurer un pipeline Jenkins multi-stages
- ✅ Intégrer des notifications Slack pour le monitoring
- ✅ Archiver et déployer les artifacts automatiquement

---

##  Architecture

```
┌─────────────────────────────────────────────────────────┐
│                  GitHub Repository                       │
│           Projet-DevOps--DerrazNour-                    │
└────────────────────┬────────────────────────────────────┘
                     │
          ┌──────────┴──────────┐
          │                     │
          ▼                     ▼
┌──────────────────┐  ┌──────────────────────────────────┐
│ GitHub Actions   │  │     Jenkins Pipeline             │
│   - Checkout     │  │   1. Checkout (Git)              │
│   - Setup Java   │  │   2. Build & Test (Maven)        │
│   - Build        │  │   3. Archive (Artifacts)         │
└──────────────────┘  │   4. Deploy                      │
                      │   5. Slack Notification          │
                      └────────────┬─────────────────────┘
                                   │
                                   ▼
                      ┌──────────────────────────────────┐
                      │   Slack Workspace: Jenkins-CI    │
                      │   Canal: #tous-jenkins-ci        │
                      │   - Notifications SUCCESS/FAIL   │
                      └──────────────────────────────────┘
```

---

##  Prérequis

Avant de commencer, assurez-vous d'avoir installé :

- **Java JDK 17** ou supérieur
  ```bash
  java -version
  ```

- **Maven 3.9+**
  ```bash
  mvn -version
  ```

- **Git**
  ```bash
  git --version
  ```

- **Jenkins 2.528.3+** (pour le pipeline CI/CD)

- **Compte GitHub** avec accès au repository

- **Workspace Slack** (optionnel, pour les notifications)

---

##        Installation

### 1. Cloner le repository

```bash
git clone https://github.com/Nopojj/Projet-DevOps--DerrazNour-.git
cd Projet-DevOps--DerrazNour-
```

### 2. Vérifier la structure du projet

```bash
tree -L 3
```

Vous devriez voir :
```
Projet-DevOps--DerrazNour-/
├── .github/
│   └── workflows/
│       └── ci.yml
├── src/
│   └── main/
│       └── java/
│           └── App.java
├── Jenkinsfile
├── pom.xml
└── README.md
```

### 3. Compiler le projet localement

```bash
mvn clean compile
```

### 4. Exécuter l'application

```bash
mvn exec:java -Dexec.mainClass="com.devops.App"
```

**Sortie attendue :**
```
CI/CD avec GitHub Actions
```

---

## ⚙️ Configuration

### Configuration Jenkins

#### 1. Créer un nouveau pipeline

1. Ouvrir Jenkins → **Nouveau Item**
2. Nom : `PipeLine-DerrazNour`
3. Type : **Pipeline**
4. Cliquer sur **OK**

#### 2. Configurer le SCM

Dans la configuration du pipeline :

```
Pipeline script from SCM
  SCM: Git
  Repository URL: https://github.com/Nopojj/Projet-DevOps--DerrazNour-.git
  Credentials: Ajouter vos credentials GitHub
  Branch Specifier: */main
  Script Path: Jenkinsfile
```

#### 3. Configurer Maven

1. **Manage Jenkins** → **Global Tool Configuration**
2. Section **Maven**
   - Nom : `Maven3`
   - ✅ Install automatically
   - Version : `3.9.12`

#### 4. Ajouter les credentials

**Pour GitHub :**
```
Manage Jenkins → Credentials → System → Global credentials
  Kind: Username with password
  Username: votre_username_github
  Password: votre_token_github
  ID: github-credentials
```

**Pour Slack :**
```
Manage Jenkins → Credentials → System → Global credentials
  Kind: Secret text
  Secret: votre_webhook_url_slack
  ID: slack-webhook
```

### Configuration GitHub Actions

Le workflow est déjà configuré dans `.github/workflows/ci.yml`.

Pour personnaliser :

```yaml
name: CI DevOps

on:
  push:
    branches: [ main, dev ]  # Modifier les branches si nécessaire
  pull_request:
    branches: [ main, dev ]

jobs:
  construire:
    runs-on: ubuntu-latest
    
    steps:
      - uses: actions/checkout@v4
      
      - name: Set up Java
        uses: actions/setup-java@v4
        with:
          java-version: '17'
          distribution: 'temurin'
      
      - name: Build with Maven
        run: mvn clean compile
```

### Configuration Slack

#### 1. Créer une application Slack

1. Aller sur https://api.slack.com/apps
2. Cliquer sur **Create New App**
3. Choisir **From scratch**
4. Nom : `Jenkins-CI-app`
5. Workspace : Sélectionner votre workspace

#### 2. Activer les Incoming Webhooks

1. Dans votre app → **Incoming Webhooks**
2. Activer **Activate Incoming Webhooks**
3. Cliquer sur **Add New Webhook to Workspace**
4. Sélectionner le canal : `#tous-jenkins-ci`
5. Copier l'URL du webhook

#### 3. Tester le webhook

```bash
curl -X POST -H 'Content-type: application/json' \
  --data '{"text":"🧪 Test de connexion Jenkins-CI"}' \
  VOTRE_WEBHOOK_URL
```

---

## 🚀 Utilisation

### Workflow de développement

#### 1. Créer une branche de fonctionnalité

```bash
git checkout -b feature/ma-nouvelle-fonctionnalite
```

#### 2. Faire vos modifications

```bash
# Éditer les fichiers
nano src/main/java/App.java

# Vérifier le statut
git status
```

#### 3. Commiter les changements

```bash
git add .
git commit -m "feat: ajout de la nouvelle fonctionnalité"
```

#### 4. Pousser vers GitHub

```bash
git push -u origin feature/ma-nouvelle-fonctionnalite
```

#### 5. Créer une Pull Request

1. Aller sur GitHub
2. Cliquer sur **Compare & pull request**
3. Remplir la description
4. Assigner des reviewers
5. Attendre les checks CI
6. Merger une fois approuvé

### Commandes Maven utiles

```bash
# Nettoyer le projet
mvn clean

# Compiler
mvn compile

# Exécuter les tests
mvn test

# Créer le package JAR
mvn package

# Installer dans le repository local
mvn install

# Tout nettoyer et recompiler
mvn clean install
```

---

## Pipeline CI/CD

### Stages du pipeline Jenkins

Le pipeline est défini dans le `Jenkinsfile` et comprend 4 stages principaux :

#### **Stage 1 : Checkout**
```groovy
stage('Checkout') {
    steps {
        checkout scm
    }
}
```
- Récupère le code source depuis GitHub
- Utilise les credentials configurés

#### **Stage 2 : Build & Test**
```groovy
stage('Build & Test') {
    steps {
        sh 'mvn clean test'
    }
}
```
- Nettoie le répertoire `target/`
- Compile le code Java
- Exécute les tests unitaires

#### **Stage 3 : Archive**
```groovy
stage('Archive') {
    steps {
        archiveArtifacts artifacts: 'target/*.jar', 
                         fingerprint: true
    }
}
```
- Archive les artifacts JAR générés
- Crée une empreinte digitale pour traçabilité

#### **Stage 4 : Deploy**
```groovy
stage('Deploy') {
    steps {
        sh '''
            mkdir -p deploy
            cp target/pipeline-demo-1.0-SNAPSHOT.jar deploy/
        '''
    }
}
```
- Crée un répertoire de déploiement
- Copie l'artifact dans le répertoire `deploy/`

### Post-actions

```groovy
post {
    success {
        slackSend(
            channel: '#tous-jenkins-ci',
            color: 'good',
            message: "✅ Pipeline Jenkins SUCCÈS : Build, Tests & Deploy OK"
        )
    }
    failure {
        slackSend(
            channel: '#tous-jenkins-ci',
            color: 'danger',
            message: "❌ Pipeline Jenkins ÉCHEC : Vérifier les logs"
        )
    }
}
```

### Diagramme de flux

```
┌──────────┐
│  START   │
└────┬─────┘
     │
     ▼
┌──────────────────┐
│   Checkout       │  ← Récupération du code
└────┬─────────────┘
     │
     ▼
┌──────────────────┐
│ Build & Test     │  ← mvn clean test
└────┬─────────────┘
     │
     ├─── SUCCESS ───┐
     │               │
     ▼               │
┌──────────────────┐│
│   Archive        ││  ← Archivage JAR
└────┬─────────────┘│
     │               │
     ▼               │
┌──────────────────┐│
│   Deploy         ││  ← Copie vers deploy/
└────┬─────────────┘│
     │               │
     ▼               ▼
┌──────────────────────────┐
│  Slack Notification      │  ← ✅ SUCCESS ou ❌ FAILURE
└──────────┬───────────────┘
           │
           ▼
       ┌───────┐
       │  END  │
       └───────┘
```

---

##  Tests

### Structure des tests

```
src/
├── main/
│   └── java/
│       └── App.java
└── test/
    └── java/
        └── AppTest.java  (à créer)
```

### Exemple de test unitaire

Créer `src/test/java/AppTest.java` :

```java
package com.devops;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AppTest {
    
    @Test
    public void testAppExists() {
        App app = new App();
        assertNotNull(app);
    }
    
    @Test
    public void testMainMethod() {
        // Test que la méthode main s'exécute sans erreur
        assertDoesNotThrow(() -> {
            App.main(new String[]{});
        });
    }
}
```

### Ajouter JUnit au pom.xml

```xml
<dependencies>
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter</artifactId>
        <version>5.10.1</version>
        <scope>test</scope>
    </dependency>
</dependencies>
```

### Exécuter les tests

```bash
# Exécuter tous les tests
mvn test

# Exécuter un test spécifique
mvn test -Dtest=AppTest

# Exécuter avec rapport détaillé
mvn test -X
```

---

##  Déploiement

### Déploiement local

L'artifact est automatiquement déployé dans le répertoire `deploy/` :

```bash
# Vérifier l'artifact déployé
ls -lh deploy/

# Exécuter l'artifact
java -jar deploy/pipeline-demo-1.0-SNAPSHOT.jar
```

### Déploiement manuel

```bash
# Build du projet
mvn clean package

# Copier l'artifact
mkdir -p deploy
cp target/pipeline-demo-1.0-SNAPSHOT.jar deploy/

# Exécuter
java -jar deploy/pipeline-demo-1.0-SNAPSHOT.jar
```

### Déploiement sur un serveur

```bash
# Via SCP
scp target/pipeline-demo-1.0-SNAPSHOT.jar user@server:/path/to/deploy/

# Via rsync
rsync -avz target/pipeline-demo-1.0-SNAPSHOT.jar user@server:/path/to/deploy/
```

---

##  Notifications

### Types de notifications Slack

Le projet envoie deux types de notifications :

####  Notification de succès
```
✅ Pipeline Jenkins SUCCÈS : Build, Tests & Deploy OK

Project: PipeLine-DerrazNour
Build: #22
Duration: 14s
Status: SUCCESS
```

####  Notification d'échec
```
❌ Pipeline Jenkins ÉCHEC : Vérifier les logs

Project: PipeLine-DerrazNour
Build: #20
Duration: 5s
Status: FAILURE
Reason: Compilation error
```

### Personnaliser les notifications

Modifier le `Jenkinsfile` :

```groovy
post {
    success {
        slackSend(
            channel: '#tous-jenkins-ci',
            color: 'good',
            message: """
                ✅ *Build Réussi*
                Project: ${env.JOB_NAME}
                Build: ${env.BUILD_NUMBER}
                Duration: ${currentBuild.durationString}
                <${env.BUILD_URL}|Voir les logs>
            """
        )
    }
}
```

---

##  Contribuer

Les contributions sont les bienvenues ! Voici comment contribuer :

### 1. Fork le projet

Cliquez sur le bouton **Fork** en haut à droite

### 2. Créer une branche

```bash
git checkout -b feature/AmazingFeature
```

### 3. Commiter vos changements

```bash
git commit -m 'feat: Add some AmazingFeature'
```

### 4. Pousser vers la branche

```bash
git push origin feature/AmazingFeature
```

### 5. Ouvrir une Pull Request

Allez sur GitHub et créez une PR depuis votre branche

### Conventions de commit

Utilisez les préfixes suivants :

- `feat:` Nouvelle fonctionnalité
- `fix:` Correction de bug
- `docs:` Documentation
- `style:` Formatage
- `refactor:` Refactoring
- `test:` Tests
- `chore:` Maintenance

---

## Statistiques du projet

### Builds

- **Total builds :** 22
- **Taux de succès :** 86% (19/22)
- **Durée moyenne :** 7 secondes
- **Dernier build :** ✅ SUCCESS

### Technologies

- **Langage :** Java 17
- **Build tool :** Maven 3.9.12
- **CI/CD :** Jenkins 2.528.3 + GitHub Actions
- **Notifications :** Slack
- **Version control :** Git

---


## 👤 Auteur

**Derraz Nour**

- GitHub: [@Nopojj](https://github.com/Nopojj)
- Repository: [Projet-DevOps--DerrazNour-](https://github.com/Nopojj/Projet-DevOps--DerrazNour-)

---

## outils

- Jenkins pour l'excellent outil CI/CD
- GitHub pour l'hébergement et GitHub Actions
- Slack pour l'API de notifications
- La communauté DevOps pour les best practices

---

##  Ressources utiles

- [Documentation Jenkins](https://www.jenkins.io/doc/)
- [GitHub Actions Documentation](https://docs.github.com/en/actions)
- [Maven Documentation](https://maven.apache.org/guides/)
- [Slack API Documentation](https://api.slack.com/)
- [Git Documentation](https://git-scm.com/doc)

---

##  Problèmes connus

### Problème : Branch master inexistante
**Solution :** Utiliser `*/main` dans la configuration Jenkins

### Problème : Git path invalide
**Solution :** Laisser Jenkins utiliser l'installation Git par défaut

### Problème : Tests non trouvés
**Solution :** Normal si aucun test n'est configuré. Le build continue.

---



---

*Dernière mise à jour : 9 janvier 2026*# Projet-DevOps--DerrazNour-
