(ns tms-client-clj.core-test
  (:require [clojure.test :refer :all]
            [tms-client-clj.core :refer :all]
            [environ.core :as env]))

(deftest env-credentials
  (testing "credentials are set"
    (is (string? (env/env :auth-token)))
    (is (string? (env/env :api-root)))))
