(ns lcsim.views
  (:require
   [re-frame.core :as rf]
   [lcsim.subs :as subs]
   ))
(defn cell []
  (let [grid-dimensions (rf/subscribe [::subs/grid-dimensions])]
    [:div
     {:style { :border "solid"
              ;  :flex 1
               :border-width 1
               :width 10
               :height 10}}
     ]))

(defn grid-row [w]
    [:div {:style
           {:display "flex"
            :flex-wrap "nowrap"}}
          (for [x (range w)] [cell])])
          
(defn grid []
  (let [grid-dimensions @(rf/subscribe [::subs/grid-dimensions])
        w (:w grid-dimensions)
        h (:h grid-dimensions)
        outx []]
    (println grid-dimensions)
    [grid-row w]))



(defn main-panel []
  (let [name (rf/subscribe [::subs/name])]
    [:div
     [:p "H " @name]
     [grid]
     ]))


