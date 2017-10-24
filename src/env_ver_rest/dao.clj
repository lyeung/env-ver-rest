(ns env-ver-rest.dao
  (:require [ring.util.response :refer [response]]
            [clojure.java.jdbc :as sql]))

(defn uuid[] (str (java.util.UUID/randomUUID)))

(defn find-env-ver [id dbconn]
  (sql/with-connection (dbconn)
    (sql/with-query-results results
      ["select * from env_ver where id=?" id]
      (cond
        (empty? results) {:status 404}
        :else (response (first results))))))

(defn create-env-ver [body dbconn]
  (let [id (uuid)]
    (sql/with-connection (dbconn)
      (let [record (assoc body "id" id)]
        (sql/insert-record :env_ver record)))
    (find-env-ver id dbconn)))

(defn find-env-ver-by-name [name dbconn]
  (sql/with-connection (dbconn)
    (sql/with-query-results results
      ["select * from env_ver where name=?" name]
      (cond
        (empty? results) {:status 404}
        :else (response (first results))))))

(defn find-all [dbconn]
  (response
  (sql/with-connection (dbconn)
    (sql/with-query-results results
      ["select * from env_ver"]
;;      (println results)
        (into [] results)))))

(defn delete-env-ver [name dbconn]
  (sql/with-connection (dbconn)
    (sql/delete-rows :env_ver ["name=?" name]))
  {:status 204})

(defn update-env-ver [name body dbconn]
  (sql/with-connection (dbconn)
    (sql/update-values :env_ver ["name=?" name] body))
  (find-env-ver name dbconn))

(defn delete-all [dbconn]
  (sql/with-connection (dbconn)
    (sql/delete-rows :env_ver ["id in (select id from env_ver)"])))

(defn drop-schema [dbconn]
  (sql/with-connection (dbconn)
    (sql/drop-table :env_ver)))
