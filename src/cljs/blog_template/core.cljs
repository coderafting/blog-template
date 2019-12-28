(ns blog-template.core
  (:require
    [reagent.core :as reagent]
    [re-frame.core :as re-frame]
    [blog-template.events :as events]
    [blog-template.views :as v]
    [blog-template.subs :as subs]
    [blog-template.config :as config]
    [reitit.frontend :as rf]
    [reitit.frontend.easy :as rfe]
    [reitit.coercion.spec :as rss]
    [reitit.frontend.controllers :as rfc]))

(defn dev-setup []
  (when config/debug?
    (enable-console-print!)
    (println "dev mode")))

(defn mount-root []
  (re-frame/clear-subscription-cache!)
  (rfe/start!
    (rf/router v/routes {:data {:coercion rss/coercion}})
    (fn [new-match]
      (swap! v/match (fn [old-match]
                       (if new-match
                         (assoc new-match :controllers (rfc/apply-controllers (:controllers old-match) new-match))))))
    {:use-fragment true})
  (reagent/render [v/current-page]
                  (.getElementById js/document "app")))

(defn ^:export init []
  (re-frame/dispatch-sync [::events/initialize-db])
  (dev-setup)
  (mount-root))
