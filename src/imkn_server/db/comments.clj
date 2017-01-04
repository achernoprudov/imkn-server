(ns imkn-server.db.comments
  (:use [imkn-server.db.utils]
        [clojure.tools.logging :only [info]])
  (:require [clojure.java.jdbc :as sql]))

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
  (let [results
        (sql/insert! db-spec :comments {:news-id news-id :user user :text text})]
    (assert (= (count results) 1))
    (first results)))

(defn all-comments [news-id]
  (info (str "Fech all news"))
  (let [results (sql/query db-spec ["SELECT id, user, text, date FROM comments ORDER BY date DESC where news-id = ?" news-id])]
    (doall results)))
