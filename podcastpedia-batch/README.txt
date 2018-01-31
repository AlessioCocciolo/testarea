To build:
gradlew bootRepackage -x test

To run:
java -jar podcastpedia-batch-0.1.0.jar addNewPodcastJob --spring.config.location=classpath:/application-dev_home.properties

