(ns user
  (:require [clojure.tools.namespace.repl :as tns]
            [clojure.java.classpath :as cp]
            [clojure.string :as str]
            [figwheel-sidecar.repl-api :as f]
            [server.system :as system])
  (:import (java.io File)))

(System/setProperty "org.jboss.logging.provider" "slf4j")

(defn fig-start []
  (f/start-figwheel!))

(defn fig-stop []
  (f/stop-figwheel!))

;; if you are in an nREPL environment you will need to make sure you
;; have setup piggieback for this to work
(defn cljs-repl []
  (f/cljs-repl))

(defn cljs-generated? [^File f]
  (-> f .getAbsolutePath (str/ends-with? "target/dev/resources")))

(apply tns/set-refresh-dirs (remove cljs-generated? (cp/classpath-directories)))

(def start system/start)
(def stop system/stop)

(defn go []
  (start)
  :ready)

(defn reset []
  (stop)
  (tns/refresh :after 'user/go))
