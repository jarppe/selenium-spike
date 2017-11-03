(ns server.main
  (:require [server.system :as system])
  (:gen-class))

(defn -main [& args]
  (System/setProperty "org.jboss.logging.provider" "slf4j")
  (try
    (system/start)
    (catch Throwable e
      (.println System/err "unexpected error while starting app")
      (.printStackTrace e System/err)
      (System/exit 1))))
