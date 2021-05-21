# IDATT2105-project - Rombestilling

Prosjektet er et bookingsystem for rom. Her kan brukere reservere ulike rom og underdeler av rom. Reservasjon kan skje som enkeltperson eller på vegne av en gruppe. Rom og brukere opprettes og administreres av administratorer som har full tilgang til hele systemet. Administratorer kan batch-opprette mange bruker samtidig ved hjelp av CSV-filer, hvorpå de nye brukerne vil motta en epost med link til side der passord kan settes. Swagger-docs finnes på `/swagger-ui/`

## Applikasjonen
Prosjektet består av en backend del og en frontend del hvorav begge delene er utviklet med moderne og stabile rammeverk. På frontend benytter vi oss av React, som er blant det mest brukte rammeverket idag. Vi brukte Typescript for å skrive frontend da det sørger for statisk typing, noe som betyr letter feilsøking og mindre feil. På backend har vi benyttet oss Spring Boot som rammeverk for å bygge REST-apiet vårt. Dette har vi gjort sammen med å bruke Kotlin og Gradle. Kotlin er et moderne språk som lar deg skrive både funksjonell og objekt orientert kode. Vi har benyttet oss av både CI og CD under prosjektet for å sørge for den beste kontinuerlige utviklingen. Vi har benyttet oss av Azure for å deploye backend hver gang vi oppdaterer main branchen vår, noe som har gjort at vi til enhver tid kan teste den nyeste funksjonaliteten vår, live. For å deploye frontend bruker vi Vercel, slik at den nyeste backenden alltid er i samspill med den nyeste frontenden. 

## Sikkerhet
Sikkerhet vært et gjennomgående fokus punkt under hele prosjektet da vi mener at dette er at vi de viktigste fokusene man må ivareta som utvikler. APIet vårt er besignet runt REST prinsipper, noe som gjør at vi derfor benytter oss av stateless authentication. Dette gjør vi ved hjelp av Json Web Tokens. En gyldig innlogget bruker vil til enhver tid ha en Acess token og en refresh token tilgjengelig. Acess token gjør at brukeren kan få tilgang på innhold, men denne har en gyldighet på 15 minutter, av sikkerhetsmessig årsaker. Bruker vil da automatisk benytte seg av refresh token for å få en ny og oppdatert acess token. Alt dette skjer automatisk, slik at bruker ikke skal trenge å bruke energi på det, samtidig som det gir brukeren en sikker opplevelse. Vi har også hatt fokus på Zero Trust konseptet, og har derfor kun åpnet et fåtall endepunkter, mens resten krever autentisjon. I tilfellet en breach skulle skje så har vi lagret alle passordene med Bcrypt algoritmen kombinert med salt.


## Krav for å kjøre applikasjonen

**Backend**
- [Docker](https://docs.docker.com/get-docker/)
- [Make](https://www.gnu.org/software/make/) (valgfritt)

**Frontend**
- [Node](https://nodejs.org/en/download/):

## Installasjon

```bash
git clone https://github.com/olros/IDATT2105-project.git
```

### Backend

```bash
cd IDATT2105-project/backend/
```

#### Unix/Linux:

```bash
# Med GNU Make:
make run-unix

# Uten GNU Make:
./gradlew jibDockerBuild --image=rombestilling.azurecr.io/rombestilling:latest
docker-compose -f docker-compose.azure.yml up --build

# Utvikling:
make db / docker-compose up
./gradlew bootRun
```

#### Windows:

```bash
# Med GNU Make:
make run-windows

# Uten GNU Make:
gradlew jibDockerBuild --image=rombestilling.azurecr.io/rombestilling:latest
docker-compose -f docker-compose.azure.yml up --build

# Utvikling:
make db / docker-compose up
gradlew bootRun
```

### Frontend

```bash
cd IDATT2105-project/frontend/

# Installer avhengigheter
yarn 

# Kjør i utviklingsmodus
yarn start
```

## Medlemmer

<img src="assets/Hermann.jpg" width="200">

**Hermann Owren Elton -**

<img src="assets/Mads.jpg" width="200">

**Mads Lundegaard -**

<img src="assets/Olaf.jpg" width="200">

**Olaf Rosendahl -**

<img src="assets/Eirik.jpg" width="200">

**Eirik Steira -**

<br/>
