(ns imkn-server.rest.comments
  (:use [clojure.tools.logging :only [info]])
  (:require [compojure.core :as cc]
            [imkn-server.db.comments :as db]))

(def add-comment
  (cc/POST "/rest/news/:id/comments/add" {{id :id} :params {user :user text :text} :body}                     ;[news-id {body :body}]
    (info (str "add news with id=[" id "], user=[" user "], text=[" text "]"))
    (db/add-comment id user text)
    {:status 201 :body "Created"}))

(def comments
  (cc/GET "/rest/news/:id/comments" [id first_result]                          ;[id first_result]
    (let [results (db/all-comments id first_result)]
      {:status 200 :body results})))