(ns imkn-server.db.creator
  (:require [clojure.java.jdbc :as sql])
  (:use [imkn-server.db.utils]
        [clojure.tools.logging :only [info]])
  )

(defn exists?
  "Check whether a given table exists."
  [db-spec]
  (try
    (do
      (->> (sql/query db-spec "select 1 from news"))
      true)
    (catch Throwable ex
      false)))

(defn- create-news-table []
  (sql/create-table-ddl :news
                        [:id "bigint primary key auto_increment"]
                        [:title "varchar"]
                        [:text "longvarchar"]
                        [:date "timestamp default CURRENT_TIMESTAMP()"]))

(defn- create-comments-table []
  (sql/create-table-ddl :comments
                        [:id "bigint primary key auto_increment"]
                        [:user "varchar"]
                        [:text "longvarchar"]
                        [:related_news :serial "references news (id)"]
                        [:date "timestamp default CURRENT_TIMESTAMP()"]))

(defn- create-tables []
  (create-news-table)
  (create-comments-table))

(defn create []
  (when-not (exists? db-spec)
    (create-tables)))
  ;(sql/with-db-connection
  ;  db-spec
  ;
  ;  []))