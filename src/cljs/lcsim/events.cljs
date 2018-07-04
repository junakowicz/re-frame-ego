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
 ::update-marked
 (fn [db [e _ _]]
   (let [marked (:marked-cells db)
         mx (first marked)
         my (second marked)]
   (println "============"e mx my)
  
   (assoc db :marked-cells [(inc mx) my])
      )))


