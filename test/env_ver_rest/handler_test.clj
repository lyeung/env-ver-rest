(ns env-ver-rest.handler-test
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [cheshire.core :as cheshire]
            [env-ver-rest.handler :refer :all]
            [ring.util.response :refer [response]]))

(defn fixtures [test-fn]
  (test-fn)
  (delete-all))

(use-fixtures :each fixtures)

(deftest test-get-env-vers-routes
  (testing "test get /env-vers"
    (let [response (app (mock/request :get "/env-vers"))]
      (is (= (:status response) 200))
      (is (= (:body response) "[]")))))

(deftest test-post-env-vers-routes
  (testing "test post /env-vers"
    (let [response (app (->
                         (mock/request :post "/env-vers")
                         (mock/content-type "application/json")
                         (mock/body (cheshire/generate-string
                                     {:name "qa"
                                      :version "10"
                                      :status "ok"
                                      :last_known "x1"}))))]
      (is (= (:status response) 200)))))
