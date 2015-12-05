(ns meal.sync
  (:require [meal.channel :as channel]
            [meal.util :as util]
            [re-frame.core :refer [dispatch register-handler]]
            [re-frame.db :refer [app-db]]
            [taoensso.sente :as sente]))

(defonce init? (atom false))

;; from client

(defn handle-cb [state [_ data]]
  (if (sente/cb-success? data)
    (assoc state :meal/data data)))

(defn listen-meal [state [_ id]]
  (channel/chsk-send! [:meal/fetch-data {:id id}]
                      1000
                      #(dispatch [:meal/fetch-data %]))
  state)


(register-handler :meal/id listen-meal)
(register-handler :meal/fetch-data handle-cb)


(defn sync! [event data handler]
  (when @init?
    (channel/chsk-send! [event data]
                        1000
                        #(dispatch [handler %]))))

;; from server
(defn meals-handler [state [_ reply]]
  (if (sente/cb-success? reply)
    (assoc state
           :meal/count (get-in reply [:meals :count])
           :meal/meals (get-in reply [:meals :meals]))
    state))

(def router (atom nil))


(defmulti event-handler :event)
(defmethod event-handler :default [_]
  ;; do nothing
  )


(defmulti msg-handler :id)
(defmethod msg-handler :chsk/state [_])
(defmethod msg-handler :chsk/recv [msg]
  (event-handler msg))
(defmethod msg-handler :chsk/handshake [{:as ev-msg :keys [?data]}]
  (reset! init? true)
  (sync! :meals/fetch {:pp util/pp
                       ;; stricly speaking we shouldn't be doing this... but let's break things a bit
                       :page (get-in @app-db [:view/index] 1)} :meals/handler))
(defmethod msg-handler :default [_]
  ;; do nothing
  )


(defn init []
  (reset! router (sente/start-chsk-router! channel/ch-chsk msg-handler))
  (register-handler :meals/handler meals-handler))
