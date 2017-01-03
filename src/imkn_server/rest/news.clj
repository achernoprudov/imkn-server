(ns imkn-server.rest.news
  (:use [clojure.tools.logging :only [info]])
  (:require [compojure.core :as cc]
            [imkn-server.db.news :as db]
            [imkn-server.utils.serializer :as ser]))

(def all-news
  (cc/GET "/rest/news" []
    (let [result ])

    ))

(def news-by-id
  (cc/GET "/rest/news/:id" [id]
    (let [result (let [res (db/get-news id)]
                   (ser/simple-news res))]
      (info (str "item is " result))
      {:status 200 :body result})))



