(ns meal.channel
  (:require-macros [cljs.core.async.macros :as asyncm :refer (go go-loop)])
  (:require [cljs.core.async :as async :refer (<! >! put! chan)]
            [taoensso.sente  :as sente :refer (cb-success?)]))


(defn init []
  (let [{:keys [chsk ch-recv send-fn state]}
        (sente/make-channel-socket! "/ch" ;; Note the same path as before
                                    {:type :auto})] ;; e/o #{:auto :ajax :ws}
    (def chsk chsk)
    (def ch-chsk ch-recv) ; ChannelSocket's receive channel
    (def chsk-send! send-fn) ; ChannelSocket's send API fn
    (def chsk-state state)   ; Watchable, read-only atom
    ))
