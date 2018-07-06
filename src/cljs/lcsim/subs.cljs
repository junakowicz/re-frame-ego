(ns lcsim.subs
  (:require
   [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::name
 (fn [db]
   (:name db)))


(re-frame/reg-sub
 ::grid-dimensions
 (fn [db]
   (:grid-dimensions db)))

(re-frame/reg-sub
 ::shape-cells
 (fn [db]
   (get-in db [:shapes :cells])))

(re-frame/reg-sub
 ::bullet-cells
 (fn [db]
   (get-in db [:bullets :cells])))

(re-frame/reg-sub
 ::ship-cells
 (fn [db]
   (get-in db [:ship :cells])))

(re-frame/reg-sub
 ::lcd-text
 (fn [db]
   (:lcd-text db)))
