(ns imkn-server.db.news
  (:use [imkn-server.db.utils]
        [korma.core]
        [clojure.tools.logging :only [info]])
  (:require [imkn-server.db.korma :as spec]))

;;; Vars

(defentity news
           (table :news)
           (database spec/korma-db))

;;;;;;;;;;;;;;;;;;;;;;;
;;; Private methods ;;;
;;;;;;;;;;;;;;;;;;;;;;;

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
  (insert news
          (values {:title title :text text})))

(defn news-by-id [news-id]
  (info (str "Fech news with id=" news-id))
  (let [results
        (select news
                (fields :id :title :text :date)
                (where {:id news-id}))]
    (assert (= (count results) 1))
    (first (prepare-news results))))

(defn all-news
  ([] (all-news 0))
  ([first-result]
   (info (str "Fech all news with first-result=" first-result))
   (let [results (select news
                         (fields :id :title :text :date)
                         (order :date :DESC)
                         (limit 10)
                         (offset first-result))]
     (prepare-news results 200))))


