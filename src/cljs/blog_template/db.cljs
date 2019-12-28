(ns blog-template.db
  (:require [cljs-time.core :as t]
            [cljs-time.coerce :as c]
            [blog-template.util :as u])
  (:require-macros [blog-template.macros :refer [readfile]]))

;; You can always have better way to build articles collection by writing program to read files in resources directory.
;; You can write fns to make sure that slug and title are unique
;; But, I think they are a diversion at this initial stage, especially with just one contributor, you.
(def articles
  [{:slug "markdown-demo"
    :title "How to write markdown content"
    :summary "A demo of markdown syntax."
    :content (readfile "md/articles/markdown-demo.md")
    :topics "Clojure | Markdown"
    :date "28 12 2019"
    :author ""}
   {:slug "hack-session-meetup-sep-2018"
    :title "The hack session during Sept 2018 meetup"
    :summary "Learn about what clojurians built during this hackathon."
    :content (readfile "md/articles/meetup-sep-2018.md")
    :topics "Clojure | ClojureScript | Hackathon"
    :date "15 9 2018"
    :author "Amarjeet"}])

(defn sorted
  [arts]
  (reverse (sort-by #(c/to-long (:datetime (u/date-format (:date %)))) arts)))

(defn contribute [] (readfile "md/contribute/contribute.md"))
(defn learn [] (readfile "md/learn/clojure.md"))
(defn about [] (readfile "md/about/about.md"))

(def venue-hosts ; sample
  [{:name "Quintype"
    :web "https://www.quintype.com/"}
   {:name "SAP Concur"
    :web "https://www.concur.co.in/"}
   {:name "Nilenso"
    :web "https://nilenso.com/"}
   {:name "Go-Jek Tech"
    :web "https://www.gojek.io/"}])

(def jobs ; sample
  [{:title "Clojure Developer, FormCept, Bangalore"
    :web "https://www.formcept.com/careers/"}
   {:title "Clojure Developer, WebEngage, Bangalore & Mumbai"
    :web "https://webengage.com/current-openings/#op-197822-clojure-programmer"}])

;; Main DB
(def main-db
  {:articles (sorted articles)
   :contribute (contribute)
   :learn (learn)
   :about (about)
   :venue-hosts venue-hosts
   :jobs jobs})
