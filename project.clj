(defproject selenium-spike "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.9.0-beta2"]
                 [org.clojure/clojurescript "1.9.946"]

                 ;; Web server:
                 [org.immutant/web "2.1.9" :exclusions [ring/ring-core org.jboss.logging/jboss-logging]]
                 [ring/ring-core "1.6.2"]
                 [metosin/ring-http-response "0.9.0"]
                 [metosin/compojure-api "2.0.0-20171007.071354-2"]
                 [hiccup "1.0.5"]

                 ;; Front:
                 [re-frame "0.10.2"]
                 [reagent "0.8.0-alpha1"]

                 ;; Logging:
                 [org.clojure/tools.logging "0.4.0"]
                 [org.slf4j/jcl-over-slf4j "1.7.25"]
                 [org.slf4j/jul-to-slf4j "1.7.25"]
                 [org.slf4j/log4j-over-slf4j "1.7.25"]
                 [ch.qos.logback/logback-classic "1.2.3" :exclusions [org.slf4j/slf4j-api]]
                 [org.jboss.logging/jboss-logging "3.3.1.Final"]

                 ;; Workflow
                 [org.clojure/tools.namespace "0.3.0-alpha3"]

                 ;; Testing:
                 [eftest "0.3.1" :scope "test"]
                 [org.seleniumhq.selenium/selenium-server "3.6.0" :scope "test"]]

  :source-paths ["src/clj" "src/cljc"]
  :test-paths ["test/clj" "test/cljc"]

  :plugins [[lein-figwheel "0.5.13" :exclusions [[org.clojure/clojure]]]
            [lein-cljsbuild "1.1.7"]
            [lein-pdo "0.1.1"]
            [metosin/boot-alt-test "0.4.0-20171016.085822-2"]
            [lein-cloverage "1.0.9"]]

  :figwheel {:repl true
             :css-dirs ["resources/public/css"]}

  :profiles {:dev {:source-paths ["src/clj" "src/cljc" "src/dev"]
                   :resource-paths ["dev-resources" "target/dev/resources"]
                   :dependencies [[binaryage/devtools "0.9.7"]
                                  [figwheel-sidecar "0.5.14"]
                                  [com.cemerick/piggieback "0.2.2"]]
                   :repl-options {:nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}}
             :prod {:resource-paths ["target/prod/resources"]
                    :aot [server.main]
                    :main server.main
                    :uberjar-name "selenium-spike.jar"}}

  :cljsbuild {:builds [{:id "dev"
                        :source-paths ["src/cljc" "src/cljs"]
                        :figwheel {:websocket-host :js-client-host}
                        :compiler {:main front.main
                                   :asset-path "/js/out"
                                   :output-to "target/dev/resources/public/js/main.js"
                                   :output-dir "target/dev/resources/public/js/out"
                                   :source-map true
                                   :source-map-timestamp true
                                   :closure-defines {goog.DEBUG true}
                                   :preloads [devtools.preload]
                                   :external-config {:devtools/config {:features-to-install [:formatters :hints]}}}}
                       {:id "prod"
                        :source-paths ["src/cljc" "src/cljs"]
                        :compiler {:main front.main
                                   :optimizations :advanced
                                   :pretty-print false
                                   :output-to "target/prod/resources/public/js/main.js"
                                   :output-dir "target/prod/resources/public/js/out"
                                   :closure-defines {goog.DEBUG false}}}]}

  :auto-clean false

  :aliases {"fig" ["do" "clean" ["figwheel"]]
            "prod" ["with-profile" "prod" "do"
                    ["clean"]
                    ["build-info"]
                    ["cljsbuild" "once" "prod"]
                    ["uberjar"]]})
