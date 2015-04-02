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

(defn ensure-200 [addresses & {:keys [cookies] :as opts}]
  (->> (for [a addresses] [a (req-get (merge (r/new-request a) opts))])
       (map (fn [[addr promise]]
                 (let [{:keys [status]} @promise]
                   [addr (= 200 status)])))))

(defn get-addresses [addresses & {:keys [cookies] :as opts}]
  (for [a addresses] [a (req-get (merge (r/new-request a) opts))]))
