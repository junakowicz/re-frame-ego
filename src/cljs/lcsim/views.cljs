(ns lcsim.views
  (:require
   [re-frame.core :as rf]
   [lcsim.events :as events]
   [lcsim.subs :as subs]))

(defn get-color [cell-type]
  (cond
    (= cell-type "blue") "lightblue"
    (= cell-type "black") "black"
    :else "white"))

(defn cell [x y]
  (let [marked  @(rf/subscribe [::subs/shape-cells])
        typem (map #(if (and (= x (first %))
                      (= y (second %)))
               "blue"
               ) marked)
        type (reduce (fn [a b] (or a b)) typem)
]
    ; (println "typem " typem)
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


