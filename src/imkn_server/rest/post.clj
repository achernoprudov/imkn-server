(ns imkn-server.rest.post
  (:use [clojure.tools.logging :only [info]])
  (:require [compojure.core :as cc]
            [imkn-server.db.post :as db]
            [imkn-server.utils.validator :as validator]))

(defn- add-post [title text]
  (info (str "Add post with title=" title ", text=" text))
  (validator/validate-params-not-null {:title title :text text})
  (db/add-post title text))

(defn- all-posts [first-result]
  (info (str "Fetch all posts. first_result=" first-result))
  (db/all-posts first-result))

(defn- post-by-id [id]
  (info (str "Fetch post with id=" id))
  (validator/validate-post-exist id)
  (db/post-by-id id))

;; Posts context

(def api
  (cc/context "/posts" []
    (cc/GET "/" [first_result]
      (let [results (all-posts first_result)]
        {:status 200 :body results}))

    (cc/GET "/:id" [id]
      (let [post (post-by-id id)]
        {:status 200 :body post}))

    (cc/POST "/add" {{title :title text :text} :body}
      (add-post title text)
      {:status 201 :body "Created"})))



