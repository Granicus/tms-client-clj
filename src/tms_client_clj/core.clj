(ns tms-client-clj.core
  (:require [clj-http.client :as client]
             [clojure.data.json :as json]
             [environ.core :as env] ))

(def tms-creds
  { 
    :url   (env/env :api-root) 
    :token (env/env :auth-token)
    })


(defn build-sms-message [text phone-numbers]
  (json/write-str {:body text
                   :recipients (map (fn [tn] {:phone (str tn)}) phone-numbers)}))

(defn send-sms [text phone-numbers {:keys [token url]}]
  (let [payload {:headers {"X-AUTH-TOKEN" token}
                 :body (build-sms-message text phone-numbers)
                 :content-type :json
                 :accept :json
                 :socket-timeout 1000
                 :conn-timeout 1000}
        resp (client/post (str url "/messages/sms") payload)]
    resp))

(comment


  (send-sms "sup?" ["5555555555"] tms-creds)

)
