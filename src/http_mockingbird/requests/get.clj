(ns http-mockingbird.requests.get
  (:require [http-mockingbird.cookies :as cookies]
            [http-mockingbird.requests :as r]
            [org.httpkit.client :as http]))

(defn req-get [{:keys [cookies address query] :as request}]
  (let [headers (into {} (filter (fn [[k v]] v) [["Cookie" (cookies/stringify-cookies cookies)]]))
        options {:headers headers}]
    (http/get address options)))

(defn req-get-sync [& args]
  @(apply req-get args))

(defn get-addresses [addresses & opts]
  (->> (for [a addresses] [a (req-get (r/merge-request a (apply hash-map opts)))])
       (map (fn [[a b]] [a @b]))
       (doall)))

(defn ensure-200 [addresses & opts]
  (->> (get-addresses addresses)
       (map (fn [[a b]] [a (= 200 (:status b))]))))

(defn get-data [address-map & opts]
  (->> (for [[a q] address-map] [a (req-get (r/merge-request a {:query q} (apply hash-map opts)))])
       (map (fn [[a b]] [a @b]))
       (doall)))
