(defproject imkn "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [compojure "1.5.1"]
                 [ring/ring-json "0.3.1"]
                 ;[ring-jetty/ring-ws "0.1.0-SNAPSHOT"]
                 [hiccup "1.0.2"]
                 [cljs-ajax "0.5.1"]

                 [org.clojure/tools.logging "0.2.3"]
                 [org.clojure/java.jdbc "0.6.1"]
                 [korma "0.4.3"]
                 [com.h2database/h2 "1.3.170"]
                 ; ClojureScript
                 [org.clojure/clojurescript "1.9.293"]
                 [reagent "0.6.0"]]


  :main imkn.core

  :source-paths ["src/clj" "src/cljc"]
  :resource-paths ["resources" "target/cljsbuild"]

  :clean-targets ^{:protect false} [:target-path
                                    [:cljsbuild :builds :app :compiler :output-dir]
                                    [:cljsbuild :builds :app :compiler :output-to]]

  :ring {:handler imkn.core/app
         ;:websockets {"/ws" imkn_server.chat/handler}
         }
  :profiles {:dev
             {:dependencies [[javax.servlet/servlet-api "2.5"]
                             [ring/ring-mock "0.3.0"]]}}

  :cljsbuild {:builds [{:id           "dev"
                        :source-paths ["src/cljs"]
                        :figwheel     true
                        :compiler     {:optimizations :none
                                       :asset-path    "/js/out"
                                       :output-to     "target/cljsbuild/public/js/app.js"
                                       :output-dir    "target/cljsbuild/public/js/out"
                                       :pretty-print  true
                                       :source-map    true}}]}

  :plugins [[lein-ring-jetty "0.1.0-SNAPSHOT"]
            [lein-cljsbuild "1.1.5"]
            [lein-figwheel "0.5.8"]]

  :figwheel {
             :http-server-root "public"
             :nrepl-middleware ["cemerick.piggieback/wrap-cljs-repl"]
             ;:server-port 3449
             ;:nrepl-port 7002
             ;:nrepl-middleware ["cemerick.p"]
             :css-dirs         ["resources/public/css"]
             :ring-handler     imkn.core/app
             }
  )
