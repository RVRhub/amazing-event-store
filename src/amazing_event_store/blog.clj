(ns amazing-event-store.blog
  (:require
    [clojure.data.json :as json]
    [clojure.walk :as walk]
    [amazing-event-store.route :as route]))

(defonce BLOG (atom {}))
(defonce ID (atom 0))

(defn get-blog-entries []
  (vals @BLOG))

(defn add-blog-entry [entry]
  (let [id (swap! ID inc)]
    (get (swap! BLOG assoc id (assoc entry :id id)) id)))

(defn get-blog-entry [id]
  (get @BLOG id))

(defn update-blog-entry [id entry]
  (when (get-blog-entry id)
    (get (swap! BLOG assoc id entry) id)))

(defn alter-blog-entry [id entry-values]
  (when (get-blog-entry id)
    (get (swap! BLOG update-in [id] merge entry-values) id)))

(defn delete-blog-entry [id]
  (when (get-blog-entry id)
    (swap! BLOG dissoc id)
    {:id id}))

(defn json-response [data]
  (when data
    {:body    (json/write-str data)
     :headers {"Content-type" "application/json"}}))

(defn json-body [request]
  (walk/keywordize-keys
    (json/read-str (slurp (:body request)))))

(defn json-error-handler [handler]
  (fn [request]
    (try
      (handler request)
      (catch Throwable throwable
        (assoc (json-response {:message (.getMessage throwable)
                               :stacktrace (map str (.getStackTrace throwable))})
          :status 500)))))

(defn get-id [request]
  (Long/parseLong (-> request :route-params :id)))

(defn get-handler [request]
  (json-response
    (get-blog-entries)))

(defn post-handler [request]
  (json-response
    (add-blog-entry (json-body request))))

(defn get-entry-handler [request]
  (json-response
    (get-blog-entry (get-id request))))

(defn put-handler [request]
  (json-response
    (update-blog-entry (get-id request) (json-body request))))

(defn delete-handler [request]
  (json-response
    (delete-blog-entry (get-id request))))

(def blog-handler
  (->
    (route/routing
     (route/with-route-matches :get "/entries" get-handler)
     (route/with-route-matches :post "/entries" post-handler)
     (route/with-route-matches :get "/entries/:id" get-entry-handler)
     (route/with-route-matches :put "/entries/:id" put-handler)
     (route/with-route-matches :delete "/entries" delete-handler))
    json-error-handler
    )
  )