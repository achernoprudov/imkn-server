(ns imkn-server.db.utils
  (:use [clojure.tools.logging :only [info]])
  (:require [clojure.java.jdbc :as sql]))

(def db-spec {:classname   "org.h2.Driver"
              :subprotocol "h2:file"
              :subname     "db/imkn-server"})

(defn timestamp-extr
  "Convert timestamp (Timestamp) to millis"
  [timestamp]
  (.getTime timestamp))

(defn exists?
  "Check whether a given table exists."
  [db-spec table]
  (try
    (do
      (->> (sql/query db-spec (format "select 1 from %s" table)))
      true)
    (catch Throwable ex
      false)))