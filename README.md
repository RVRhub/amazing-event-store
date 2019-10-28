# Amazing Event Store

Event Store base on Clojure

## Installation

* brew install clojure

Leiningen: Build automation and dependency management tool 
* brew install leiningen

### Generate new clojure project
* lein new app amazing-event-store
### Run app
* lein run
* lein repl

## Usage
```bash
    $ java -jar amazing-event-store-0.1.0-standalone.jar [args]
```

## Options

## How to run Ring server

* lein ring server-headless


##Datomic 

Connect to REPL via docker container: 

```bash
 docker exec -it 3b15398b76d6 /bin/bash 
 cd /srv/datomic/
 bin/repl'
```
 
Add first code
```clojure
 (require '[datomic.api :as d :refer [q db]])
 
 (def tx-data [{:db/id (d/tempid :db.part/user)
                :user/email "fowler@acm.org"
                :user/name "Martin Fowler"
                :user/roles [:user.roles/author :user.roles/editor]}])
 
 (def tx-result @(d/transact conn tx-data))
 
 (q '[:find ?name
      :where [?e :user/name ?name]]
    (:db-after tx-result))
```
 
### Bugs
### Any Other Sections
### That You Think
### Useful links

* https://docs.datomic.com/cloud/whatis/architecture.html
* https://github.com/clojure-cookbook/clojure-cookbook/tree/master/06_databases
* http://www.learndatalogtoday.org/chapter/1
* https://github.com/Datomic/day-of-datomic/blob/master/src/datomic/samples/repl.clj
* https://github.com/Datomic/day-of-datomic/blob/master/tutorial/crud.clj
* https://github.com/gws/docker-datomic-free-transactor
* https://docs.datomic.com/on-prem/dev-setup.html

## License

Copyright © 2019 RVR
