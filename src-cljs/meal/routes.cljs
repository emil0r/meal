(ns meal.routes
  (:require-macros [secretary.core :refer [defroute]])
  (:require [secretary.core :as secretary]
            [re-frame.core :refer [dispatch]]))

(defroute view-page "/:tab" [tab]
  (dispatch [:view/change tab]))


(defroute view-meal "/meal/:meal-id" [meal-id]
  (dispatch [:meal/id meal-id])
  (dispatch [:view/change "meal" meal-id]))
