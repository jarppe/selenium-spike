(ns server.system
  (:require [clojure.tools.logging :as log]
            [immutant.web :as immutant]
            [server.handler :as handler]))

(defonce server (atom nil))

(defn stop-server [server]
  (when server
    (immutant/stop server))
  nil)

(defn start-server [old-server]
  (stop-server old-server)
  (immutant/run #'handler/handler {:host "0.0.0.0", :port 3000, :path "/"}))

(defn start []
  (log/info "starting http server to port 3000...")
  (swap! server start-server)
  :running)

(defn stop []
  (swap! server stop-server))

(comment
  (start)
  )