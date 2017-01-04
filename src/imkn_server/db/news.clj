(ns imkn-server.db.news
  (:use [imkn-server.db.spec]
        [clojure.tools.logging :only [info]])
  (:require [clojure.java.jdbc :as sql]))

;;;;;;;;;;;;;;;;;;;;;;;
;;; Private methods ;;;
;;;;;;;;;;;;;;;;;;;;;;;

(defn- clob-extr
  "Extract string from clob object (JdbcClob).
  If max-length is defined - trim to max-length and add '...' symbols.
  If max-length is not defined - return whole string."
  ([clob] (clob-extr clob (.length clob)))
  ([clob max-length]
   (let [length (.length clob)
         doTrim (> length max-length)
         trimTo (if doTrim max-length length)
         trimmed (.getSubString clob 1 trimTo)]
     (if doTrim (str trimmed "...") trimmed))))

(defn- prepare-news
  "Preparing news to presenting on the client side"
  ([news]
   (map #(update-in % [:text] clob-extr) news))
  ([news max-length]
   (let [extractor #(clob-extr % max-length)]
     (map #(update-in % [:text] extractor) news))))

;;;;;;;;;;;;;;;;;;;;;
;;; Query methods ;;;
;;;;;;;;;;;;;;;;;;;;;

(defn add-news [title text date]
  (info (str "Adding news with title=" title))
  (let [results
        (sql/with-connection
          db-spec
          (sql/insert-record
            :news
            {:title title :text text :date date}))]
    (assert (= (count results) 1))
    (first (vals results))))

(defn get-news [news-id]
  (info (str "Fech news with id=" news-id))
  (let [results
        (sql/with-connection
          db-spec
          (sql/with-query-results
            rs ["select id, title, text, date from news where id = ?" news-id]
            (doall (prepare-news rs))))]
    (assert (= (count results) 1))
    (first results)))

(defn get-all-news []
  (let [results
        (sql/with-connection
          db-spec
          (sql/with-query-results
            rs ["select id, title, date text from news"]
            (doall (prepare-news rs 200))))]
    results))


