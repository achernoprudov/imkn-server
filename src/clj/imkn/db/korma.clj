(ns imkn.db.korma
  (:require [korma.db :as kdb]
            [clojure.string :as str]))

(def korma-spec
  (kdb/h2
    {:db         "db/imkn-server"
     :delimiters ""
     :naming     {:fields str/lower-case
                  :keys   str/lower-case}}))

(kdb/defdb korma-db korma-spec)