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
              [meal.routes :refer [view-page]]
              meal.sync
              [meal.views.index :as views.index]
              [meal.views.meal :as views.meal]
              [meal.views.navbar :as views.navbar]
              [meal.views.share :as views.share])
    (:import [goog History]))

(enable-console-print!)

(println "Edits to this text should show up in your developer console.")

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)



(defn get-state [db _]
  (reaction [@(reaction (get-in @db [:view] :index))
             @(reaction (get-in @db [:meal/id] nil))]))



(defonce history (History.))
(events/listen history EventType/NAVIGATE
               (fn [event]
                 (secretary/dispatch! (.-token event))))
(.setEnabled history true)

(defn change-view-handler [state [_ view meal-id]]
  (assoc state
         :view (keyword view)
         :meal/id meal-id
         :uri (str "/" view)))

(defn init []
  (register-handler :view/change change-view-handler)
  (register-sub :state get-state)
  (meal.channel/init)
  (views.share/init))


(defn main []
  (init)
  (let [state (subscribe [:state])]
    (fn []
      (let [[view meal] @state]
        (if (nil? meal)
          (.setToken history (name view))
          (.setToken history (str (name view) "/" meal)))
        [:div#content.container
         [:div.row
          [:div.col-xs-11 (match [view]
                                 [:meal] [views.meal/view meal]
                                 [:share] [views.share/view]
                                 [_] [views.index/view])]
          [:div.col-xs-1 [views.navbar/sidebar]]]]))))


(defn ^:export render-app
  []
  ;;(reset! app-db @storage/db)
  (r/render [main] (.getElementById js/document "app")))

(render-app)
