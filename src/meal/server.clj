(ns meal.server
  (:require [com.stuartsierra.component :as component]

            ;; middleware for files and resources
            [ring.middleware.anti-forgery :refer [wrap-anti-forgery]]
            [ring.middleware.cookies :refer [wrap-cookies]]
            [ring.middleware.file-info :refer [wrap-file-info]]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.resource :refer [wrap-resource]]
            [ring.middleware.reload :refer [wrap-reload]]
            [ring.middleware.stacktrace :refer [wrap-stacktrace]]
            [taoensso.timbre :as log]))


(defrecord Server [dev? server run-server stop-server handler
                   server-options middleware-options]
  component/Lifecycle
  (start [this]
    (if server
      this
      (let [handler (-> (if dev?
                          (wrap-reload (wrap-stacktrace handler))
                          handler)
                        (wrap-anti-forgery)
                        (wrap-cookies {:http-only true})
                        (wrap-keyword-params)
                        (wrap-params)
                        (wrap-resource "public")
                        (wrap-file-info))]
        (log/info (format "Starting HTTP Server on port %s" (:port server-options)))
        (assoc this
               :server (run-server handler server-options)))))
  (stop [this]
    (if-not server
      this
      (do
        (log/info "Stopping HTTP Server")
        (stop-server server)
        (assoc this
               :server nil)))))


(defn get-server [data]
  (map->Server data))
