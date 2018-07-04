(ns lcsim.views
  (:require
   [re-frame.core :as rf]
   [lcsim.subs :as subs]))

(defn get-color [cell-type]
  (cond
    (= cell-type "blue") "lightblue"
    (= cell-type "black") "black"
    :else "white"))

(defn cell [x y]
  (let [marked  @(rf/subscribe [::subs/marked-cells])
        type (if (and (= x (first marked))
                      (= y (second marked)))
               "blue"
               )]
    (println "~~~~~~~" (first marked) x (second marked) y type)
    [:div
     {:id "sd"
      :style {:border "solid"
              :border-width 1
              :width 10
              :height 10
              :background-color (get-color type)}}]))

(defn grid-row [y w]
  [:div {:style
         {:display "flex"
          :flex-wrap "nowrap"}}
   (for [x (range w)] [cell x y])])


(defn grid []
  (let [grid-dimensions @(rf/subscribe [::subs/grid-dimensions])
        w (:w grid-dimensions)
        h (:h grid-dimensions)]
    [:div
     (for [y (range h)] [grid-row y w])]))

(defn main-panel []
  (let [name (rf/subscribe [::subs/name])]
    [:div
     [:p "H " @name]
     [grid]]))


