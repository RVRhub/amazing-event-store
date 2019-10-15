(ns amazing-event-store.event-store
  (:gen-class))

(defn commandToEvent
  "This function returns the event by command name"
  [x]
  (def commandType {'create "CreateEvent", 'makeOffer "MakeOfferEvent", 'makeDecision "PlannedEvent"})
  (get commandType x)
  )

(defn storeEventByCommandType
  "This function store event by command type"
  [aggregateId command]
  (def event (commandToEvent command))
  (println "Aggregate with id:" aggregateId " has new event" event)
  )

(storeEventByCommandType 111 'makeDecision)
