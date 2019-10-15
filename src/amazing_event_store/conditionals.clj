(ns amazing-event-store.conditionals
  (:gen-class))

(defn condIfWithOr
  [firstStmt secondStmt]
  (println "condDoubleIf: ")
  (if (or firstStmt secondStmt)
    (do (println "Success")
        (println "Second line"))
    (do (println "Not Success")
        (println "Second line"))
    ))

(condIfWithOr true false)

(defn condCase
  [type]
  (println "condCase: ")
  (case type
    "string" (println "Hello World")
    "int" (println 100)
    ))
(condCase "int")

(defn condCond
  [value]
  (println "condCond: ")
  (cond
    (<= value 5) (println "Less or EQ 5")
    (<= value 100) (println "Less  or EQ 100 ")
    :else (println "More then 100")
    ))
(condCond 5)
(condCond 10)
(condCond 100)
(condCond 101)