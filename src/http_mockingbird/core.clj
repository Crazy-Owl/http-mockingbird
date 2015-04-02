(ns http-mockingbird.core
  [:require
   [org.httpkit.client :as http]
   [clojure.string :as s]])

; {:request :body :cookies}

;; cookies
(defn set-cookie [request cookie-name cookie-val]
  (assoc-in request [:cookies cookie-name] cookie-val))

(defn stringify-cookies [cookies]
  (if cookies
    (->> (for [[k v] cookies]
           [k "=" v ";"])
         (apply concat)
         (apply str))))

(defn split-cookies [s]
  (into {} (for [s (s/split s #"; ")] (s/split s #"=" 2))))

;; GET

(defn req-get [{:keys [cookies address query] :as request}]
  (let [headers (into {} (filter (fn [[k v]] v) [["Cookie" (stringify-cookies cookies)]]))
        options {:headers headers}]
    (http/get address options)))

(defn req-get-sync [& args]
  @(apply req-get args))

;; Specific GET stuff

(defn ensure-200 [addresses & {:keys [cookies] :as opts}]
  (->> (for [a addresses] [a (req-get (merge {:address a} opts))])
       (map (fn [[addr promise]]
                 (let [{:keys [status]} @promise]
                   [addr (= 200 status)])))))
