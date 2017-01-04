(ns imkn-server.rest.comments
  (:use [clojure.tools.logging :only [info]])
  (:require [compojure.core :as cc]
            [imkn-server.db.comments :as db]))

(def add-comment
  (cc/POST "/rest/news/:id/comments/add" req                    ;[news-id {body :body}]
    (info (str "add news with body=" req))
    ;(db/add-comment (:title body) (:text body))
    {:status 201 :body "Created"}))

(def comments
  (cc/GET "/rest/news/:id/comments" [news-id]
    (let [results (db/all-comments news-id)]
      {:status 200 :body results})))