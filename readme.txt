Program developed in parity with the course "Microservices and Distributed Systems" by Amigoscode

There are some extra features, including:

-Sendgrid send email configuration

You can opt to save the notifications in a database or to send with SendGrid in the notification microservice. To do so,
simply change sender property in the corresponding notification application.yml to the spring profile you intend to use.
The value should be "db" to save in database or "sendgrid" to send an email with sendgrid. For profiles default, kafka
and docker, you should include in your computer's environment the SENDGRID_API_KEY (your SendGrid API key) and the
MY_EMAIL_GMAIL (the email you're using to send with SendGrid) variables.

-Easy swap between:

   -Running locally with rabbitmq as the message queue;
   -Running locally with kafka as the message queue;
   -Running inside a docker;
   -running with k8s (using minikube).

*To run locally with rabbitmq, just run the programs with default as the active spring profile. Run Zipkin, rabbitmq and
the databases from a docker.

*To run locally with kafka, start kafka at your machine, change the active spring profile for notification and customer
microservices to "kafka", run the other microservices with default profile. Run Zipkin and the databases from a docker.

*To run all from a docker simple do "docker compose up" from a terminal in the root directory.

*To run with k8s simply start your minikube with "minikube start" and do "minikube apply -f {directory}" for each of the
services inside the repository k8s, putting the service folder in place of {directory}. if using SendGrid, don't forget
to include the SENDGRID_API_KEY (your SendGrid API key) and the MY_EMAIL_GMAIL (the email you're using to send with
SendGrid) variables in k8s/services/notification/deployment.yml

