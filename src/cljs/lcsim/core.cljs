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


