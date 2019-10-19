(ns amazing-event-store.course-web
  (:require [amazing-event-store.handlers :as handlers]))

(defn not-found-middleware [handler]
      (fn [request]
        (or (handler request)
            {:status 404 :body (str "404 Not Found (with middleware!):" (:uri request))}))
      )
(defn simple-log-middleware [handler]
  (fn [{:keys [uri] :as request}]
    (println "Request path: " uri)
    (handler request)))

(defn example-handler [request]
  {:headers {"Location" "http://github.com/RVRhub"
             "Set-cookie" "test-1"}
   :status 301}
  )

(defn on-init []
  (println "Initializing sample webapp!"))

(defn on-destroy []
  (println "Destroying sample webapp!"))

(defn foo
  "I don't do a whole lot."
  [x]
  (println x "Hello, World!"))

(defn test1-handler [request]
  (throw (RuntimeException. "error!"))
  {:body "test1"})

(defn test2-handler [request]
  {:status 301 :headers {"Location" "http://github.com/RVRhub"}})

(defn router-handler [request]
  (condp = (:uri request)
    "/test1" (test1-handler request)
    "/test2" (test2-handler request)
    "/test3" (handlers/handler3 request)
    nil))

(defn wrapping-handler [request]
  (try
    (if-let [resp (router-handler request)]
      resp
      {:status 404 :body (str "Not found: " (:uri request))})
    (catch Throwable e
      {:status 500 :body (apply str (interpose "\n" (.getStackTrace e)))})
    )
  )

(def full-handler
  (not-found-middleware (simple-log-middleware router-handler)))