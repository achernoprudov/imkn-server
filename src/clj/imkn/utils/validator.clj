(ns imkn.utils.validator
  (:require [imkn.db.post :as db]
            [imkn.utils.error :as error]
            [clojure.string :as str]))

(defn- validate-param-value [map-entry]
  (let [key (first map-entry)
        value (second map-entry)]
    (if (str/blank? value)
      (throw (error/build-readable (format "'%s; parameter is required" (name key)))))))

(defn validate-post-exist [post-id]
  (if (not (db/post-exist? post-id))
    (throw (error/build-readable (str "Post with id = " post-id " does not exist")))))

(defn validate-params-not-null [value-map]
  (map validate-param-value (seq value-map)))