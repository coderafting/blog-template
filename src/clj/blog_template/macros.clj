(ns blog-template.macros
  (:require [clojure.java.io :as io]))


(defmacro readfile [file-path]
  (slurp (io/resource file-path)))
