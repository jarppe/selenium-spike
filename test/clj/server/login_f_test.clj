(ns server.login-f-test
  (:require [clojure.test :refer :all]
            [server.ftest-fixture :as ftest-fixture :refer [driver]]
            [server.ftest-util :as f])
  (:import (org.openqa.selenium WebDriver By Keys)
           (org.openqa.selenium.chrome ChromeDriver)
           (java.util.concurrent TimeUnit)
           (java.awt Desktop)))

(use-fixtures :once ftest-fixture/with-local-chrome)
(use-fixtures :each ftest-fixture/with-landing-page)

(deftest landing-page-has-no-login-status-test
  (is (empty? (-> (f/find-element! (f/by-css ".login-result"))
                  (f/find-child-elements)))))

(deftest initially-username-has-focus-test
  (is (-> (f/find-element! (f/by-css "#username"))
          (f/element-has-focus?))))

(deftest initially-login-button-is-disabled
  (is (-> (f/find-element! (f/by-css ".login-form button[type=submit]"))
          (f/element-disabled?))))

(deftest when-username-and-password-are-entered-button-is-enabled-test
  (-> (f/find-element! (f/by-id "username"))
      (f/click)
      (f/send-keys "a"))
  (-> (f/find-element! (f/by-id "password"))
      (f/click)
      (f/send-keys "a"))
  (is (f/wait-until {:timeout 2000}
        (-> (f/find-element! (f/by-css ".login-form button[type=submit]"))
            (f/element-enabled?)))))

(deftest user-foo-cant-login-with-wrong-password-test
  (-> (f/find-element! (f/by-id "username"))
      (f/click)
      (f/send-keys "foo"))
  (-> (f/find-element! (f/by-id "password"))
      (f/click)
      (f/send-keys "baz"))
  (-> (f/find-element! (f/by-css ".login-form button[type=submit]"))
      (f/assert-element-enabled)
      (f/click))
  (is (f/find-element (f/by-css ".login-result .alert-secondary"))))

(deftest user-foo-can-login-with-correct-password-test
  (-> (f/find-element! (f/by-id "username"))
      (f/click)
      (f/send-keys "foo"))
  (-> (f/find-element! (f/by-id "password"))
      (f/click)
      (f/send-keys "bar"))
  (-> (f/find-element! (f/by-css ".login-form button[type=submit]"))
      (f/assert-element-enabled)
      (f/click))
  (is (f/wait-until {:timeout 10000}
        (f/find-element! (f/by-css ".login-result .alert-success")))))


(comment

  (System/setProperty "webdriver.chrome.driver" "/Users/jarppe/swd/misc/selenium-spike/dev-resources/chromedriver")

  (def d (ChromeDriver.))

  (.get d "http://localhost:3000/")

  (let [screen-size (-> (java.awt.Toolkit/getDefaultToolkit) .getScreenSize)
        width (.-width screen-size)
        height (.-height screen-size)]
    (doto (-> d .manage .window)
      (.setPosition (org.openqa.selenium.Point. 0 0))
      (.setSize (org.openqa.selenium.Dimension. (int (/ width 3)) height)))
    )

  (binding [ftest-fixture/driver d]


    #_(-> (f/find-element (f/by-css "#username"))
        (f/click)
        (f/send-keys (Keys/chord (into-array CharSequence [Keys/BACK_SPACE]))))

    (-> (f/find-element (f/by-css "#username"))
        (f/click)
        (f/send-keys "a" "b" Keys/BACK_SPACE))


    )

  (.quit d)

  (Keys/chord ["a" Keys/BACK_SPACE])
  Keys/BACK_SPACE

  )
