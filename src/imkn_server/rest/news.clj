(ns imkn-server.rest.news
  (:use [clojure.tools.logging :only [info]])
  (:require [compojure.core :as cc]
            [imkn-server.db.news :as db]))

(def add-news
  (cc/POST "/rest/news/add" {body :body}
    (info (str "add news with body=" body))
    (db/add-news (:title body) (:text body))
    {:status 201 :body "Created"}))

(def all-news
  (cc/GET "/rest/news" [first_result]
    (let [results (db/all-news first_result)]
      {:status 200 :body results})))

(def news-by-id
  (cc/GET "/rest/news/:id" [id]
    (let [result (db/news-by-id id)]
      (info (str "item is " result))
      {:status 200 :body result})))



