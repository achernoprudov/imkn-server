(ns imkn.core
  (:require [compojure.core :as cc]
            [compojure.route :as route]
            [compojure.handler :as handler]
            [ring.middleware.json :as middleware]
            [hiccup.page :refer [include-js include-css html5]]

            [imkn.utils.error :as error]
            [imkn.rest.post :as posts]
            [imkn.rest.comment :as comments]))

(def mount-target
  [:div#app
   [:h3 "ClojureScript has not been compiled!"]
   [:p "please run "
    [:b "lein figwheel"]
    " in order to start the compiler"]])

(defn head []
  [:head
   [:meta {:charset "utf-8"}]
   [:meta {:name "viewport"
           :content "width=device-width, initial-scale=1"}]
   (include-css "css/site.css")])

(defn loading-page []
  (html5
    (head)
    [:body {:class "body-container"}
     mount-target
     (include-js "js/out/goog/base.js")
     (include-js "js/app.js")
     [:script "goog.require('imkn.core')"]]))

(cc/defroutes app-routes
              (cc/GET "/" [] (loading-page))

              (cc/context "/rest" []
                posts/api
                comments/api
                (route/not-found {:status 404
                                  :body   {:message "Not found"}}))

              (route/not-found "Not Found"))

(def app
  (->
    (handler/site app-routes)
    (middleware/wrap-json-body {:keywords? true})
    error/wrap-fallback-exception
    middleware/wrap-json-response))
