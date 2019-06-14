
# Requirements
 * java 11
 * docker (docker-cli for windows)
 * minikube (I dont recoment running docker-for-windows with HyperV )
 * kubectl
 * configure docker-cli to use docker in minikube. See 'minikube docker-env'.

# To build and deploy :

See Readme files in [authentication-service|authentication-service/Readme.md] and [main-service|main-service/Readme.md].

To deploy authentication:
'kubectl apply -f k8s-resources\authentication-deployment.yaml'

To create authentication-service:
'kubectl apply -f k8s-resources\authentication-service.yaml'


To deploy main:
'kubectl apply -f k8s-resources\main-deployment.yaml'

To create main-service:
'kubectl apply -f k8s-resources\main-service.yaml'

To create ingres:
'kubectl apply -f k8s-resources\ingress.yml'

To find out the deployment ip
'minikube ip'

For accesibility and google authorisation domains register minikube.com in windows hosts as  value of 'minikube ip'

