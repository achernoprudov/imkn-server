(ns imkn-server.db.comment
  (:use [imkn-server.db.utils]
        [korma.core]
        [clojure.tools.logging :only [info]])
  (:require [imkn-server.db.korma :as spec]))

(defentity comment
           (table :comment)
           (database spec/korma-db))

;;;;;;;;;;;;;;;;;;;;;;;;
;;;; Private methods ;;;
;;;;;;;;;;;;;;;;;;;;;;;;

(defn- prepare-comments
  "Preparing comments to presenting on the client side"
  [comments]
  (map #(update-in % [:date] timestamp-extr) comments))

;;;;;;;;;;;;;;;;;;;;;;;;
;;;; Public methods ;;;;
;;;;;;;;;;;;;;;;;;;;;;;;

(defn add-comment [post-id user text]
  (let [results (insert comment
                        (values {:post_id post-id :user user :text text}))]
    (assert (= (count results) 1))
    (first results)))

(defn all-comments
  ([post-id]
   (all-comments post-id 0))
  ([post-id first-result]
   (let [results (select comment
                         (fields :id :user :text :date)
                         (order :date :DESC)
                         (where {:post_id post-id})
                         (limit 10)
                         (offset first-result))]
     (prepare-comments results))))
