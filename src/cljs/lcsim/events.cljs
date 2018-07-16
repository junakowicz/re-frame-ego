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

; SCREENS
(re-frame/reg-event-db
 ::set-active-panel
 (fn [db [_ value]]
   (assoc db :active-panel value)))


(re-frame/reg-event-db
 ::move-shapes
 (fn [db [e _ _]]
   (let [cells-pos (get-in db [:shapes :cells])
         direction (get-in db [:shapes :direction] )
         [tox toy] (utils/offset-cells cells-pos direction)
         dimensions (:grid-dimensions db)
         newpositions (utils/wrap-move dimensions tox toy)
         veo (apply map vector newpositions);recreate to format [[1 1] [0 1]]
         ]
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

(defn subtract [all bad]
  (let [as (into #{} all)
        bs (into #{} bad)]
    (into [] (clojure.set/difference as bs))))

(re-frame/reg-event-fx
 ::check-collision
 (fn [{:keys [db]} [_ _]]
   (let [bullets-pos (get-in db [:bullets :cells])
         shape-pos (get-in db [:shapes :cells])
         ship-pos (get-in db [:ship :cells])
         shape-bullet (for [bullet bullets-pos] (if (some #(= bullet %) shape-pos) bullet))
         shape-bullet-clean (filter #(not= nil %) shape-bullet)
         shape-ship (for [ship ship-pos] (if (some #(= ship %) shape-pos) ship))
         shape-ship-clean (filter #(not= nil %) shape-ship)
         out-shape-pos (if (not (empty? shape-bullet-clean))  (subtract shape-pos shape-bullet-clean) shape-pos)
         out-bullets-pos (if (not (empty? shape-bullet-clean)) (subtract bullets-pos shape-bullet-clean) bullets-pos)
         db-out (-> db
                    (assoc-in [:shapes :cells] out-shape-pos)
                    (assoc-in [:bullets :cells] out-bullets-pos))
         shape-direction {:x (if (even? (:score db)) 1 -1) :y 1}]

     (cond
       (not (empty? shape-ship-clean)) {:db db ::game-over :lost}
       (empty? shape-pos) {:db db ::game-over :won}
       (not (empty? shape-bullet-clean)) {:db db-out :dispatch-n (list [::update-score 1] [::set-direction shape-direction])} ;dispatch-n
       :else {:db db-out}))))

(re-frame/reg-event-db
 ::set-direction
 (fn [db [e d f]]
     (println "============" e d f)
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

;effect example
(re-frame/reg-fx
 ::game-over
 (fn [d]
   (println "============reg-fxreg-fxfx"  d)
   (if (= d :won) 
        (re-frame/dispatch [::set-active-panel :won])
        (re-frame/dispatch  [::set-active-panel :retry]))))

(re-frame/reg-event-db
 ::reset-screen
 (fn [db [e d]]
   (let [txt (:lcd-text db)
         cells (utils/cells-from-text txt)]
     (assoc-in db [:shapes :cells] cells))))

(re-frame/reg-event-db
 ::add-i-my
 (fn [db [e d]]
   (let [txt (:cells db/i-my)
         cells-pos (get-in db [:shapes :cells])
        all (concat txt cells-pos)
         ]
     (assoc-in db [:shapes :cells] all))))

;;SHIP
(re-frame/reg-event-db
 ::emit-bullet
 (fn [db [e _]]
   (let [cells-pos (get-in db [:ship :cells])
         [lcx lcy] (first cells-pos)
         bullet-pos [(inc lcx) lcy]
         current-bullets (get-in db [:bullets :cells])
         updated-bullets (concat current-bullets [bullet-pos])]
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
 ::control-ship
 (fn [{:keys [db]} [e k]]
   (println "===========controll=fx" e k)
   (let [step (cond
                (= k 38) {:x 0 :y -1}
                (= k 40) {:x 0 :y 1}
                (= k 37) {:x -1 :y 0}
                (= k 39) {:x 1 :y 0})]
     {:db db
      :dispatch [::move-ship step]})))

(re-frame/reg-event-fx
 ::fire
 (fn [{:keys [db]} [e k]]
   (println "===========controll=fx" e k)
  (if (not= :continue (:active-panel db)) 
        {:db db :dispatch-n (list [::emit-bullet] [::update-score -1]) }
        {:db db})))

