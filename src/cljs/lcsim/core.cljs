(ns lcsim.core
  (:require
   [reagent.core :as reagent]
   [re-frame.core :as re-frame]
   [lcsim.events :as events]
   [lcsim.views :as views]
   [lcsim.config :as config]
   [lcsim.db :as db]
   ))

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

(defn move []
  (re-frame/dispatch [::events/move-shapes])
  (re-frame/dispatch [::events/move-bullets])
  )

(defn keydown [e]
  (println (.-keyCode e))
  (if (some #(= (.-keyCode e) %) [32 37 38 39 40])
    (do (.preventDefault e)
        (re-frame/dispatch [::events/controll-ship (.-keyCode e)]))))

(js/setInterval #(move) 1000)
(set! (.-onkeydown js/document) keydown)


