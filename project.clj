(defproject meal "0.1.0-SNAPSHOT"
  :description "Share a meal with me!"
  :url ""

  :main meal.core

  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/core.async "0.1.346.0-17112a-alpha"]
                 [ez-database "0.3.1-beta1"]
                 [http-kit "2.1.19"]
                 [org.postgresql/postgresql "9.2-1002-jdbc4"]
                 [com.jolbox/bonecp "0.8.0.RELEASE"]

                 [com.stuartsierra/component "0.3.0"]
                 [metosin/compojure-api "0.23.1"]
                 [org.clojure/core.match "0.3.0-alpha4"]
                 [hiccup "1.0.5"]
                 [ring/ring-anti-forgery "1.0.0"]
                 [slingshot "0.12.2"]
                 [com.cognitect/transit-clj "0.8.283"]
                 [clj-time "0.11.0"]
                 [buddy "0.7.2"]
                 [com.taoensso/timbre "4.1.4"]
                 [com.draines/postal "1.11.4"]
                 [com.taoensso/sente "1.6.0"]

                 [org.clojure/clojurescript "1.7.145"]
                 [com.cognitect/transit-cljs "0.8.225"]
                 [reagent "0.5.1"]
                 [re-frame "0.5.0-alpha1"]
                 [alandipert/storage-atom "1.2.4" ]
                 [clj-stacktrace "0.2.8"]
                 [secretary "1.2.3"]
                 [ez-web "0.3.0"]
                 [cljs-ajax "0.5.1"]
                 [com.taoensso/sente "1.6.0"]
                 [joplin.jdbc "0.3.4"]
                 [yesql "0.5.1"]]

  :uberjar-name "meal.jar"

  :plugins [[lein-cljsbuild "1.1.0"]
            [lein-figwheel "0.4.1"]
            [cider/cider-nrepl "0.9.1"]]

  :source-paths ["src" "src-cljs"]

  :clean-targets ^{:protect false} ["resources/public/js/compiled" "target"]

  :profiles {:dev {:plugins [[lein-midje "3.1.1"]]
                   :dependencies [[com.cemerick/piggieback "0.2.1"]
                                  [figwheel-sidecar "0.4.1"]
                                  [org.clojure/tools.nrepl "0.2.11"]
                                  [spyscope "0.1.5"]
                                  [midje "1.6.3"]]
                   :repl-options {:nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}
                   :injections [(require 'spyscope.core)
                                (use 'spyscope.repl)]}}

  :cljsbuild {
    :builds [{:id "dev"
              :source-paths ["src-cljs"]

              :figwheel { :on-jsload "meal.core/on-js-reload" }

              :compiler {:main meal.core
                         :asset-path "js/compiled/out"
                         :output-to "resources/public/js/compiled/meal.js"
                         :output-dir "resources/public/js/compiled/out"
                         :source-map-timestamp true }}
             {:id "min"
              :source-paths ["src-cljs"]
              :compiler {:output-to "resources/public/js/compiled/meal.min.js"
                         :main meal.core
                         :optimizations :advanced
                         :pretty-print false}}]}

  :figwheel {
             ;; :http-server-root "public" ;; default and assumes "resources"
             ;; :server-port 3449 ;; default
             ;; :server-ip "127.0.0.1"

             :css-dirs ["resources/public/css"] ;; watch and update CSS

             ;; Start an nREPL server into the running figwheel process
             ;; :nrepl-port 7888

             ;; Server Ring Handler (optional)
             ;; if you want to embed a ring handler into the figwheel http-kit
             ;; server, this is for simple ring servers, if this
             ;; doesn't work for you just run your own server :)
             ;; :ring-handler hello_world.server/handler

             ;; To be able to open files in your editor from the heads up display
             ;; you will need to put a script on your path.
             ;; that script will have to take a file path and a line number
             ;; ie. in  ~/bin/myfile-opener
             ;; #! /bin/sh
             ;; emacsclient -n +$2 $1
             ;;
             ;; :open-file-command "myfile-opener"

             ;; if you want to disable the REPL
             ;; :repl false

             ;; to configure a different figwheel logfile path
             ;; :server-logfile "tmp/logs/figwheel-logfile.log"
             })
