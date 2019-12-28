(ns blog-template.views
  (:require
    [re-frame.core :as re-frame]
    [blog-template.subs :as subs]
    [blog-template.events :as events]
    [markdown-to-hiccup.core :as m]
    [soda-ash.core :as sa]
    [reagent.core :as r]
    [reitit.core :as rt]
    [reitit.frontend :as rf]
    [reitit.frontend.easy :as rfe]
    [reitit.coercion :as rc]
    [reitit.coercion.spec :as rss]
    [spec-tools.data-spec :as ds]
    [blog-template.util :as u]
    [clojure.string :as st]))

;; Header menu
(defn header []
  (let [s (r/atom "home")]
    (fn []
      [:div#header-container
       [sa/Menu {:stackable true
                 :secondary true
                 :pointing true
                 :size "large"
                 :fluid true
                 :borderless true}
        [sa/Container
         [sa/MenuItem
          [sa/Image {:src "img/new-logo.png"
                     :circular true
                     :centered true
                     :size "mini"
                     :onClick #(do
                                 (rfe/push-state ::home)
                                 (reset! s "home"))}]]
         [sa/MenuItem {:content "My blog"
                       :active (if (= "home" @s) true false)
                       :color "green"
                       :onClick #(do
                                   (rfe/push-state ::home)
                                   (reset! s "home"))}]
         [sa/MenuItem {:content "Learn Clojure"
                       :active (if (= "learn" @s) true false)
                       :color "green"
                       :position "right"
                       :onClick #(do
                                   (rfe/push-state ::learn)
                                   (reset! s "learn"))}]
         [sa/MenuItem {:content "Contribute"
                       :active (if (= "contribute" @s) true false)
                       :color "green"
                       :onClick #(do
                                   (rfe/push-state ::contribute)
                                   (reset! s "contribute"))}]
         [sa/MenuItem {:content "About"
                       :active (if (= "about" @s) true false)
                       :color "green"
                       :onClick #(do
                                   (rfe/push-state ::about)
                                   (reset! s "about"))}]]]])))

;; Articles

(defn art-author [a]
  (if (not (empty? (st/trim a)))
    (str "by: " a)))

(defn article-card [slug title summary content topics pub-date author]
  [sa/Card {:header title
            :description summary
            :extra topics
            :meta (str (u/art-date pub-date) "; " (art-author author))
            :onClick #(do
                        (re-frame/dispatch [::events/current-article {:slug slug
                                                                      :title title
                                                                      :summary summary
                                                                      :content content
                                                                      :topics topics
                                                                      :date pub-date
                                                                      :author author}])
                        (rfe/push-state ::article {:slug slug}))}])

(defn articles-list []
  (let [articles (re-frame/subscribe [::subs/articles])]
    [:div
     [sa/Segment {:size "tiny"
                  :raised true
                  :basic true
                  :style {:overflow "auto" :maxHeight 800}}
      [sa/CardGroup {:stackable true
                     :itemsPerRow 1}
       (for [a @articles]
         ^{:key (:title a)}
         [article-card (:slug a) (:title a) (:summary a) (:content a) (:topics a) (:date a) (:author a)])]]]))

(defn current-article
  [slug]
  (let [es (re-frame/subscribe [::subs/articles])]
    (some #(if (= slug (:slug %)) %) @es)))

;; Article page

(defn render-md [md-content]
  (-> md-content
      (m/md->hiccup)
      (m/component)))

(defn article []
  (let [a (re-frame/subscribe [::subs/current-article])]
    [:div#article-body
     (render-md (:content @a))]))

;; Side bars

(defn venue-sponsor
  [name web]
  [:div
   [:a {:href web} name]])

(defn venue-sponsors
  []
  (let [vhs (re-frame/subscribe [::subs/hosts])]
    [:div#side-bar
     [:h4 "Venue sponsors"]
     [sa/Divider]
     (for [h @vhs]
       ^{:key (:name h)}
       [venue-sponsor (:name h) (:web h)])]))

(defn job
  [title web]
  [:div
   [:a {:href web} title]
   [:br]
   [:br]])

(defn jobs
  []
  (let [js (re-frame/subscribe [::subs/jobs])]
    [:div#side-bar
     [:h4 "Clojure(Script) Jobs"]
     [sa/Divider]
     (for [j @js]
       ^{:key (:title j)}
       [job (:title j) (:web j)])]))

(defn meetups-link []
  [:div
   [sa/Button {:content "( meetups )"
               :size "large"
               :color "facebook"
               :fluid true
               :onClick #(.open js/window "https://www.meetup.com/Bangalore-Clojure-User-Group/")}]])

(defn side-bar
  []
  [:div
   [meetups-link]
   [:br]
   [jobs]
   [:br]
   [venue-sponsors]])

(defn home []
  [:div#main-body
   [sa/Grid {:stackable true}
    [sa/GridRow
     [sa/GridColumn {:width "12"}
      [:div#home-articles
       [articles-list]]]
     [sa/GridColumn {:width "4"}
      [side-bar]]]]])

(defn articles-page []
  [:div#main-body
   [sa/Grid {:stackable true}
    [sa/GridRow
     [sa/GridColumn {:width "12"}
      [articles-list]]
     [sa/GridColumn {:width "4"}
      [side-bar]]]]])

;; Contribute
(defn contribute []
  (let [c (re-frame/subscribe [::subs/contribute])]
    [:div#page-body
     (render-md @c)]))

;; About page
(defn about []
  (let [ab (re-frame/subscribe [::subs/about])]
    [:div#page-body
     (render-md @ab)]))

;; Learn
(defn learn []
  (let [h (re-frame/subscribe [::subs/learn])]
    [:div#page-body
     (render-md @h)]))

;; Routes
(def routes
  [["/"
    {:name ::home
     :view home}]

   ["/articles"
    {:name ::articles
     :view articles-page}]

   ["/article/:slug"
    {:name ::article
     :view article
     :parameters {:path {:slug string?}}
     :controllers
     [{:parameters {:path [:slug]}
       :start (fn [{:keys [path]}]
                (re-frame/dispatch-sync [::events/current-article (current-article (:slug path))]))}]}]

   ["/contribute"
    {:name ::contribute
     :view contribute}]

   ["/learn"
    {:name ::learn
     :view learn}]

   ["/about"
    {:name ::about
     :view about}]])

;; Main

(defonce match (r/atom nil))

(defn current-page []
  [:div
   [header]
   (if @match
     (let [view (:view (:data @match))]
       [view @match]))])
