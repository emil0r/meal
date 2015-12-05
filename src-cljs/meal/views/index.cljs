(ns meal.views.index
  (:require [meal.views.navbar :as views.navbar]))


(defn index []
  [:div.content
   (views.navbar/navbar)])
