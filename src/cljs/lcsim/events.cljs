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
   (let [cells-pos (:marked-cells db)
         cells-x (for [pos cells-pos] (first pos))
         cells-y (for [pos cells-pos] (second pos))
         dimensions (:grid-dimensions db)
         direction (:direction db)
         dx (:x direction)
         dy (:y direction)
         tox (map #( + dx %) cells-x)
         toy (map #(+ dy %) cells-y)
         newpositions (utils/wrap-move dimensions tox toy)
         vex (first newpositions)
         vey (second newpositions)
         veo (apply map vector [vex vey])
         ]
     (println "============" e  cells-x cells-y "tox" tox "newpositions" newpositions "vex" vex "veo" veo)
     (assoc db :marked-cells veo))))

(re-frame/reg-event-db
 ::set-direction
 (fn [db [e d]]
     (println "============" e d)

     (assoc db :direction d)))


