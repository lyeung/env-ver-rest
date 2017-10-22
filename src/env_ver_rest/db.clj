(ns env-ver-rest.db
  (:import com.mchange.v2.c3p0.ComboPooledDataSource)
  (:require [config.core :refer [env]]))

(def config
  {:classname (:db-classname env)
   :subprotocol (:db-subprotocol env)
   :subname (:db-subname env)
   :user (:db-user env)
   :password (:db-password env)})

(defn pool [config]
  (let [cpds (doto (ComboPooledDataSource.)
               (.setDriverClass (:classname config))
               (.setJdbcUrl (str "jdbc:"
                                 (:subprotocol config)
                                 ":"
                                 (:subname config)))
               (.setUser (:user config))
               (.setPassword (:password config))
               (.setMaxPoolSize 6)
               (.setMinPoolSize 1)
               (.setInitialPoolSize 1))]
    {:datasource cpds}))

(def pooled-db
  (delay (pool config)))

(defn db-conn[]  @pooled-db)

