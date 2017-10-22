(defproject env-ver-rest "0.1.0-SNAPSHOT"
  :description "env-ver REST backend"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [compojure "1.5.1"]
;;                 [ring/ring-defaults "0.2.1"]
                 [ring/ring-json "0.1.2"]
                 [cheshire "4.0.3"]
                 [c3p0/c3p0 "0.9.1.2"]
                 [org.clojure/java.jdbc "0.2.3"]
                 [com.h2database/h2 "1.3.168"]
                 [yogthos/config "0.9"]
                 [com.taoensso/carmine "2.16.0"]]
  :plugins [[lein-ring "0.9.7"]]
  :main env-ver-rest.core
  :ring {:handler env-ver-rest.handler/app}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring/ring-mock "0.3.0"]]
         :plugins [[com.jakemccrary/lein-test-refresh "0.21.1"]]
         :resource-paths ["config/dev"]}
   :test {:resource-paths ["config/test"]}
   :prod {:resource-paths ["config/prod"]}
   })
