# IDATT2105-project


Prosjektet er et bookingsystem for grupperom. Her kan brukere reservere ulike rom og deler av rom. Reservasjon kan skje som enkeltperson eller som gruppe. Rom og brukere opprettes og administreres av administratorer.


## Krav for å kjøre applikasjonen
### Backend
- [Docker](https://docs.docker.com/get-docker/)
- [Make](https://www.gnu.org/software/make/) 

### Frontend
- [Yarn](https://classic.yarnpkg.com/en/docs/install/#debian-stable):
```bash
npm install -g yarn
```

## Installasjon

### Backend

```bash
git clone https://gitlab.stud.idi.ntnu.no/team-2-sysutv/idatt2106_2021_2.git

cd idatt2106_2021_2/backend

# Run the app without GNU Make

# With Maven  
#port 3306, 8080, and 8000 needs to be open for this to work
docker-compose up & gradlew jibDockerBuild (./gradlew jibDockerBuild for unix/linux)

# With Docker-compose
docker-compose -f docker-compose.azure.yml build
docker-compose -f docker-compose.azure.yml up

# Run the app with GNU Make

make run-unix og make run-windows

```

### Frontend

```bash
git clone https://gitlab.stud.idi.ntnu.no/team-2-sysutv/idatt2106_2021_2.git

cd idatt2106_2021_2/frontend

# Set url to api in env-file
echo REACT_APP_API_URL=http://localhost:8080/api/  > .env

# Install dependencies
yarn 

# Run the app
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
