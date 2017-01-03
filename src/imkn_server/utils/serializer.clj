(ns imkn-server.utils.serializer)

(defn- trim-text [text-value]
  (def text (str text-value))
  (if (> (count text) 23)
    (str (subs text 0 20) "...")
    text))

(defn simple-news [news]
  (update-in news [:text] trim-text))

