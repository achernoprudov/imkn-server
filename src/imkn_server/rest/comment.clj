(ns imkn-server.rest.comment
  (:use [clojure.tools.logging :only [info]])
  (:require [compojure.core :as cc]
            [clojure.string :as str]
            [imkn-server.db.comment :as db]
            [imkn-server.db.post :as post-db]
            [imkn-server.utils.error :as error]))

(def add-comment
  (cc/POST "/rest/posts/:id/comments/add" {{id :id} :params {user :user text :text} :body}
    (info (str "Add comment to post with id=[" id "], user=[" user "], text=[" text "]"))
    (if (str/blank? user)
      (throw (error/build-readable "'user' parameter is required")))
    (if (str/blank? text)
      (throw (error/build-readable "'text' parameter is required")))
    (if (not (post-db/post-exist? id))
      (throw (error/build-readable (str "Post with id = " id " does not exist"))))
    (db/add-comment id user text)
    {:status 201 :body "Created"}))

(def comments
  (cc/GET "/rest/posts/:id/comments" [id first_result]
    (if (not (post-db/post-exist? id))
      (throw (error/build-readable (str "Post with id = " id " does not exist"))))
    (let [results (db/all-comments id first_result)]
      {:status 200 :body results})))