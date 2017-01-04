(ns imkn-server.db.utils)

(def db-spec {:classname   "org.h2.Driver"
              :subprotocol "h2:file"
              :subname     "db/imkn-server"})

(defn timestamp-extr
  "Convert timestamp (Timestamp) to millis"
  [timestamp]
  (.getTime timestamp))