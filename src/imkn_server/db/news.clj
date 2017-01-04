(ns imkn-server.db.news
  (:use [imkn-server.db.spec]
        [clojure.tools.logging :only [info]])
  (:require [clojure.java.jdbc :as sql]))

;;;;;;;;;;;;;;;;;;;;;;;
;;; Private methods ;;;
;;;;;;;;;;;;;;;;;;;;;;;

(defn- timestamp-extr
  "Convert timestamp (Timestamp) to millis"
  [timestamp]
  (.getTime timestamp))

(defn- text-extr
  ([text] text)
  ([text max-length]
   (let [length (count text)
         doTrim (> length max-length)
         trimTo (if doTrim max-length length)
         trimmed (subs text 1 trimTo)]
     (if doTrim (str trimmed "...") text))))


(defn- prepare-news
  "Preparing news to presenting on the client side"
  ([news]
   (let [update-text #(update-in % [:text] text-extr)
         update-date #(update-in % [:date] timestamp-extr)]
     (map (comp update-text update-date) news)))
  ([news max-length]
   (let [extractor #(text-extr % max-length)
         update-text #(update-in % [:text] extractor)
         update-date #(update-in % [:date] timestamp-extr)]
     (map (comp update-text update-date) news))))

;;;;;;;;;;;;;;;;;;;;;
;;; Query methods ;;;
;;;;;;;;;;;;;;;;;;;;;

(defn add-news [title text]
  (info (str "Adding news with title=" title ", text=" text))
  (let [results
        (sql/with-connection
          db-spec
          (sql/insert-record
            :news
            {:title title :text text}))]
    (assert (= (count results) 1))
    (first (vals results))))

(defn get-news [news-id]
  (info (str "Fech news with id=" news-id))
  (let [results
        (sql/with-connection
          db-spec
          (sql/with-query-results
            rs ["SELECT id, title, text, date FROM news WHERE id = ?" news-id]
            (doall (prepare-news rs))))]
    (assert (= (count results) 1))
    (first results)))

(defn get-all-news []
  (info (str "Fech all news"))
  (let [results
        (sql/with-connection
          db-spec
          (sql/with-query-results
            rs ["SELECT id, title, text, date FROM news ORDER BY date DESC"]
            (doall (prepare-news rs 200))))]
    results))


