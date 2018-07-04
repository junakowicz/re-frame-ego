(ns lcsim.events
  (:require
   [re-frame.core :as re-frame]
   [lcsim.db :as db]
   [lcsim.utils :as utils]
   ))

(re-frame/reg-event-db
 ::initialize-db
 (fn [_ _]
   db/default-db))

(re-frame/reg-event-db
 ::move
 (fn [db [e _ _]]
   (let [cell-pos (:marked-cells db)
         cell-x (first cell-pos)
         cell-y (second cell-pos)
         dimensions (:grid-dimensions db)
         direction (:direction db)
         dx (:x direction)
         dy (:y direction)
         tox (+ dx cell-x)
         toy (+ dy cell-y)]
     (println "============" e cell-x cell-y)
     (assoc db :marked-cells (utils/wrap-move dimensions tox toy)))))

(re-frame/reg-event-db
 ::set-direction
 (fn [db [e d]]
     (println "============" e d)

     (assoc db :direction d)))


