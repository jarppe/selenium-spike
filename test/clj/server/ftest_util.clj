(ns server.ftest-util
  (:require [clojure.test :refer :all]
            [server.ftest-fixture :refer [driver]])
  (:import (org.openqa.selenium By WebElement SearchContext)
           (org.openqa.selenium.support.ui WebDriverWait)
           (ch.qos.logback.core.util TimeUtil)
           (java.util.concurrent TimeUnit)))

;;
;; By:
;;

(defn by-css ^By [^String css-selector]
  (By/cssSelector css-selector))

(defn by-data-test-id ^By [test-id]
  (by-css (str "[data-test-id=" test-id "]")))

(defn by-id ^By [^String id]
  (By/id id))

(defn by-xpath ^By [^String xpath]
  (By/xpath xpath))

;;
;; Finding elements:
;;

(defn find-element!
  ([^By by] (find-element! driver by))
  ([^SearchContext context ^By by]
   (.findElement context by)))

(defn find-element
  ([^By by] (find-element driver by))
  ([^SearchContext context ^By by]
   (when context
     (try
       (find-element! context by)
       (catch org.openqa.selenium.NoSuchElementException _
         nil)))))

(defn find-elements
  ([^By by] (find-elements driver by))
  ([^SearchContext context ^By by]
   (when context
     (.findElements context by))))

(defn find-child-elements [^SearchContext context]
  (when context
    (.findElements context (by-xpath ".//*"))))

;;
;; Focus:
;;

(defn get-focus-element ^WebElement []
  (-> driver .switchTo .activeElement))

(defn element-has-focus? [^WebElement e]
  (= e (get-focus-element)))

;;
;; Disabled/enabled?
;;

(defn element-disabled? [^WebElement e]
  (when e
    (not (.isEnabled e))))

(defn element-enabled? [^WebElement e]
  (when e
    (.isEnabled e)))

(defn assert-element-enabled [^WebElement e]
  (when-not (element-enabled? e)
    (throw (ex-info "Element not enabled" {})))
  e)

;;
;; Send keys:
;;

(defn send-keys [^WebElement e & strs]
  (when e
    (doseq [s strs]
      (->> s (map str) (into-array CharSequence) (.sendKeys e))))
  e)

;;
;; Click:
;;

(defn click [^WebElement e]
  (when e
    (.click e))
  e)

(defn get-attribute [^WebElement e attribute-name]
  (when e
    (.getAttribute e attribute-name)))

(defn get-value [^WebElement e]
  (get-attribute e "value"))

(defn clear [^WebElement e]
  (when e
    (.clear e))
  e)

;;
;; Execute javascript:
;;

(defn execute-js [^String s & args]
  (.executeScript driver s (into-array Object args)))

;;
;; Wait:
;;

(defn- ->wait-function [f]
  (reify java.util.function.Function
    (apply [this_ driver_]
      (f))))

(defn wait-for
  ([f] (wait-for nil f))
  ([{:keys [timeout polling ignoring]} f]
   (let [^WebDriverWait w (WebDriverWait. driver 1 200)]
     (when timeout (.withTimeout w timeout TimeUnit/MILLISECONDS))
     (when polling (.pollingEvery w polling TimeUnit/MILLISECONDS))
     (when ignoring (.ignoring w ignoring))
     (.until w (->wait-function f)))))

(defmacro wait-until [& body]
  (let [[opts# body#] (if (-> body first map?)
                      [(first body) (rest body)]
                      [{} body])]
    `(wait-for ~opts# (fn [] ~@body#))))
