(ns amazing-event-store.db
  (:require [korma.db :as korma-db]
            [korma.core :as korma]))

(def postgresql-db
  {:classname "org.postgresql.Driver"
   :subprotocol "postgresql"
   :subname "//localhost:5432/event_store"
   :user "postgres"
   :password "postgres"})

(korma-db/defdb korma-pool postgresql-db)

(korma/defentity entries)