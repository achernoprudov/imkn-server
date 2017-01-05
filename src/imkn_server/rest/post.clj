(ns imkn-server.rest.post
  (:use [clojure.tools.logging :only [info]])
  (:require [compojure.core :as cc]
            [imkn-server.db.post :as db]
            [imkn-server.utils.error :as error]
            [clojure.string :as str]))

(def add-post
  (cc/POST "/rest/posts/add" {{title :title text :text} :body}
    (info (str "Add post with title=" title ", text=" text))
    (if (str/blank? title)
      (throw (error/build-readable "'title' parameter is required")))
    (if (str/blank? text)
      (throw (error/build-readable "'text' parameter is required")))
    (db/add-post title text)
    {:status 201 :body "Created"}))

(def posts
  (cc/GET "/rest/posts" [first_result]
    (info (str "Fetch all posts. first_result=" first_result))
    (let [results (db/all-posts first_result)]
      {:status 200 :body results})))

(def post-by-id
  (cc/GET "/rest/posts/:id" [id]
    (info (str "Fetch post with id=" id))
    (if (not (db/post-exist? id))
      (throw (error/build-readable (str "Post with id = " id " does not exist"))))
    (let [result (db/post-by-id id)]
      {:status 200 :body result})))



