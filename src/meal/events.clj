(ns meal.events
  (:require [com.stuartsierra.component :as component]
            [clojure.core.async :as async :refer [<! go-loop]]
            [taoensso.sente :as sente]
            [taoensso.timbre :as log]
            [meal.channel :as channel]
            [meal.meal :as meal]
            [meal.user :as user]))

(defonce router (atom nil))

(defmulti event-handler :id)
(defmethod event-handler :default [e]
  ;; silently drop
  )

(defmethod event-handler :meal/add [{:keys [?reply-fn ?data] :as x}]
  (let [{:keys [name picture ingredients description auth]} ?data
        added (meal/add-meal<! {:name name
                                :ingredients ingredients
                                :description description
                                :user_id (:id (user/get (:id auth)))})]
    (when ?reply-fn
      (?reply-fn {:slug (:key added)}))
    (log/info {:what :meal/add
               :data ?data})))

(defmethod event-handler :meal/fetch-data [{:keys [?data ?reply-fn]}]
  (?reply-fn (meal/get-meal (:id ?data))))

(defmethod event-handler :meals/fetch [{:keys [?reply-fn ?data]}]
  (?reply-fn {:meals {:count (meal/get-count)
                      :meals (meal/get-meals 0 50)}}))

(defmethod event-handler :auth/authenticate [{:keys [?reply-fn ?data]}]
  (let [name (get-in ?data ["name"])
        id (get-in ?data ["id"])]
    (when-not (user/exists? id)
      (log/info {:what ::user-created
                 :name name
                 :id id})
      (user/create! name id))
    (when ?reply-fn
      (?reply-fn {:auth? true
                  :id id
                  :name name}))))

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
