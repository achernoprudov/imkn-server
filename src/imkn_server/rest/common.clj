(ns imkn-server.rest.common
  (:require [compojure.route :as route]))

(defn not-found
  (route/not-found {:status 404
                    :body {:message "Not found"}}))
