(ns imkn-server.db.comments
  (:use [imkn-server.db.utils]
        [korma.core]
        [clojure.tools.logging :only [info]])
  (:require [imkn-server.db.korma :as spec]))

(defentity comments
           (table :comments)
           (database spec/korma-db))

;;;;;;;;;;;;;;;;;;;;;;;;
;;;; Private methods ;;;
;;;;;;;;;;;;;;;;;;;;;;;;

(defn- prepare-comments
  "Preparing news to presenting on the client side"
  [news]
  (map #(update-in % [:date] timestamp-extr) news))

;;;;;;;;;;;;;;;;;;;;;;;;
;;;; Public methods ;;;;
;;;;;;;;;;;;;;;;;;;;;;;;

(defn add-comment [news-id user text]
  (info (str "Adding new comment for news with id=[" news-id "], by user=[" user "], text =[" text "]"))
  (let [results (insert comments
                        (values {:news_id news-id :user user :text text}))]
    (assert (= (count results) 1))
    (first results)))

(defn all-comments
  ([news-id]
   (all-comments news-id 0))
  ([news-id first-result]
   (info (str "Fech comments for news with id=[" news-id "] and first_result=[" first-result "]"))
   (let [results (select comments
                         (fields :id :user :text :date)
                         (order :date :DESC)
                         (where {:news_id news-id})
                         (limit 10)
                         (offset first-result))]
     (prepare-comments results))))
