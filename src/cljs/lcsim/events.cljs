(ns lcsim.events
  (:require
   [re-frame.core :as re-frame]
   [lcsim.db :as db]
   ))

(re-frame/reg-event-db
 ::initialize-db
 (fn [_ _]
   db/default-db))
;;move to utils
(defn check-bounds [pos max]
  (cond
    (> pos max) 0
    (< pos 0) max
    :else pos))

(defn wrap-move [dimensions xpos ypos]
  (let [xmax (:w dimensions)
        ymax (:h dimensions)
        xnew (check-bounds xpos xmax)
        ynew (check-bounds ypos ymax)]
    [xnew ynew]
    ))

(re-frame/reg-event-db
 ::move
 (fn [db [e _ _]]
   (let [cell-pos (:marked-cells db)
         cx (first cell-pos)
         cy (second cell-pos)
         dimensions (:grid-dimensions db)
         direction (:direction db)
         dx (:x direction)
         dy (:y direction)
         tox (+ dx cx)
         toy (+ dy cy)]
     (println "============" e cx cy)

     (assoc db :marked-cells (wrap-move dimensions tox toy)))))

(re-frame/reg-event-db
 ::set-direction
 (fn [db [e d]]
   (let [
         dx (:x d)
         dy (:y d)]
     (println "============" e dx dy)

     (assoc db :direction d))))


