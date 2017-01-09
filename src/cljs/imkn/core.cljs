(ns imkn.core
  (:require [reagent.core :as r]
            [imkn.posts :as posts]))

(def click-count (r/atom 0))

(defn counting-component []
  [:div
   "The atom " [:code "click-count"] " has value: "
   @click-count ". "
   [:input {:type     "button" :value "Click me!"
            :on-click #(swap! click-count inc)}]])

(defn app []
  [:div [:h1 {:class "title"} "Hello from clj"]
   (counting-component)
   (posts/posts-component)
   ])

(r/render [app] (js/document.querySelector "#app"))