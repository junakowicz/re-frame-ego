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
         direction (get-in db [:bullets :direction])
         [tox toy] (utils/offset-cells cells-pos direction)
         dimensions (:grid-dimensions db)
         newpositions (utils/destroy-out dimensions tox toy)
         veo (apply map vector newpositions);recreate to format [[1 1] [0 1]]
         filtered (filterv (fn [x] (not (or (= (first x) nil) (= (second x) nil)))) veo)]
     (assoc-in db [:bullets :cells] filtered))))


(re-frame/reg-event-fx
 ::check-collision
 (fn [{:keys [db]} [_ _]]
   (let [bullets-pos (get-in db [:bullets :cells])
         shape-pos (get-in db [:shapes :cells])
         colision (first (for [bullet bullets-pos] (if (some #(= bullet %) shape-pos) bullet)))
         colisions (for [bullet bullets-pos] (if (some #(= bullet %) shape-pos) bullet))
         out-shape-pos (if colision (filterv (fn [x] 
          (println "x" x)
          (not(= x colision))) shape-pos) shape-pos)
         out-bullets-pos (if colision (filterv (fn [x]
                        (not (= x colision))) bullets-pos) bullets-pos)
         db-removed-shapes (assoc-in db [:shapes :cells] out-shape-pos)
         db-removed-bullets-shapes (assoc-in db-removed-shapes [:bullets :cells] out-bullets-pos)
   ]
                 (println "colisions" colisions "colision: " colision "bullets-pos" bullets-pos)


      (if colision {:db db-removed-bullets-shapes
                    :dispatch [::update-score 1]}
                   {:db db-removed-bullets-shapes})
     )))

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

(re-frame/reg-event-fx                             
 ::update-score
 (fn [{:keys [db]} [e d]]
     (println "============fx" e d)
                   
   {:db (assoc db :score (+ d (:score db)))          
    }))

(re-frame/reg-event-db
 ::reset-screen
 (fn [db [e d]]
   (println "============" e d)
   (let [txt (:lcd-text db)
         cells (utils/cells-from-text txt)]
     (println "============txt" txt "cells" cells)

     (assoc-in db [:shapes :cells] cells))))

;;SHIP

(re-frame/reg-event-db
 ::emit-bullet
 (fn [db [e _]]
   (let [cells-pos (get-in db [:ship :cells])
         [lcx lcy] (first cells-pos)
         bullet-pos [(inc lcx) lcy]
        
     current-bullets (get-in db [:bullets :cells])
     updated-bullets (concat current-bullets [bullet-pos]) ]
     (println "===========" "lcx lcy" lcx lcy "bullet-pos " bullet-pos "current-bullets" current-bullets "updated-bullets" updated-bullets)
     (assoc-in db [:bullets :cells] updated-bullets))))

(re-frame/reg-event-db
 ::move-ship
 (fn [db [e direction]]
   (let [cells-pos (get-in db [:ship :cells])
         [tox toy] (utils/offset-cells cells-pos direction)
         dimensions (:grid-dimensions db)
         newpositions (utils/wrap-move dimensions tox toy)
         veo (apply map vector newpositions);recreate to format [[1 1] [0 1]]
         ]
     (assoc-in db [:ship :cells] veo))))

(re-frame/reg-event-fx
 ::controll-ship
 (fn [{:keys [db]} [e k]]
   (println "===========controll=fx" e k)
   (let [step (cond
                (= k 38) {:x 0 :y -1}
                (= k 40) {:x 0 :y 1}
                (= k 37) {:x -1 :y 0}
                (= k 39) {:x 1 :y 0})
     action (if step [::move-ship step]
                [::emit-bullet])]

     {:db  db
      :dispatch action})))

