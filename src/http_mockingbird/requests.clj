(ns http-mockingbird.requests)

(defn new-request [address]
  {:address address})

(defn merge-request [address options & other-opts]
  (apply merge {:address address} options other-opts))

(defn request->headers [{:keys [headers] :as request}] headers) ;; pretty silly now, rework
