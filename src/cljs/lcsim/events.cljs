(ns lcsim.events
  (:require
   [re-frame.core :as re-frame]
   [clojure.string :as string]
   [lcsim.db :as db]
   [lcsim.utils :as utils]
   ))

(re-frame/reg-event-db
 ::initialize-db
 (fn [_ _]
   db/default-db))


(re-frame/reg-event-db
 ::move-shapes
 (fn [db [e _ _]]
   (let [cells-pos (get-in db [:shapes :cells])
         direction (get-in db [:shapes :direction] )
         [tox toy] (utils/offset-cells cells-pos direction)
         dimensions (:grid-dimensions db)
         newpositions (utils/wrap-move dimensions tox toy)
        ;  [vex vey] newpositions
         veo (apply map vector newpositions);recreate to format [[1 1] [0 1]]
         ]
    ;  (println "============" e "tox" tox "newpositions" newpositions  "veo" veo)
     (assoc-in db [:shapes :cells] veo))))

(re-frame/reg-event-db
 ::move-bullets
 (fn [db [e _ _]]
   (let [cells-pos (get-in db [:bullets :cells])
         direction (get-in db [:bullets :direction] )
         [tox toy] (utils/offset-cells cells-pos direction)
         dimensions (:grid-dimensions db)
         newpositions (utils/wrap-move dimensions tox toy)
         veo (apply map vector newpositions);recreate to format [[1 1] [0 1]]
         ]
     (assoc-in db [:bullets :cells] veo))))

(re-frame/reg-event-db
 ::set-direction
 (fn [db [e d]]
     (println "============" e d)
     (assoc-in db [:shapes :direction] d)))

; https://github.com/Day8/re-frame/blob/master/docs/EffectfulHandlers.md
(re-frame/reg-event-fx                             
 ::lcd-text-change
 (fn [{:keys [db]} [e d]]
     (println "============fx" e d)
                   
   {:db  (assoc db :lcd-text (string/upper-case d))          
    :dispatch [::reset-screen]
    }))

(re-frame/reg-event-db
 ::reset-screen
 (fn [db [e d]]
   (println "============" e d)
   (let [txt (:lcd-text db)
         cells (utils/cells-from-text txt)]
     (println "============txt" txt "cells" cells)

     (assoc-in db [:shapes :cells] cells))))


