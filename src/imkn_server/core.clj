(ns imkn-server.core
  (:require [compojure.core :as cc]
            [compojure.route :as route]
            [compojure.handler :as handler]
            [ring.middleware.json :as middleware]
            [imkn-server.rest.post :as post-api]
            [imkn-server.rest.comment :as comment-api]
            [clojure.tools.logging :as log])
  (:import (clojure.lang ExceptionInfo)))

(cc/defroutes app-routes
              (cc/GET "/" [] "Main")

              ; Posts REST API
              post-api/posts
              post-api/add-post
              post-api/post-by-id

              ; Comments REST API
              comment-api/comments
              comment-api/add-comment

              (route/not-found "Not Found"))

(defn- inrenal-error-response
  ([error] (inrenal-error-response error "Internal server error"))
  ([error message]
   (log/error error message)
   {:status 500 :body {:message message}}))

(defn- wrap-fallback-exception
  [handler]
  (fn [request]
    (try
      (handler request)
      (catch ExceptionInfo exception
        (if (:readable (ex-data exception))
          (inrenal-error-response exception (.getMessage exception))
          (inrenal-error-response exception)))

      (catch Exception exception
        (inrenal-error-response exception)))))

(def app
  (->
    (handler/site app-routes)
    (middleware/wrap-json-body {:keywords? true})
    wrap-fallback-exception
    middleware/wrap-json-response))

