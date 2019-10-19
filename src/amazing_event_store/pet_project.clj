(ns amazing-event-store.pet-project)

(def buyerAccount (ref 100))
(def merchantAccount (ref 0))
(def prices {'pen 1, 'notebook 5, 'backpack 90})
(def items (ref []))

(defn printInfo
  []
  (println "PrintInfo:")
  (println "buyerAccount:" @buyerAccount)
  (println "merchantAccount:" @merchantAccount)
  (println "items:" @items)
  )

(defn buy
  [item]
  (def itemPrice (get prices item))
  (if (<= itemPrice @buyerAccount)
    (dosync
      (ref-set merchantAccount (+ @merchantAccount itemPrice))
      (ref-set buyerAccount (- @buyerAccount itemPrice))
      (def newItem (cons item @items))
      (ref-set items newItem)
      )
    (println "Insufficient funds")
    )
  (printInfo)
  )

(buy 'pen)
(buy 'notebook)
(buy 'backpack)
(buy 'notebook)
(buy 'notebook)


