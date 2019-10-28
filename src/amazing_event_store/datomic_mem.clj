(ns amazing-event-store.datomic-mem
  (:import datomic.Util java.util.Random)
  (:require
    [clojure.data.generators :as gen]
    [clojure.java.io :as io]
    [clojure.pprint :as pp]
    [datomic.api :as d]))

;;(def  uri "datomic:free://localhost:4334/hello")
(def uri "datomic:mem://sample-database")

(d/create-database uri)

(def conn (d/connect uri))
(def db (d/db conn))

(def resource io/resource)

(defn read-all
  "Read all forms in f, where f is any resource that can
   be opened by io/reader"
  [f]
  (Util/readAll (io/reader f)))

(defn transact-all
  "Load and run all transactions from f, where f is any
   resource that can be opened by io/reader."
  [conn f]
  (loop [n 0
         [tx & more] (read-all f)]
    (if tx
      (recur (+ n (count (:tx-data  @(d/transact conn tx))))
             more)
      {:datoms n})))

(defn setup-sample-db-1
  [conn]
  (doseq [schema ["es_schema.edn"]]
    (def datoms (->> (io/resource schema)
                     (transact-all conn)))
    (println datoms)
    )
  (let [[[ed]] (seq (d/q '[:find ?e :where [?e :event/aggregateId "aggregateId"]]
                         (d/db conn)
                         ))]
    @(d/transact conn [[:db/add ed :event/type "makeOffer"]])
    @(d/transact conn [[:db/add ed :event/member "roman2"]])
    @(d/transact conn [[:db/add ed :event/place "Cafe 2"]]))

  (let [[[eid]] (seq (d/q '[:find ?e :where [?e :event/aggregateId "aggregateId"]]
                         (d/db conn))
                    )]
    (pp/pprint (entity-history db eid))
    )
  )

(defn entity-history
  "Takes an entity and shows all the transactions that touched this entity.
Pairs well with clojure.pprint/print-table"
  [db eid]
  (->> eid
       (d/q
         '[:find ?e ?attr ?v ?tx ?added ?inst
           :in $ ?e
           :where
           [?e ?a ?v ?tx ?added]
           [?tx :db/txInstant ?inst]
           [?a :db/ident ?attr]]
         (datomic.api/history db))
       (map #(->> %
                  (map vector [:e :a :v :tx :added :inst])
                  (into {})))
       (sort-by :tx)))

(defn datom-table
  "Print a collection of datoms in an org-mode compatible table."
  [db datoms]
  (println datoms)
  (->> datoms
       (map
         (fn [{:keys [e a v tx added]}]
           {"part" (d/part e)
            "e" (format "0x%016x" e)
            "a" (d/ident db a)
            "v" (trunc v 24)
            "tx" (format "0x%x" tx)
            "added" added}))
       (pp/print-table ["part" "e" "a" "v" "tx" "added"])))

(setup-sample-db-1 conn)
