(ns imkn.posts
  (:require [reagent.core :as r]
            [ajax.core :refer [GET POST]]))

(defn get-posts []
  (GET "/rest/posts" {:response-format :json}))

(def posts
  (r/atom [{:id 1}]))

(defn posts-component []
  [:div {:class "posts-component"}
   (for [post @posts]
     [:div {:class "post-item"} (:id post)])])


(get-posts)