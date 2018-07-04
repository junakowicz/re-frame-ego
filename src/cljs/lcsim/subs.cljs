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
 ::marked-cells
 (fn [db]
   (:marked-cells db)))
