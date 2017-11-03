(ns server.handler
  (:require [clojure.tools.logging :as log]
            [compojure.api.sweet :as sweet :refer [GET]]
            [ring.util.http-response :as resp]
            [ring.middleware.resource :as resource]
            [server.index :as index]
            [server.middleware :as mw]))

(def routes
  (GET "/" []
    index/index-response))

(def api-handler
  (-> (sweet/api {:swagger {:ui "/swagger", :spec "/swagger.json"}} routes)
      (mw/wrap-no-cache)))

(defn resources-handler [request]
  (resource/resource-request request "public"))

(def handler (some-fn api-handler
                      resources-handler
                      (constantly (resp/not-found "the will is strong, but I'm unable to serve you"))))
