(ns http-mockingbird.requests.post
  (:require [org.httpkit.client :as http]
            [clojure.data.json :as json]
            [http-mockingbird.cookies :as cookies]
            [http-mockingbird.requests :as r]
            [http-mockingbird.utils :as u]))

(defn req-post [{:keys [cookies address form] :as request}]
  (let [headers (into {} (filter (fn [[k v] v]) [["Cookie" (cookies/stringify-cookies cookies)]]))
        options {:headers headers :form-params form}]
    (http/post address options)))

(defn req-post-sync [& args]
  @(apply req-post args))

(defn data-from-post [request path]
  "Don't forget to prepend :body or :headers to the path"
  (u/get-path (assoc request :body (json/read-str (:body request))) path))

(defn ensure-post-200 [address-map & {:keys [cookies] :as opts}]
  (->> (for [[a fm] address-map] [a (req-post (merge (r/new-request a) {:form fm} opts))])
       (map (fn [[addr promise]]
              (let [{:keys [status]} @promise]
                [addr (= 200 status)])))))
