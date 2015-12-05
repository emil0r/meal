(ns ^:figwheel-always meal.core
    (:require-macros [reagent.ratom :refer [reaction]])
    (:require [cljs.core.match :refer-macros [match]]
              [goog.events :as events]
              [goog.history.EventType :as EventType]
              [re-frame.core :refer [dispatch
                                     register-sub
                                     register-handler
                                     subscribe]]
              [re-frame.db :refer [app-db]]
              [reagent.core :as r]
              [secretary.core :as secretary]
              meal.channel
              [meal.login :as login]
              ;;[meal.logout :refer [logout]]
              [meal.routes :refer [view-page]]
              ;;[meal.storage :as storage]
              [meal.views.index :as views.index]
              ;;meal.updates
              ;;[meal.util :refer [logo]]
              )
    (:import [goog History]))

(enable-console-print!)

(println "Edits to this text should show up in your developer console.")

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)



(defn get-state [db _]
  (reaction (get-in @db [:view] :index)))



(defonce history (History.))
(events/listen history EventType/NAVIGATE
               (fn [event]
                 (secretary/dispatch! (.-token event))))
(.setEnabled history true)

(defn change-view-handler [state [_ view]]
  (assoc state
         :view (keyword view)
         :uri (str "/" view)))

(defn init []
  (register-handler :view/change change-view-handler)
  (register-sub :state get-state)
  (meal.channel/init))


(defn main []
  (init)
  (let [state (subscribe [:state])]
    (fn []
      (let [view @state]
        (.setToken history (name view))
        (match [view]
               ;;[:login] [login/show-login]
               [_] [views.index/index])))))


(defn ^:export render-app
  []
  ;;(reset! app-db @storage/db)
  (r/render [main] (.getElementById js/document "app")))

(render-app)
