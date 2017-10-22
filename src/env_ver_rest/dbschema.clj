(ns env-ver-rest.dbschema
  (:require [clojure.java.jdbc :as sql]))

(defn init [db-conn]
  (sql/with-connection (db-conn)
    (sql/create-table :env_ver
                      [:id "varchar(256)" "primary key"]
                      [:name "varchar(1024)"]
                      [:version "varchar(3)"]
                      [:status "varchar(30)"]
                      [:last_known "varchar(100)"])))

