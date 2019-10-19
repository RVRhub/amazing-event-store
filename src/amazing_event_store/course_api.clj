(ns amazing-event-store.course-api
  (:require [amazing-event-store.handlers :as handlers]
            [amazing-event-store.blog-korma :as blog]
            [amazing-event-store.route :as route]))

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

(defn test1-handler [request]
  {:body (str "test1, route args: " (:route-params request))})

(defn test2-handler [request]
  {:status 301 :headers {"Location" "http://github.com/RVRhub"}})

(def router-handler
  (route/routing
    blog/blog-handler
    (route/with-route-matches :get "/test1" test1-handler)
    (route/with-route-matches :get "/test1/:id" test1-handler)
    (route/with-route-matches :get "/test2" test2-handler)
    (route/with-route-matches :get "/test3" handlers/handler3)))


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