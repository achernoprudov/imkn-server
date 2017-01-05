(ns imkn-server.utils.error)

(defn build-readable [message]
  "Build readable exception for throwing"
  (ex-info message {:readable true}))
