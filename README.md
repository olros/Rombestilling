# IDATT2105-project - Rombestilling

Prosjektet er et bookingsystem for rom. Her kan brukere reservere ulike rom og underdeler av rom. Reservasjon kan skje som enkeltperson eller på vegne av en gruppe. Rom og brukere opprettes og administreres av administratorer som har full tilgang til hele systemet. Administratorer kan batch-opprette mange bruker samtidig ved hjelp av CSV-filer, hvorpå de nye brukerne vil motta en epost med link til side der passord kan settes. Swagger-docs finnes på `/swagger-ui/`

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
./gradlew jib --image=rombestilling.azurecr.io/rombestilling:latest
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
gradlew jib --image=rombestilling.azurecr.io/rombestilling:latest
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
