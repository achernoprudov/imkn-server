(ns imkn-server.db.post
  (:use [imkn-server.db.utils]
        [korma.core]
        [clojure.tools.logging :only [info]])
  (:require [imkn-server.db.korma :as spec]))

(defentity post
           (table :post)
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
         trimmed (subs text 0 trimTo)]
     (if doTrim (str trimmed "...") text))))

(defn- prepare-posts
  "Preparing posts to presenting on the client side"
  ([posts]
   (let [update-text #(update-in % [:text] text-extr)
         update-date #(update-in % [:date] timestamp-extr)]
     (map (comp update-text update-date) posts)))
  ([posts max-length]
   (let [extractor #(text-extr % max-length)
         update-text #(update-in % [:text] extractor)
         update-date #(update-in % [:date] timestamp-extr)]
     (map (comp update-text update-date) posts))))

;;;;;;;;;;;;;;;;;;;;;
;;; Query methods ;;;
;;;;;;;;;;;;;;;;;;;;;

(defn post-exist? [post-id]
  "Check whether posts with id exists"
  (try
    (do
      (->> (= 1 (count
                  (select post
                          (fields :id)
                          (where {:id post-id}))))))
    (catch Throwable ex
      false)))

(defn add-post [title text]
  (insert post
          (values {:title title :text text})))

(defn post-by-id [id]
  (info (str "Fech posts with id=" id))
  (let [results
        (select post
                (fields :id :title :text :date)
                (where {:id id}))]
    (assert (= (count results) 1))
    (first (prepare-posts results))))

(defn all-posts
  ([] (all-posts 0))
  ([first-result]
   (info (str "Fech all posts. first_result=" first-result))
   (let [results
         (select post
                 (fields :id :title :text :date)
                 (order :date :DESC)
                 (limit 100)
                 (offset first-result))]
     (prepare-posts results 200))))
