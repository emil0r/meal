(ns meal.views.index
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :refer [subscribe register-sub]]))


(defn get-meals [db _]
  (reaction (get-in @db [:meal/meals] [])))
(register-sub :meal/meals get-meals)

(defn- show-meal [{:keys [name slug description image]}]
  [:div.row {:key slug}
   [:div.col-md-12
    [:a {:href (str "#meal/" slug)}
     [:h2 name]
     [:p.info description]]]])
(defn view []
  (let [meals (subscribe [:meal/meals])]
    (fn []
      [:div
       [:h1 "Meals"]
       (map show-meal @meals)])))
