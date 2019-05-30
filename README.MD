# Projet Scala

>Your project is to imagine and drone/mobile IOT devices and create it's big data architecture.
   write your code in scala send by email the git with the commit hash before the 9th of July.You will have to think about company or ngo creating/selling/promoting an IOT devices.
   Example : a company selling autonomous transport bus to town hallsAll your devices will be sending regulary some status report as data (you'll chose the format and che communication means).
   You will have to build a big data solution with two sides :
   A) Monitoring
   Almost in real times analyse the status of every devices and alert an user of a critical situation.
   Example a bus is full of passanger or out of fuel, thus it's anable to provides a good services to passangers waiting at the next bus stop.B) Storing and analytics
   Store  all the data send by every devices and be able to answer questions like :
   In proportion to the whole number of devices is there more failing devices in the north hemisphere or the south hemisphere?
   Is there more failing devices when the weather is hot or when the weather is called?
   Among the failing devices which percentage fails beceause of low battery or empty fuel tank?I).  
>   *Mandatory (15 points)*
>   - Imagine the typical message of your device it must include latitude, longitude, temperature, and fuel/battery remaining.
>   - write the programme that will read all the files containing message in the folder given buy the user. Every line of each file will contain one message. File format could be csv or text file containing json line.
>   - than send the message to a server using the protocol of your choice rest, mqtt…
>   - display the message on a web ui2) 
>>*Optional*
>>   - store the data in a database (5)
>>   - write your front-end using scala-js (5)
> >  - deploy in the cloud (5)

Le but de ce projet est de réaliser de la maintenance de structure à l'aide de prise de photo et/ou vidéo par des drones.


## libraries:  
- Play Json
    - User friendly
- Akka-http
    - Really simple web server for the REST api
- Slick
    - ORM
    - Generation de requetes depuis le code Scala
- Scala CSV [(com.github.tototoshi)](https://github.com/tototoshi/scala-csv)
    - Parsing csv is not [fun](https://sebsauvage.net/wiki/doku.php?id=csv&fbclid=IwAR2PBtYwuMd9BZO35JTju_qFczLFkD6eOsFQ4kcY0YgsPaOXhJninyEkGqga)


## Objectifs

### Semaine 1
- [X] JSON Parsing
- [X] CSV Parsing
- [X] Read whole directory & sub directories

### Semaine 2
- [X] Drone producer
- [ ] Drone consumer
- [ ] Search on saved information

## Authors
- Beuvry Jérémy
- Fleury Sébastien
- Pochon Mathieu

### Compilation

Pour generer le fichier necessaire pour l'affichage des données via twirl,
il faut auparavant faire un `compile` dans sbt (Dans Intellij Idea -> sbt shell)

### Utilisation

Une fois lancé, le serveur accepte :  
  
```POST /drones```  
Le body doit contenir les informations d'un drone au format json comme par exemple:  
```{"id":1,"latitude":1.1,"longitude":4,"temperature":9.7,"battery":5,"defect":1,"time":5984984}```
 
```DELETE /drones```  
Vide la DB du site

Le site est accessible [127.0.0.1:8080](127.0.0.1:8080)

Afin de lancer kafka, il faut lancer le script
```kafka_launch.sh```
Il faut adapter certaines variables afin que le script fonctionne correctement.