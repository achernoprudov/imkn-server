(ns imkn-server.core
  (:require [compojure.core :as cc]
            [compojure.route :as route]
            [compojure.handler :as handler]
            [ring.middleware.json :as middleware]
            [imkn-server.rest.news :as rest-news]))

(cc/defroutes app-routes
              (cc/GET "/" [] "He;")

              ; REST API
              rest-news/add-news
              rest-news/all-news
              rest-news/news-by-id

              (route/not-found "Not Found"))

(def app
  (-> (handler/site app-routes)
      (middleware/wrap-json-body {:keywords? true})
      middleware/wrap-json-response))

