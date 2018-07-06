(ns lcsim.views
  (:require
   [re-frame.core :as rf]
   [lcsim.events :as events]
   [lcsim.subs :as subs]))

(defn get-color [is-shape is-bullet is-ship]
  (cond
    (and is-bullet is-shape) "red"
    is-shape "lightblue"
    is-bullet "black"
    is-ship "lightgreen"
    :else "white"))

(defn cell [x y]
  (let [shape-cells  @(rf/subscribe [::subs/shape-cells])
        bullet-cells  @(rf/subscribe [::subs/bullet-cells])
        ship-cells  @(rf/subscribe [::subs/ship-cells])
       
        is-shape (some #(= [x y] %) shape-cells)
        is-bullet (some #(= [x y] %) bullet-cells)
        is-ship (some #(= [x y] %) ship-cells)]

(if is-bullet (println "bullet at" x y "ship-cells" ship-cells))
(if is-shape (println "shape at" x y))
    ; (println "bullet-cells " bullet-cells)
    ; (println "has-shape " has-shape "type" type)
    [:div
     {:id "sd"
      :style {:border "solid"
              :border-width 1
              :width 10
              :height 10
              :background-color (get-color is-shape is-bullet is-ship)}}]))

(defn grid-row [y w]
  [:div {:style
         {:display "flex"
          :flex-wrap "nowrap"}}
   (for [x (range w)] [cell x y])])

(defn input-lcd-text []
  (let [lcd-text (rf/subscribe [::subs/lcd-text])]
    (fn []
      [:div
        [:input       {:value @lcd-text
         :on-change   #(rf/dispatch [::events/lcd-text-change (-> % .-target .-value)])}]])))

(defn repeat-text []
      [:div
       [:p "The LCD text is: " @(rf/subscribe [::subs/lcd-text])]
       ])


(defn grid []
  (let [grid-dimensions @(rf/subscribe [::subs/grid-dimensions])
        w (:w grid-dimensions)
        h (:h grid-dimensions)]
    [:div
     (for [y (range h)] [grid-row y w])]))

(defn controls []
    [:div
     [:div {:style
            {:display "flex"
              :flex-wrap "nowrap"}}
     [:button {:on-click #(rf/dispatch [::events/set-direction {:x -1 :y -1}])} "\\"]
     [:button {:on-click #(rf/dispatch [::events/set-direction {:x 0 :y -1}])} "|"]
     [:button {:on-click #(rf/dispatch [::events/set-direction {:x 1 :y -1}])} "/"]]
     [:div {:style
            {:display "flex"
              :flex-wrap "nowrap"}}
     [:button {:on-click #(rf/dispatch [::events/set-direction {:x -1 :y 0}])} "<"]
     [:button {:on-click #(rf/dispatch [::events/set-direction {:x 0 :y 0}])} "O"]
     [:button {:on-click #(rf/dispatch [::events/set-direction {:x 1 :y 0}])} ">"]
      ]
     [:div {:style
            {:display "flex"
              :flex-wrap "nowrap"}}
     [:button {:on-click #(rf/dispatch [::events/set-direction {:x -1 :y 1}])} "/"]
     [:button {:on-click #(rf/dispatch [::events/set-direction {:x 0 :y 1}])} "|"]
     [:button {:on-click #(rf/dispatch [::events/set-direction {:x 1  :y 1}])} "\\"]]])

(defn main-panel []
  (let [name (rf/subscribe [::subs/name])]
    [:div
     [:p "H " @name]
     [repeat-text]
     [grid]
     [controls]
     [input-lcd-text]
     ]
             ))


