(ns env-ver-rest.handler
  (:import com.mchange.v2.c3p0.ComboPooledDataSource)
  (:require [compojure.core :refer :all]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [clojure.java.jdbc :as sql]
            [env-ver-rest.dbschema :as dbschema]
            [env-ver-rest.db :as db]
            [taoensso.carmine :as car :refer (wcar)]
            ;;            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.middleware.json :as middleware]
            [ring.util.response :refer [response]]))


(defn uuid[] (str (java.util.UUID/randomUUID)))
(dbschema/init db/db-conn)

(defn find-env-ver [id]
  (sql/with-connection (db/db-conn)
    (sql/with-query-results results
      ["select * from env_ver where id=?" id]
      (cond
        (empty? results) {:status 404}
        :else (response (first results))))))

(defn find-env-ver-by-name [name]
  (sql/with-connection (db/db-conn)
    (sql/with-query-results results
      ["select * from env_ver where name=?" name]
      (cond
        (empty? results) {:status 404}
        :else (response (first results))))))


(defn create-env-ver [body]
  (let [id (uuid)]
    (println (str "body" body))
    (sql/with-connection (db/db-conn)
      (let [record (assoc body "id" id)]
        (sql/insert-record :env_ver record)))
    (find-env-ver id)))

(defn update-env-ver [name body]
  (sql/with-connection (db/db-conn)
    (sql/update-values :env_ver ["name=?" name] body))
  (find-env-ver name))

(defn find-all []
  (response
  (sql/with-connection (db/db-conn)
    (sql/with-query-results results
      ["select * from env_ver"]
;;      (println results)
        (into [] results)))))

(defn delete-env-ver [name]
  (sql/with-connection (db/db-conn)
    (sql/delete-rows :env_ver ["name=?" name]))
  {:status 204})

(defn delete-all []
  (sql/with-connection (db/db-conn)
    (sql/delete-rows :env_ver ["id in (select id from env_ver)"])))

(defn drop-schema []
  (sql/with-connection (db/db-conn)
    (sql/drop-table :env_ver)))

(defroutes app-routes
  (context "/env-vers" []
           (defroutes env-ver-routes
             (GET "/" [] (find-all))
             (POST "/" {body :body} (create-env-ver body))
             (context "/:env-name" [env-name]
                      (defroutes env-ver-route
                        (GET "/" [] (find-env-ver-by-name env-name))
                        (PUT "/" {body :body}
                             (update-env-ver env-name body))
                        (DELETE "/" [] (delete-env-ver env-name))))))
  (route/not-found "Not Found"))
                        
;  (GET "/" [] "Hello World")
;  (route/not-found "Not Found"))

(def app
;;  (wrap-defaults app-routes site-defaults))
  (-> (handler/api app-routes)
      (middleware/wrap-json-body)
      (middleware/wrap-json-response)))
  
