(ns amazing-event-store.core
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))

(defn DataTypes []
  (def a 1)
  (def b 'test)
  (def c "Text")

  (println a)
  (println b)
  (println c)
  )

(DataTypes)