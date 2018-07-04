(ns lcsim.events
  (:require
   [re-frame.core :as re-frame]
   [lcsim.db :as db]
   ))

(re-frame/reg-event-db
 ::initialize-db
 (fn [_ _]
   db/default-db))


(re-frame/reg-event-db
 ::move
 (fn [db [e _ _]]
   (let [marked (:marked-cells db)
         mx (first marked)
         my (second marked)
         dimensions (:grid-dimensions db)
         direction (:direction db)
         dx (:x direction)
         dy (:y direction)]
     (println "============" e mx my)

     (assoc db :marked-cells [(+ dx mx) (+ dy my)]))))

(re-frame/reg-event-db
 ::set-direction
 (fn [db [e d]]
   (let [
         dx (:x d)
         dy (:y d)]
     (println "============" e dx dy)

     (assoc db :direction d))))


