(ns meal.sync
  (:require [meal.channel :as channel]
            [re-frame.core :refer [dispatch register-handler]]
            [taoensso.sente :as sente]))


(defn handle-cb [state [_ data]]
  (if (sente/cb-success? data)
    (assoc state :meal/data data)
    (println "argh" data)))

(defn listen-meal [state [_ id]]
  (channel/chsk-send! [:meal/fetch-data {:id id}]
                      1000
                      #(dispatch [:meal/fetch-data %]))
  state)


(register-handler :meal/id listen-meal)
(register-handler :meal/fetch-data handle-cb)
