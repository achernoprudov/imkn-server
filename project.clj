(defproject imkn-server "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [compojure "1.5.1"]
                 [ring/ring-json "0.3.1"]
                 [ring-jetty/ring-ws "0.1.0-SNAPSHOT"]
                 [hiccup "1.0.2"]
                 [org.clojure/tools.logging "0.2.3"]
                 [org.clojure/java.jdbc "0.6.1"]
                 [korma "0.4.3"]
                 [com.h2database/h2 "1.3.170"]]
  :plugins [[lein-ring-jetty "0.1.0-SNAPSHOT"]]
  :ring {:handler imkn-server.core/app
         ;:websockets {"/ws" imkn_server.chat/handler}
         }
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring/ring-mock "0.3.0"]]}})
