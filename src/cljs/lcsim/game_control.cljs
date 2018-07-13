(ns lcsim.game-control
  (:require
   [re-frame.core :as rf]
   [lcsim.events :as events]
   [lcsim.subs :as subs]))

(defn move-shapes []
  (rf/dispatch [::events/move-shapes]))

(defn move-bullets []
  (rf/dispatch [::events/move-bullets])
  (rf/dispatch [::events/check-collision]))

(defn keydown [e]
  (println (.-keyCode e))
  (if (some #(= (.-keyCode e) %) [37 38 39 40])
    (do (.preventDefault e)
        (rf/dispatch [::events/control-ship (.-keyCode e)])))

  (if (some #(= (.-keyCode e) %) [32])
    (do (.preventDefault e)
        (rf/dispatch [::events/fire (.-keyCode e)]))))

(def intervals (atom []))

(defn clear-intervals []
  (while (last @intervals) (do (println (last @intervals) "cleared")
                               (js/clearInterval (last @intervals))
                               (swap! intervals drop-last))))

(defn start-game []
  (swap! intervals conj (js/setInterval #(move-shapes) 800))
  (swap! intervals conj (js/setInterval #(move-bullets) 50))
  (println "INTERVALS " @intervals)
  (set! (.-onkeydown js/document) keydown))