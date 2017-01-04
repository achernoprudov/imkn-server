(ns imkn-server.rest.news
  (:use [clojure.tools.logging :only [info]])
  (:require [compojure.core :as cc]
            [imkn-server.db.news :as db]
            [imkn-server.utils.serializer :as serializer]))

;(def all-news
;  (cc/GET "/rest/news" []
;    (let [results (map serializer/simple-news (db/get-all-news))]
;      (info (str "item is " results))
;      {:status 200 :body results})))

(def all-news
  (cc/GET "/rest/news" []
    (let [results (db/get-all-news)]
      (info (str "item is " results))
      {:status 200 :body results})))

(def news-by-id
  (cc/GET "/rest/news/:id" [id]
    (let [result (db/get-news id)]
      (info (str "item is " result))
      {:status 200 :body result})))



