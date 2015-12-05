(ns meal.views.share
  (:require [clojure.string :as str]
            [meal.input :as input]
            [reagent.core :as r]))


(defn view []
  (let [name (r/atom "")
        ingredients (r/atom "")
        description (r/atom "")]
   (fn []
     [:div
      [:h1 "Share a meal!"]
      [:table.table
       [:tr
        [:th "Name of the meal"]
        [:td [input/text
              :name name
              :class :form-control
              :placeholder "Name of meal"]]]
       [:tr
        [:th "Picture"]
        [:td [:input {:type :file
                      :accept "image/*;capture=camera"}]]]
       [:tr
        [:th]
        [:td [:strong "Ingredients and Description are optional"]]]
       [:tr
        [:th "Ingredients"]
        [:td [input/textarea
              :ingredients ingredients
              :class :form-control
              :placeholder (str/join "\n" ["1 scoop of awesome"
                                           "2 table spoons of delicious"
                                           "1 table"])]]]

       [:tr
        [:th "Description"]
        [:td [input/textarea
              :description description
              :class :form-control
              :placeholder (str/join "\n" ["Take your scoop of awesome and mix it with your spoons of delicious"
                                           "Invite friends"
                                           "Sit down at the table with friends and enjoy"])]]]]])))
