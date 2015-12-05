(ns meal.views.meal
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :refer [subscribe register-sub]]
            [reagent.core :as r]))

(defn get-meal [db _]
  (reaction (get-in @db [:meal/data] {})))
(register-sub :meal/data get-meal)

(defn view []
  (let [meal (subscribe [:meal/data])]
    (fn []
      (let [{:keys [name description ingredients image]} @meal]
        [:div
         [:table.table
          (for [[th td] [["Name" name]
                         ["Description" description]
                         ["Ingredients" ingredients]]]
            [:tr [:th th] [:td td]])]]))))
