(ns meal.login
  (:require [meal.channel :as channel]
            [re-frame.core :refer [dispatch register-handler]]
            [reagent.core :as r]))



(defn handle-authenticated [state [_ {:keys [id name]}]]
  (assoc state :auth {:id id :name name}))

(register-handler :auth/authenticated handle-authenticated)

(defn ^:export authenticate [response]
  (let [r (js->clj response)]
    (channel/chsk-send! [:auth/authenticate r]
                        1000
                        (fn [reply]
                          (dispatch [:auth/authenticated reply])))))

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
