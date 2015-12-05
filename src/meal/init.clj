(ns meal.init
  (:require [clojure.edn :as edn]
            [com.stuartsierra.component :as component]
            [org.httpkit.server :as http-server :refer [run-server]]
            [taoensso.timbre :as log]
            [meal.database :as db]
            [meal.routes :as routes]
            [meal.server :as server]
            [meal.settings :as settings]))



(defn- system-map [{:keys [settings run-server stop-server
                           server-options prod? log
                           admins db-specs]}]
  (let []
    (component/system-map
     :settings settings
     :database (db/database db-specs)
     :server (component/using (server/get-server {:server-options server-options
                                                  :run-server run-server
                                                  :stop-server stop-server
                                                  :handler routes/app-routes
                                                  :dev? (not prod?)})
                              []))))


(defonce system (atom nil))

(defn stop []
  (when-not (nil? @system)
    (component/stop @system)
    (reset! system nil)))

(defn- stop-server [server]
  (when (fn? server)
    (server)))


(defn init [settings-path]
  (let [settings (component/start (settings/settings settings-path))]

    (reset! system (component/start
                    (system-map
                     {:prod? (settings/prod? settings)
                      :log (settings/get settings [:log])
                      :admins (settings/get settings [:admins])
                      :settings settings
                      :db-specs (settings/get settings [:db :specs])
                      :server-options (settings/get settings [:server :options])
                      :middleware-options (settings/get settings [:server :middleware])
                      :run-server run-server
                      :stop-server stop-server})))

    ;; log uncaught exceptions in threads
    (Thread/setDefaultUncaughtExceptionHandler
     (reify Thread$UncaughtExceptionHandler
       (uncaughtException [_ thread ex]
         (log/error {:what :uncaught-exception
                     :exception ex
                     :where (str "Uncaught exception on" (.getName thread))}))))

    ;; add shutdown hook
    (.addShutdownHook
     (Runtime/getRuntime)
     (proxy [Thread] []
       (run []
         (stop))))))
