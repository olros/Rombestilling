help:
	@echo 'db                     - start the db and phpmyadmin'
	@echo 'restart                - rebuild and start the db and phpmyadmin'
	@echo 'deploy                 - build dockerimage and deploy it to a Azure Container Registry'
	@echo 'run                    - run backend and db in docker-compose'

db:
	docker-compose up

restart:
	docker-compose build
	docker-compose up

deploy:
	docker login rombestilling.azurecr.io
	gradlew jib --image=rombestilling.azurecr.io/rombestilling:latest

PR:
	git add .

run:
	docker-compose -f docker-compose.azure.yml build
	docker-compose -f docker-compose.azure.yml up