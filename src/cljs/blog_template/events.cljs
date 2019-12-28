(ns blog-template.events
  (:require
    [re-frame.core :as re-frame]
    [blog-template.db :as bdb]))

(re-frame/reg-event-db
  ::initialize-db
  (fn [db _]
    (assoc db :db bdb/main-db)))

(re-frame/reg-event-db
  ::current-article
  (fn [db [_ article]]
    (assoc db :current-article article)))
