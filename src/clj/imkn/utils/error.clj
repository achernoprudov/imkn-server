(ns imkn.utils.error
  (:require [clojure.tools.logging :as log])
  (:import (clojure.lang ExceptionInfo)))

(defn build-readable [message]
  "Build readable exception for throwing"
  (ex-info message {:readable true}))


(defn- inrenal-error-response
  ([error] (inrenal-error-response error "Internal server error"))
  ([error message]
   (log/error error message)
   {:status 500 :body {:message message}}))

(defn wrap-fallback-exception
  [handler]
  (fn [request]
    (try
      (handler request)
      (catch ExceptionInfo exception
        (if (:readable (ex-data exception))
          (inrenal-error-response exception (.getMessage exception))
          (inrenal-error-response exception)))

      (catch Exception exception
        (inrenal-error-response exception)))))