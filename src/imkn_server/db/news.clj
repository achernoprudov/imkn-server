(ns imkn-server.db.news
  (:use [imkn-server.db.spec]
        [clojure.tools.logging :only [info]])
  (:require [clojure.java.jdbc :as sql]))

(defn add-news
  [title text]
  (let [results (sql/with-connection db-spec
                                     (sql/insert-record :news
                                                        {:title title :text text}))]
    (assert (= (count results) 1))
    (first (vals results))))

(defn get-news
  [news-id]
  (info (str "Fech news with id=" news-id))
  (let [results
        (sql/with-connection db-spec
                             (sql/with-query-results res
                                                     ["select title, text from news where id = ?" news-id]
                                                     (doall res)))]
    (assert (= (count results) 1))
    (first results)))

(defn get-all-news
  []
  (let [results (sql/with-connection db-spec
                                     (sql/with-query-results res
                                                             ["select id, title, text from news"]
                                                             (doall res)))]
    results))