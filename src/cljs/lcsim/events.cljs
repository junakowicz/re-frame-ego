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


(defn offset-cells [cells-pos offset-xy]
   (let [cells-x (for [pos cells-pos] (first pos))
         cells-y (for [pos cells-pos] (second pos))
         dx (:x offset-xy)
         dy (:y offset-xy)
         tox (map #(+ dx %) cells-x)
         toy (map #(+ dy %) cells-y)]
      [tox toy]
     ))


(re-frame/reg-event-db
 ::move
 (fn [db [e _ _]]
   (let [cells-pos (:marked-cells db)
         direction (:direction db)
         [tox toy] (offset-cells cells-pos direction)
         dimensions (:grid-dimensions db)
         newpositions (utils/wrap-move dimensions tox toy)
        ;  [vex vey] newpositions
         veo (apply map vector newpositions);recreate to format [[1 1] [0 1]]
         ]
    ;  (println "============" e "tox" tox "newpositions" newpositions "vex" vex "veo" veo)
     (assoc db :marked-cells veo))))

(re-frame/reg-event-db
 ::set-direction
 (fn [db [e d]]
     (println "============" e d)

     (assoc db :direction d)))


