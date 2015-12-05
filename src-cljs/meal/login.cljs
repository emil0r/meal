(ns meal.login
  (:require [meal.channel :as channel]
            [reagent.core :as r]))



(defn ^:export authenticate [response]
  (let [r (js->clj response)]
    (println r)
    (channel/chsk-send! [:auth/authenticate r])))

(defn ^:export check-login [response]
  (let [r (js->clj response)]
    (case (get r "status")
      "connected" (.api js/FB "/me" meal.login.authenticate)
      nil)))


(defn ^:export handle-login [response]
  (.log js/console response))

(defn ^:export handle-logout [response]
  (.log js/console response))

(defn ^:export logout []
  (.logout js/FB handle-logout))
