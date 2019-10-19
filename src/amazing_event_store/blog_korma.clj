(ns amazing-event-store.blog-korma
  (:require
    [clojure.data.json :as json]
    [clojure.walk :as walk]
    [amazing-event-store.db :as db]
    [korma.core :as korma]
    [amazing-event-store.route :as route]))

(defn get-blog-entries []
  (korma/select db/entries))

(defn add-blog-entry [entry]
  (korma/insert db/entries
                (korma/values
                  (select-keys entry [:title :body]))))

(defn get-blog-entry [id]
  (first (korma/select db/entries
                       (korma/where {:id id}))))

(defn update-blog-entry [id entry]
  (korma/update db/entries
                (korma/set-fields (select-keys entry [:title :body]))
                (korma/where {:id id}))
  (get-blog-entry id))

(defn alter-blog-entry [id entry-values]
  (update-blog-entry id entry-values))

(defn delete-blog-entry [id]
  (when (get-blog-entry id)
    (korma/delete db/entries
                  (korma/where {:id id}))
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