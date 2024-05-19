## Berberi

Berberi est une plateforme de gestion dédiée aux professionnels de la beauté et du bien-être, inspirée de Fresha. Cette application permet la prise de rendez-vous en ligne, la gestion des paiements, des outils de marketing, et bien plus encore.

## Technologies Utilisées

- **Backend** : Java avec Spring Boot 3.2.5
- **Frontend** : React
- **Application Mobile** : React Native pour Android
- **Base de Données** : PostgreSQL
- **IDE** : IntelliJ IDEA
- **Version Control** : Git

## Prérequis

- JDK 17 ou supérieur
- Node.js et npm
- PostgreSQL
- IntelliJ IDEA

## Installation

### Backend (Spring Boot)

1. Clonez le dépôt :
   \`\`\`bash
   git clone https://github.com/ZOUHIRELA/berberi.git
   \`\`\`
2. Accédez au répertoire du projet :
   \`\`\`bash
   cd berberi/backend
   \`\`\`
3. Configurez votre base de données PostgreSQL dans le fichier application.properties :
   \`\`\`properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/berberi_db
   spring.datasource.username=postgres
   spring.datasource.password=votre-mot-de-passe
   spring.jpa.hibernate.ddl-auto=update
   \`\`\`
4. Installez les dépendances et démarrez l'application :
   \`\`\`bash
   ./mvnw clean install
   ./mvnw spring-boot:run
   \`\`\`

### Frontend (React)

1. Accédez au répertoire du projet frontend :
   \`\`\`bash
   cd ../frontend
   \`\`\`
2. Installez les dépendances :
   \`\`\`bash
   npm install
   \`\`\`
3. Démarrez l'application React :
   \`\`\`bash
   npm start
   \`\`\`

### Application Mobile (React Native)

1. Accédez au répertoire du projet mobile :
   \`\`\`bash
   cd ../mobile
   \`\`\`
2. Installez les dépendances :
   \`\`\`bash
   npm install
   \`\`\`
3. Exécutez l'application sur un émulateur Android :
   \`\`\`bash
   npm run android
   \`\`\`

## Utilisation

### Enregistrement et Connexion

Berberi permet aux utilisateurs de s'enregistrer et de se connecter via les méthodes suivantes :
- Email et mot de passe
- OAuth2 (Google, Facebook, GitHub)

### Fonctionnalités

- Prise de rendez-vous en ligne : Les clients peuvent réserver des services via une interface conviviale.
- Gestion des paiements : Les paiements sont sécurisés et faciles à gérer.
- Outils de marketing : Automatisez les campagnes marketing et augmentez la visibilité.
- Gestion des stocks : Gardez une trace des produits et des fournitures.
- Rapports détaillés : Génère des rapports pour analyser la performance et les tendances.
- Vérification par Email : Lors de l'enregistrement et de la réinitialisation du mot de passe, un code de vérification est envoyé par email, valide pendant 15 minutes.

## Contribuer

1. Fork le projet
2. Créez votre branche de fonctionnalité (\`git checkout -b feature/nouvelle-fonctionnalité\`)
3. Commitez vos changements (\`git commit -m 'Ajouter nouvelle fonctionnalité'\`)
4. Poussez la branche (\`git push origin feature/nouvelle-fonctionnalité\`)
5. Ouvrez une Pull Request

## License

Ce projet est sous licence MIT. Voir le fichier LICENSE pour plus de détails.

## Contact

Pour toute question, veuillez contacter zouhir.mpi.isitd@gmail.com.

Merci d'utiliser Berberi!" > README.md
