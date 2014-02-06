(ns tms-client-clj.core
  (:require [clj-http.client :as client]
            [clojure.data.json :as json]))

(defn links [coll]
  (if (map? coll)
    [(:_links coll)]
    (map :_links coll)))

(defn req1 [{:keys [token url]}]
  (let [payload {:headers {"X-AUTH-TOKEN" token}
                 :content-type :json
                 :accept :json
                 :socket-timeout 1000
                 :conn-timeout 1000}
        resp (client/get url payload)
        json-str (:body resp)
        resp-data (json/read-str json-str :key-fn keyword)]
    resp))

(defn reqs [env link-fns]
  (let [resp-data (req1 env)]
    (if (empty? link-fns)
      resp-data
      (let [new-env (update-in env [:url] str ((first link-fns) resp-data))]
        (recur new-env (rest link-fns))))))

(defn post-test [url token the-msg]
  (try
    (client/post url
                 {:body the-msg
                  :headers {"X-AUTH-TOKEN" token}
                  :content-type :json
                  :accept :json
                  :socket-timeout 1000
                  :conn-timeout 1000})
    (catch clojure.lang.ExceptionInfo e
      {:status (-> e ex-data :object)})
    (catch Exception e {:status e})))

(defn build-sms-message [text phone-numbers]
  (json/write-str {:headers {"X-AUTH-TOKEN" (:token stage)}
                   :body "You've been Clojured"
                   :recipients (map (fn [tn] {:phone (str tn)}) phone-numbers)}))

(defn send-sms [text phone-numbers {:keys [token url]}]
  (let [payload {:headers {"X-AUTH-TOKEN" (:token stage)}
                 :body (build-sms-message text phone-numbers)
                 :content-type :json
                 :accept :json
                 :socket-timeout 1000
                 :conn-timeout 1000}
        resp (client/post (str url "/messages/sms") payload)]
    resp))
