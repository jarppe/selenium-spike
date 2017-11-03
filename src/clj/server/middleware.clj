(ns server.middleware
  (:require [ring.util.http-response :as resp]))

(defn wrap-no-cache [handler]
  (fn [request]
    (when-let [response (handler request)]
      (update response :headers assoc "Cache-Control" "max-age=0,no-cache,no-store"))))
