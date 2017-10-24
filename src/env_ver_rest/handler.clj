(ns env-ver-rest.handler
  (:import com.mchange.v2.c3p0.ComboPooledDataSource)
  (:require [compojure.core :refer :all]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [clojure.java.jdbc :as sql]
            [env-ver-rest.dbschema :as dbschema]
            [env-ver-rest.db :as db]
            [env-ver-rest.dao :as dao]
            [taoensso.carmine :as car :refer (wcar)]
            ;;            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.middleware.json :as middleware]
            [ring.util.response :refer [response]]))

(dbschema/init db/db-conn)

(defroutes app-routes
  (context "/env-vers" []
           (defroutes env-ver-routes
             (GET "/" [] (dao/find-all db/db-conn))
             (POST "/" {body :body} (dao/create-env-ver body db/db-conn))
             (context "/:env-name" [env-name]
                      (defroutes env-ver-route
                        (GET "/" [] (dao/find-env-ver-by-name env-name db/db-conn))
                        (PUT "/" {body :body}
                             (dao/update-env-ver env-name body db/db-conn))
                        (DELETE "/" [] (dao/delete-env-ver env-name db/db-conn))))))
  (route/not-found "Not Found"))
                        
;  (GET "/" [] "Hello World")
;  (route/not-found "Not Found"))

(def app
;;  (wrap-defaults app-routes site-defaults))
  (-> (handler/api app-routes)
      (middleware/wrap-json-body)
      (middleware/wrap-json-response)))
  
