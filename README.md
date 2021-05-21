# IDATT2105-project - Rombestilling

**Selve applikasjonen kan du finne her: https://rombestilling.vercel.app/**


Prosjektet er et bookingsystem for rom. Her kan brukere reservere ulike rom og underdeler av rom. Reservasjon kan skje som enkeltperson eller på vegne av en gruppe. Rom og brukere opprettes og administreres av administratorer som har full tilgang til hele systemet. Administratorer kan batch-opprette mange bruker samtidig ved hjelp av CSV-filer, hvorpå de nye brukerne vil motta en epost med link til side der passord kan settes. Swagger-docs finnes på `/swagger-ui/`

## Applikasjonen
Prosjektet består av en backend del og en frontend del hvorav begge delene er utviklet med moderne og stabile rammeverk. På frontend benytter vi oss av React, som er blant det mest brukte rammeverket idag. Vi brukte Typescript for å skrive frontend da det sørger for statisk typing, noe som betyr letter feilsøking og mindre feil. På backend har vi benyttet oss Spring Boot som rammeverk for å bygge REST-apiet vårt. Dette har vi gjort sammen med å bruke Kotlin og Gradle. Kotlin er et moderne språk som lar deg skrive både funksjonell og objekt orientert kode. Vi har benyttet oss av både CI og CD under prosjektet for å sørge for den beste kontinuerlige utviklingen. Vi har benyttet oss av Azure for å deploye backend hver gang vi oppdaterer main branchen vår, noe som har gjort at vi til enhver tid kan teste den nyeste funksjonaliteten vår, live. For å deploye frontend bruker vi Vercel, slik at den nyeste backenden alltid er i samspill med den nyeste frontenden. 

Applikasjonen har to viktige aktører:

1. Endebruker
2. Adminbruker

En _endebruker_ har begrenset tilgang og kan 
- Logge inn
- Tilbakestille passord
- Endre på profil
- Liste tilgjengelige rom
- Reservere rom (på vegne av seg selv eller en gruppe)

En _adminbruker_ har tilgang til alle aspekter en endebruker kan, i tillegg til følgende: 
- Lage endebrukere (evt ved csv-fil)
- Gjøre endebrukere til adminbrukere
- Administrere brukere 
- Administrere rom 
- Administrere grupper
- Administrere gruppemedlemsskap 
- Se statistikk for rom



## Databaseskjema
Følgende er vårt databaseskjema:

![dbskjema](https://user-images.githubusercontent.com/35424810/119131275-87d94f00-ba39-11eb-8f96-1b6690b2237d.png)

## Teknologier 
- **React** - Javascript-rammeverk for å bygge brukergrensesnitt
- **Spring Boot** - Server-side rammeverk
- **Docker** - Rammeverk for konteinerisering 
- **MySQL** - Database (DBMS)
- **H2 Database Engine** - In-memory database til automatisert testing
- **Swagger** - API dokumentasjon
- **Material UI** - UI tema/design
- **JWT** - Autentiseringsmekanisme for REST APIer
- **QueryDSL** - Spørrespråk for å konstruere SQL-spørringer 
- **opencsv** - CSV-analyserer for Java
- **awaitility** - Java DSL for å synkronisere asynkrone operasjoner  

## Sikkerhet
Sikkerhet vært et gjennomgående fokus punkt under hele prosjektet da vi mener at dette er at vi de viktigste fokusene man må ivareta som utvikler. APIet vårt er besignet runt REST prinsipper, noe som gjør at vi derfor benytter oss av stateless authentication. Dette gjør vi ved hjelp av Json Web Tokens. En gyldig innlogget bruker vil til enhver tid ha en Acess token og en refresh token tilgjengelig. Acess token gjør at brukeren kan få tilgang på innhold, men denne har en gyldighet på 15 minutter, av sikkerhetsmessig årsaker. Bruker vil da automatisk benytte seg av refresh token for å få en ny og oppdatert acess token. Alt dette skjer automatisk, slik at bruker ikke skal trenge å bruke energi på det, samtidig som det gir brukeren en sikker opplevelse. 

Vi har også implementert en enkel form for Refresh Token Rotation (RTR): Et refresh token kan bare brukes én gang og hvis en prøver å gjenbruke en refresh token vil alle resresh tokens utgitt til brukeren etter denne bli ugyldige. 

Vi har også hatt fokus på Zero Trust konseptet, og har derfor kun åpnet et fåtall endepunkter, mens resten krever autentisjon. Alle endepunkter krever authentisering og adminrolle. Vi har som nevnt åpnet et fåtall endepunkter til brukere med brukerrollen, slik at man må være registrert som en gyldig bruker med denne rollen. 

I tilfellet en breach skulle skje så har vi lagret alle passordene med Bcrypt algoritmen kombinert med salt. Vi har også utivklet et eget autorisasjons system, for å validere et brukere har rettighetene de trenger for å kunne utføre en handling. Dette er for å forhindre at brukere kan endre eller slette andre brukeres innhold.

## API-dokumentasjon
Dette prosjektet bruker Swagger 3 til API-dokumentasjon. Denne finnes på http://localhost:8080/swagger-ui (https://rombestilling.azurewebsites.net/swagger-ui/)

For å autentisere kan "Authentication Services"-specet brukes:

![image](https://user-images.githubusercontent.com/35424810/119133813-9ffe9d80-ba3c-11eb-9a3d-0ba3d3b16d9a.png)


Her kan man prøve ut forespørselen og legge inn epost og passord:

![image](https://user-images.githubusercontent.com/35424810/119133915-c4f31080-ba3c-11eb-9f03-dbadf60cdb6f.png)


Da vil man få tilbake et access token under feltet "token" som man bruker til å logge inn:

![image](https://user-images.githubusercontent.com/35424810/119133994-e05e1b80-ba3c-11eb-997a-f868b99074bc.png)


Legg så tokenet inn som authentication, husk å inkludere prefikset "Bearer":

![image](https://user-images.githubusercontent.com/35424810/119134121-0683bb80-ba3d-11eb-86ee-0139c75c0037.png)


## Installasjon

### Krav for å kjøre applikasjonen

**Backend**
- [Docker](https://docs.docker.com/get-docker/)
- [Make](https://www.gnu.org/software/make/) (valgfritt)

**Frontend**
- [Node](https://nodejs.org/en/download/):


### Klon repoet fra Git
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

Når applikasjonene har startet opp vil applikasjonen være tilgjengelig på http://localhost:3000/ og REST API-et vil da være tilgjengelig på http://localhost:8080/ 


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
