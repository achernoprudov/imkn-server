(ns imkn-server.utils.serializer
  (:use [clojure.tools.logging :only [info]]))

(defn- extract-text [clob]
  ;(info (str "clob class - " (class (.getCharacterStream clob))))
  (str clob))

(defn- trim-text [text-value]
  (let [text (extract-text text-value)]
    (if (> (count text) 103)
      (str (subs text 0 100) "...")
      text)))

(defn simple-news [news]
  (update-in news [:text] trim-text))

