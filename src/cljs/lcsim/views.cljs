(ns lcsim.views
  (:require
   [re-frame.core :as rf]
   [lcsim.events :as events]
   [lcsim.subs :as subs]
   [lcsim.game-control :as game-control]
   ))

(defn get-color [is-shape is-bullet is-ship]
  (cond
    (and is-bullet is-shape) "red"
    is-shape "#186396"
    is-bullet "#4deaff"
    is-ship "#35a8ff"
    :else "#ebf9d1"))

(defn cell [x y]
  (let [shape-cells  @(rf/subscribe [::subs/shape-cells])
        bullet-cells  @(rf/subscribe [::subs/bullet-cells])
        ship-cells  @(rf/subscribe [::subs/ship-cells])
       
        is-shape (some #(= [x y] %) shape-cells)
        is-bullet (some #(= [x y] %) bullet-cells)
        is-ship (some #(= [x y] %) ship-cells)]

    [:div
     {:style {:margin 1
              :width 12
              :height 12
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
         :on-change #(rf/dispatch [::events/lcd-text-change (-> % .-target .-value)])}]])))

(defn repeat-text []
      [:div
       [:p "The LCD text is: " @(rf/subscribe [::subs/lcd-text])]])

(defn score-text []
      [:div
       [:h2 "SCORE: " @(rf/subscribe [::subs/score])]])

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

(defn start-button []
  [:button  {:on-click #(do (rf/dispatch [::events/set-active-panel :game])
                            (rf/dispatch [::events/add-i-my])
                            (game-control/start-game))} "START"])

; SCREENS
(defn welcome
  []
  (let [has-name (not (empty? @(rf/subscribe [::subs/lcd-text])))]
    [:div
     [repeat-text]
     [grid]
     [input-lcd-text]
     (when has-name [start-button])]))

(defn continue
  []
  [:div
   [grid]
   [:p "(you can change your position)"]
   [start-button]])

(defn game
  []
  [:div
   [score-text]
   [grid]
   [controls]])

(defn retry
  []
  [:h2 "Game over"
   [:br]
   [:button  {:on-click #(rf/dispatch [::events/set-active-panel :continue])} "CONTINUE"]])

(defn won
  []
  [:div 
  [:h2 "Congratulations you won the prize of non-attachment and patience ;)"]
  [:p "BTW " [score-text] " is not important"]])

(defn main-panel
  []
  (let [active  (rf/subscribe [::subs/active-panel])]
    (fn []
      [:div {:style
             {:text-align "center"
              :font-family "Monaco, monospace"}}
       [:h1 "Ego Annihilator"]
       (condp = @active
         :welcome   [welcome]
         :continue  [continue]
         :won   (do (game-control/clear-intervals) [won])
         :retry  (do (game-control/clear-intervals) [retry])
         :game   [game])])))


