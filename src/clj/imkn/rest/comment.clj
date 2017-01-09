(ns imkn.rest.comment
  (:use [clojure.tools.logging :only [info]])
  (:require [compojure.core :as cc]
            [imkn.db.comment :as db]
            [imkn.utils.validator :as validator]))

(defn- add-comment [post-id user text]
  (info (str "Add comment to post with id=[" post-id "], user=[" user "], text=[" text "]"))
  (validator/validate-params-not-null {:user user :text text})
  (validator/validate-post-exist post-id)
  (db/add-comment post-id user text))

(defn- comments [post-id first-result]
  (info (str "Fech comments for post with id=[" post-id "] and first_result=[" first-result "]"))
  (validator/validate-post-exist post-id)
  (db/all-comments post-id first-result))

;; Public

(def api
  (cc/context "/posts/:post-id/comments" [post-id]
    (cc/GET "/" [first_result]
      (let [results (comments post-id first_result)]
        {:status 200 :body results}))

    (cc/POST "/add" {{user :user text :text} :body}
      (add-comment post-id user text)
      {:status 201 :body "Created"})))