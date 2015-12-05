(ns meal.events
  (:require [com.stuartsierra.component :as component]
            [clojure.core.async :as async :refer [<! go-loop]]
            [taoensso.sente :as sente]
            [taoensso.timbre :as log]
            [meal.channel :as channel]
            [meal.user :as user]))

(defonce router (atom nil))

(defmulti event-handler :id)
(defmethod event-handler :default [e]
  ;; silently drop
  )
(defmethod event-handler :auth/authenticate [{:keys [?reply-fn ?data]}]
  (let [name (get-in ?data ["name"])
        id (get-in ?data ["id"])]
    (when-not (user/exists? id)
      (log/info {:what ::user-created
                 :name name
                 :id id})
      (user/create! name id))))

(defn wrap-event-handler [{:keys [?reply-fn ring-req] :as packet}]
  (cond


    ;; initial check passes. continue
    :else (event-handler packet)))



(defrecord EventsListener [started?]
  component/Lifecycle
  (start [this]
    (if started?
      this
      (do
        (log/info "Starting EventsListener")
        (reset! router (sente/start-chsk-router! channel/ch-chsk wrap-event-handler))
        (assoc this :started? true))))

  (stop [this]
    (if-not started?
      this
      (do
        (log/info "Stopping EventsListener")
        (when-let [stop-f @router]
          (stop-f))
        (assoc this :started? false)))))


(defn events-listener []
  (map->EventsListener {}))
