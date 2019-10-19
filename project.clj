(defproject amazing-event-store "0.1.0-SNAPSHOT"
  :description "Amazing ES"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :plugins [[lein-ring "0.12.5"]]
  :ring {:handler amazing-event-store.course-api/full-handler
         :init amazing-event-store.course-api/on-init
         :port 4001
         :destroy amazing-event-store.course-api/on-destroy}
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [ring "1.7.1"]
                 [org.clojure/data.json "0.2.6"]
                 [org.postgresql/postgresql "42.2.8"]
                 [org.clojure/java.jdbc "0.6.1"]
                 [korma/korma "0.4.3"]]
  :main ^:skip-aot amazing-event-store.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
