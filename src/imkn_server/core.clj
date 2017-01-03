(ns imkn-server.core
  (:require [compojure.core :as cc]
            [compojure.route :as route]
            [compojure.handler :as handler]
            [ring.middleware.json :as middleware]
            [imkn-server.rest.news :as rest-news]))

(cc/defroutes app-routes
              (cc/GET "/" [] "He;")

              ; REST API
              rest-news/news-by-id
              rest-news/all-news

              (route/not-found "Not Found"))

(def app
  (-> (handler/site app-routes)
      (middleware/wrap-json-body {:keywords? true})
      middleware/wrap-json-response))

