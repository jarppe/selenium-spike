(ns server.ftest-fixture
  (:require [clojure.test :refer :all])
  (:import (org.openqa.selenium WebDriver By WebElement)
           (org.openqa.selenium.remote RemoteWebDriver)
           (org.openqa.selenium.chrome ChromeDriver)
           (java.util.concurrent TimeUnit)))

(def ^:dynamic ^RemoteWebDriver driver nil)

(defn with-local-chrome [f]
  (System/setProperty "webdriver.chrome.driver" "/Users/jarppe/swd/misc/selenium-spike/dev-resources/chromedriver")
  (let [d (ChromeDriver.)]
    (-> d .manage .timeouts (.implicitlyWait 1 TimeUnit/SECONDS))
    (try
      (binding [driver d]
        (f))
      (finally
        (.quit d)))))

(defn with-landing-page [f]
  (.get driver "http://localhost:3000/")
  (f))
