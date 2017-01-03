(ns imkn-server.handler
  (:require [compojure.core :as cc]
            [compojure.route :as route]
            [compojure.handler :as handler]))

(cc/defroutes app-routes
   (cc/GET "/" [] "Nice")
             )

(def app
  (handler/site app-routes))