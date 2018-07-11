(ns lcsim.core
  (:require
   [reagent.core :as reagent]
   [re-frame.core :as re-frame]
   [lcsim.events :as events]
   [lcsim.views :as views]
   [lcsim.config :as config]
   [lcsim.db :as db]))

(defn dev-setup []
  (when config/debug?
    (enable-console-print!)
    (println "dev mode")))

(defn mount-root []
  (re-frame/clear-subscription-cache!)
  (reagent/render [views/main-panel]
                  (.getElementById js/document "app")))

(defn ^:export init []
  (re-frame/dispatch-sync [::events/initialize-db])
  (re-frame/dispatch-sync [::events/reset-screen])
  (dev-setup)
  (mount-root))

(defn move-shapes []
  (re-frame/dispatch [::events/move-shapes]))

(defn move-bullets []
  (re-frame/dispatch [::events/move-bullets])
  (re-frame/dispatch [::events/check-collision]))

(defn keydown [e]
  (println (.-keyCode e))
  (if (some #(= (.-keyCode e) %) [37 38 39 40])
    (do (.preventDefault e)
        (re-frame/dispatch [::events/controll-ship (.-keyCode e)])))

  (if (some #(= (.-keyCode e) %) [32])
    (do (.preventDefault e)
        (re-frame/dispatch [::events/fire (.-keyCode e)]))))

(def intervals (atom []))

(defn clear-intervals []
  (while (last @intervals) (do (println (last @intervals) "cleared")
                               (js/clearInterval (last @intervals))
                               (swap! intervals drop-last))))

(defn start-game []
  (swap! intervals conj (js/setInterval #(move-shapes) 500))
  (swap! intervals conj (js/setInterval #(move-bullets) 250))
  (println "INTERVALS " @intervals)
  (set! (.-onkeydown js/document) keydown))

;change direction
;score -1
