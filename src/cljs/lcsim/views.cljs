(ns lcsim.views
  (:require
   [re-frame.core :as rf]
   [lcsim.events :as events]
   [lcsim.subs :as subs]
   [lcsim.core :as core]
   ))

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

    [:div
     {:style {:border "solid"
              :border-width 1
              :border-color "lightgray"
              :width 10
              :height 10
              :background-color (get-color is-shape is-bullet is-ship)}}]))

(defn grid-row [y w]
  [:div {:style
         {:display "flex"
          :flex-wrap "nowrap"}}
   (for [x (range w)] ^{:key (str y x)} [cell x y])])

(defn input-lcd-text []
  (let [lcd-text (rf/subscribe [::subs/lcd-text])]
    (fn []
      [:div "ENTER YOUR NAME"
       [:br]
        [:input  {:value @lcd-text :maxLength 7
         :on-change   #(rf/dispatch [::events/lcd-text-change (-> % .-target .-value)])}]])))

(defn repeat-text []
      [:div
       [:p "The LCD text is: " @(rf/subscribe [::subs/lcd-text])]
       ])
(defn score-text []
      [:div
       [:h2 "SCORE: " @(rf/subscribe [::subs/score])]
       ])


(defn grid []
  (let [grid-dimensions @(rf/subscribe [::subs/grid-dimensions])
        w (:w grid-dimensions)
        h (:h grid-dimensions)]
     [:div {:style
            {:display "inline-block"}}
     (for [y (range h)] ^{:key (str y w)} [grid-row y w])]))

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

(defn welcome
  []
  [:div
   [repeat-text]
   [grid]
   [input-lcd-text]
   [:button  {:on-click #(rf/dispatch [::events/set-active-panel :game]
                                      (core/start-game))}
    "START"]])

(defn continue
  []
  [:div
   [grid]
   [:p "(you can change your position)"]
   [:button  {:on-click #(rf/dispatch [::events/set-active-panel :game]
                                      (core/start-game))}
    "START"]])

(defn game
  []
  [:div
   [score-text]
   [grid]
   [controls]])

(defn end
  []
  [:h2 "game over"
   [:br]
   [:button  {:on-click #(rf/dispatch [::events/set-active-panel :continue])} "CONTINUE"]])

(defn main-panel
  []
  (let [active  (rf/subscribe [::subs/active-panel])]
    (fn []
      [:div {:style
 {:text-align "center"
  :font-family "sans-serif"
  }}
       [:div "Heading"]
       (condp = @active
         :welcome   [welcome]
         :continue  [continue]
         :end   [end]
         :game   [game])])))


