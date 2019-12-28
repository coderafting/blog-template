(ns blog-template.util
  (:require [cljs-time.core :as t]
            [cljs-time.coerce :as c]
            [cljs.reader :as r]
            [clojure.string :as st]))

(def month-index
  {"1" "Jan" "2" "Feb" "3" "Mar" "4" "Apr" "5" "May" "6" "June"
   "7" "Jul" "8" "Aug" "9" "Sep" "10" "Oct" "11" "Nov" "12" "Dec"})

(defn date-format
  [ddmmyyyy-string]
  (let [ds (clojure.string/split ddmmyyyy-string #" ")
        d (first ds)
        m (month-index (second ds))
        y (last ds)]
    {:day (r/read-string d)
     :month (r/read-string (second ds))
     :year (r/read-string y)
     :date (str m " " d ", " y)
     :datetime (t/date-time (r/read-string y) (r/read-string (second ds)) (r/read-string d))
     :future-event? (t/after? (t/date-time (r/read-string y) (r/read-string (second ds)) (r/read-string d))
                              (t/now))}))

(defn art-date
  [date-string]
  (:date (date-format date-string)))

(defn future-event?
  [date-string]
  (:future-event? (date-format date-string)))
