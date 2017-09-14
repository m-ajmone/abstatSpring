# Abstat

## Installing

Installare e far partire MongoDB

Il progetto lo faccio partire da Spring Tool Suite (Run as SpringBoot App) poichè ha tomcat integrato, da eclipse immagino si debba
prima mettere sul server e poi Run As > Run on Server.

In data/... ci sono l'ontologia dbpedia_2014, il dataset system-test (intero e splittato) e qualche summary di prova.

Se fate partire il progetto sulla vostra macchina non ci saranno i metadati corrispondenti nel database (quindi in /home non comparirà nessun dataset/ontologia).
Per rimediare basta rifare l'upload di dataset e ontologia dall'interfaccia in [localhost:8080/home] così da creare i metadati nel database.

## Links

* Home: [localhost:8080/home]
* CRUD dataset : [localhost:8080/dataset/list]
* CRUD ontology : [localhost:8080/ontology/list]

## Commenti

Manca ancora la parte front-end, attualmente non è molto curata.


