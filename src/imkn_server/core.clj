(ns imkn-server.core
  (:require [compojure.core :as cc]
            [compojure.route :as route]
            [compojure.handler :as handler]
            [clojure.tools.logging :as log]
            [ring.middleware.json :as middleware]

            [imkn-server.rest.post :as posts]
            [imkn-server.rest.common :as common]
            [imkn-server.rest.comment :as comments])
  (:import (clojure.lang ExceptionInfo)))

(cc/defroutes app-routes
              (cc/GET "/" [] "Main")

              (cc/context "/rest" []
                posts/api
                comments/api
                common/not-found)

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

