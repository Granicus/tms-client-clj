(ns tms-client-clj.core
  (:require [clj-http.client :as client]
            [clojure.data.json :as json]))

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

(comment

  (def url "https://stage-tms.govdelivery.com")
  (def token "api-token")
  (def stage {:url url :token token})

  (send-sms "sup?" ["5555555555"] stage)

)
