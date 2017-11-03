(ns server.index
  (:require [ring.util.http-response :as resp]
            [hiccup.core :as hiccup]
            [hiccup.page :as page])
  (:import (org.apache.commons.codec.digest DigestUtils)
           (org.joda.time DateTimeZone)))

(defn make-index-response []
  (-> (hiccup/html
        (page/html5
          [:head
           [:title "Selenium spike"]
           [:meta {:charset "utf-8"}]
           [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]
           [:link {:rel "stylesheet"
                   :href "https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.2/css/bootstrap.min.css"
                   :integrity "sha384-PsH8R72JQ3SOdhVi3uxftmaW6Vc51MKb0q5P2rRUpPvrszuE4W1povHYgTpBfshb"
                   :crossorigin "anonymous"}]
           [:link {:rel "stylesheet" :href "css/font-awesome.min.css"}]
           [:link {:rel "stylesheet" :href "css/styles.css"}]]
          [:body
           [:div#app
            [:div.loading
             [:h1 "Loading..."]]]
           (page/include-js "/js/main.js")]))
      (resp/ok)
      (resp/content-type "text/html; charset=utf-8")))

(def index-response (make-index-response))
