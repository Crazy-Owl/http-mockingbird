(ns http-mockingbird.utils
  (:require [org.httpkit.client :as http]))

(defn get-path [coll [x & xs]]
  "Traverses the collection (nested), tries to find a value for the given path. Returns nil if path is unreachable or nothing is found. Useful for ensuring the presence and structure of JSON response"
  (cond
   (nil? coll) nil
   (empty? xs) (get coll x)
   :else (recur (get coll x) xs)))
