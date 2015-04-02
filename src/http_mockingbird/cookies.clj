(ns http-mockingbird.cookies
  (:require [clojure.string :as s]))

(defn set-cookie [request cookie-name cookie-val]
  (assoc-in request [:cookies cookie-name] cookie-val))

(defn stringify-cookies [cookies]
  (if cookies
    (->> (for [[k v] cookies]
           [k "=" v ";"])
         (apply concat)
         (apply str))))

(defn split-cookies [s]
  "Split the cookie string (i.e. copied from developer console) into a dict that can be used later"
  (into {} (for [s (s/split s #"; ")] (s/split s #"=" 2))))
