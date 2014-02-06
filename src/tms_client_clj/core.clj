(ns tms-client-clj.core
  (:require [clj-http.client :as client]
             [clojure.data.json :as json]
             [environ.core :as env] ))

(def tms-creds
  { :auth-token (env/env :auth-token)
    :api-root   (env/env :api-root) })


(defn foo
  "I don't do a whole lot."
  [x]
  (println x "Hello, World!"))
